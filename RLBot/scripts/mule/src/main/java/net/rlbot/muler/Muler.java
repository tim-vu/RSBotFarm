package net.rlbot.muler;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.event.listeners.ChatMessageListener;
import net.rlbot.api.event.types.ChatMessageEvent;
import net.rlbot.api.event.types.ChatMessageType;
import net.rlbot.api.game.Worlds;
import net.rlbot.api.items.Trade;
import net.rlbot.api.scene.Players;
import net.rlbot.api.script.Script;
import net.rlbot.api.script.ScriptManifest;
import net.rlbot.api.script.randoms.LoginHandler;
import net.rlbot.muler.api.model.Position;
import net.rlbot.muler.handlers.MuleLoginHandler;
import net.rlbot.muler.farm.FarmConnector;
import net.rlbot.script.api.reaction.Reaction;

import java.util.regex.Pattern;

@Slf4j
@ScriptManifest(name = "Muler", author = "Tim", version = 0.1)
public class Muler extends Script implements ChatMessageListener {

    private final MuleLoginHandler muleLoginHandler = new MuleLoginHandler();

    private FarmConnector farmConnector;

    private String lastTradeRequest = null;

    @Override
    public int loop() {

        if(!farmConnector.isInitialized()) {
            var player = Players.getLocal();
            var position = new Position();
            position.setX(player.getPosition().getX());
            position.setY(player.getPosition().getY());
            position.setZ(player.getPosition().getPlane());
            farmConnector.init(player.getName(), position, Worlds.getCurrent());
            log.info("Successfully initialized the farm connector");
            return 1000;
        }

        var mulingRequests = farmConnector.getMulingRequests();

        if(mulingRequests.size() == 0) {
            muleLoginHandler.setDisabled(true);

            if(Game.isLoggedIn()) {
                log.debug("Logging out");
                Game.logout();
                return 1000;
            }

            return 1000;
        }

        muleLoginHandler.setDisabled(false);

        if(!Game.isLoggedIn()) {
            return 2000;
        }

        if(Trade.isSecondScreenOpen()){

            log.info("Accepting the second screen");
            if(!Trade.acceptSecondScreen()) {
                log.warn("Failed to accept the second trade screen");
                return 1000;
            }

            Reaction.REGULAR.sleep();

            if(!Time.sleepUntil(() -> !Trade.isOpen(), 6000))
            {
                log.info("The bot did not accept the trade within the timeout");
                Trade.declineSecondScreen();
                return 2000;
            }

            return 1000;
        }

        if(Trade.isFirstScreenOpen()){

            if(!Time.sleepUntil(() -> isOfferedGold() && Trade.hasAcceptedFirstScreen(true), 8000))
            {
                log.info("The bot did not offer any gold");
                Reaction.REGULAR.sleep();
                Trade.declineFirstScreen();
                return 1000;
            }

            Reaction.REGULAR.sleep();

            log.info("Accepting the first screen");
            if(!Trade.acceptFirstScreen() || !Time.sleepUntil(() -> Trade.isSecondScreenOpen() || !Trade.isOpen(), 8000))
            {
                log.warn("Failed to accept the first trade screen");
                return 1000;
            }

            Reaction.REGULAR.sleep();
            return 1000;
        }

        if(lastTradeRequest == null) {
            return 1000;
        }

        var player = Players.getNearest(lastTradeRequest);

        if(player == null) {
            log.debug("Could not find the player " + lastTradeRequest);
            return 1000;
        }

        log.info("Sending trade request to " + lastTradeRequest);
        if(!player.interact("Trade with")) {
            return 1000;
        }

        if(!Time.sleepUntil(Trade::isFirstScreenOpen, () -> Players.getLocal().isMoving(), 12000)) {
            log.debug("Failed to open the trade screen");
            return 1000;
        }

        Reaction.REGULAR.sleep();
        return 1000;
    }

    private static final int GOLD = 995;

    private static boolean isOfferedGold() {
        return Trade.getFirst(true, GOLD) != null;
    }

    @Override
    public boolean onStart(String[] args) {

        if(args.length != 1) {
            return false;
        }

        farmConnector = new FarmConnector(args[0]);

        removeRandomEventHandler(LoginHandler.class);
        addRandomEventHandler(muleLoginHandler);

        super.onStart(args);
        return true;
    }

    private static final Pattern TRADE_REQUEST_PATTERN = Pattern.compile("(.*) wants to trade with you\\.");

    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {

        if(chatMessageEvent.getType() != ChatMessageType.TRADEREQ) {
            return;
        }

        //extract the name of the player who sent the trade request using the regex
        var matcher = TRADE_REQUEST_PATTERN.matcher(chatMessageEvent.getMessage());

        if(!matcher.find()) {
            log.debug("Failed to parse trade request message: " + chatMessageEvent.getMessage());
            return;
        }

        var playerName = matcher.group(1);

        //if the player who sent the trade request is not the player we are muling for, return
        if(farmConnector.getMulingRequests().stream().noneMatch(r -> r.getDisplayName().equals(playerName))) {
            log.debug("Received trade request from " + playerName + " but we are not muling for them");
            return;
        }

        log.info("Received trade request from " + playerName);
        lastTradeRequest = playerName;
    }

}
