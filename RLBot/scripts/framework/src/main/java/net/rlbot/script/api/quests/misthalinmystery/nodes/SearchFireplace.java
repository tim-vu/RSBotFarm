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
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Map;

@Slf4j
public class SearchFireplace extends UnaryNode {

    private static final int GEMS_ENTERED = 4050;
    private static final WidgetAddress GEMSTONE_PANEL = new WidgetAddress(555, 0);

    private static final Map<Integer, Integer> INDEX_TO_RUBY_WIDGET_ID = Map.of(
            0, 19,
            1, 3,
            2, 11,
            3, 23,
            4, 7,
            5, 15
    );

    public SearchFireplace() {
        super("Searching the piano");
    }

    @Override
    protected ActionResult doExecute() {

        if(Quests.getStage(Quest.MISTHALIN_MYSTERY) == 105) {
            return ActionResult.SUCCESS;
        }

        if(Dialog.isOpen()) {

            Dialog.continueSpace();

            if(!Time.sleepUntil(GEMSTONE_PANEL::isWidgetVisible, 1200)) {
                log.warn("Failed to open the gemstone panel");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(!GEMSTONE_PANEL.isWidgetVisible()) {

            var fireplace = SceneObjects.getNearest("Fireplace");

            if(fireplace == null) {
                log.warn("Unable to find fireplace");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!fireplace.interact("Search") || !Time.sleepUntil(Dialog::isOpen, () -> Players.getLocal().isMoving(), 1200)) {
                log.warn("Failed to open gemstone interface");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        var enteredGems = Vars.getBit(GEMS_ENTERED);
        var gemstoneWidgetId = INDEX_TO_RUBY_WIDGET_ID.get(enteredGems);

        var gemstone = Widgets.get(555, gemstoneWidgetId);

        if(!Widgets.isVisible(gemstone)) {
            log.warn("Unable to find gem widget");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        if(!gemstone.interact(Predicates.always())) {
            log.warn("Failed to switch gem");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        if(enteredGems == 5) {

            if(!Time.sleepUntil(() -> !GEMSTONE_PANEL.isWidgetVisible(), 1200)) {
                log.warn("Failed to wait for the gemstone panel to close");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(!Time.sleepUntil(() -> Vars.getBit(GEMS_ENTERED) > enteredGems, 1200)) {
            log.warn("Failed to wait for the gem to be entered");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.IN_PROGRESS;
    }
}
