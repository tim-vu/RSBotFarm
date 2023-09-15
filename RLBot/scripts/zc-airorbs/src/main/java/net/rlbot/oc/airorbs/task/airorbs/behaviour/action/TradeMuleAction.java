package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.items.Trade;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class TradeMuleAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Trading with the mule";
    }

    @SneakyThrows
    @Override
    public void execute() {

        var mulingRequest = getBlackboard().get(Keys.MULING_REQUEST);

        if(!Trade.isOpen() && !Inventory.contains(ItemId.COINS_995)) {
            getBlackboard().put(Keys.MULING_REQUEST, null);
            getBlackboard().put(Keys.HAS_GOLD, false);
            getBlackboard().get(Keys.MULING_API).completeMulingRequest(mulingRequest.getId());
            log.info("Trade with the mule completed");
            return;
        }

        var mule = Players.query()
                .names(mulingRequest.getDisplayName())
                .results()
                .nearest();

        if(mule == null){
            log.warn("Mule not found");
            Time.sleep(5000);
            return;
        }

        if(!Trade.isOpen()){

            Action.logPerform("TRADE_WITH_MULE");
            if(!mule.interact("Trade with") || !Time.sleepUntil(Trade::isOpen, () -> Players.getLocal().isMoving(), 12000))
            {
                Action.logTimeout("TRADE_WITH_MULE");
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        if (Trade.isFirstScreenOpen()) {

            if(!Trade.contains(false, ItemId.COINS_995)) {

                Action.logPerform("OFFER_ALL_COINS");
                if(!Trade.offerAll(ItemId.COINS_995)) {
                    Action.logFail("OFFER_ALL_COINS");
                    return;
                }

                Reaction.REGULAR.sleep();
                return;
            }

            if(!Trade.hasAcceptedFirstScreen(false)) {

                Action.logPerform("ACCEPT_FIRST_SCREEN");
                if(!Trade.acceptFirstScreen()) {
                    Action.logFail("ACCEPT_FIRST_SCREEN");
                    return;
                }

                Reaction.REGULAR.sleep();
                return;
            }

            if(!Trade.hasAcceptedFirstScreen(true)) {

                log.trace("Waiting for the mule to accept the first screen");
                if(!Time.sleepUntil(() -> Trade.hasAccepted(true), 6000)) {
                    log.warn("The mule has not accepted the request yet");
                    return;
                }

                Reaction.REGULAR.sleep();
                return;
            }

            return;
        }

        if (!Trade.hasAcceptedSecondScreen(false)) {

            log.debug("Accepting the second screen");
            if(!Trade.acceptSecondScreen()) {
                log.warn("Failed to accept the second screen");
                return;
            }

            Reaction.REGULAR.sleep();
        }

        if(!Time.sleepUntil(() -> !Trade.isOpen() && !Inventory.contains(ItemId.COINS_995), 12000)) {
            log.warn("The mule has not accepted the request within the timeout");
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
