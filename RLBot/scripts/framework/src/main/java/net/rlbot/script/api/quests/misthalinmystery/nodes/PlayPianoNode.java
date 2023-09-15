package net.rlbot.script.api.quests.misthalinmystery.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Vars;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Map;

@Slf4j
public class PlayPianoNode extends UnaryNode {

    private static final WidgetAddress PIANO = new WidgetAddress(554, 1);

    private static final WidgetAddress PIANO_KEYS = new WidgetAddress(554, 5);

    private static final int KEYS_PLAYED = 4049;

    private static final Map<Integer, WidgetAddress> KEY_TO_PLAY = Map.of(
            0,  new WidgetAddress(554, 21),
            1, new WidgetAddress(554, 22),
            2, new WidgetAddress(554, 25),
            3, new WidgetAddress(554, 28)
    );

    public PlayPianoNode() {
        super("Playing the piano");
    }

    @Override
    protected ActionResult doExecute() {

        if(Quests.getStage(Quest.MISTHALIN_MYSTERY) >= 80) {
            return ActionResult.SUCCESS;
        }

        if(!PIANO.isWidgetVisible()) {

            var piano = SceneObjects.getNearest("Piano");

            if(piano == null) {
                log.warn("Unable to find piano");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!piano.interact("Play") || !Time.sleepUntil(PIANO::isWidgetVisible, () -> Players.getLocal().isMoving(), 2400)) {
                log.warn("Failed to play the piano");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        var keysPlayed = Vars.getBit(KEYS_PLAYED);

        var keyAddress = KEY_TO_PLAY.get(keysPlayed);

        var key = keyAddress.resolve();

        if(key == null) {
            log.warn("Unable to find key");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        if(!key.interact(Predicates.always()) || !Time.sleepUntil(() -> Vars.getBit(KEYS_PLAYED) != keysPlayed, 1200)) {
            log.warn("Unable to play key");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.IN_PROGRESS;
    }
}
