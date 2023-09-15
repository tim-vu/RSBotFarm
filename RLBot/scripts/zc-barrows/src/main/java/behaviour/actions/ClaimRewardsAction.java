package behaviour.actions;

import enums.Brother;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.WidgetID;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class ClaimRewardsAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Claiming rewards";
    }

    @Override
    public void execute() {

        var chest = SceneObjects.getNearest("Chest");

        if(chest == null) {
            log.warn("Unable to find chest");
            Time.sleepTick();
            return;
        }


        if(chest.getActions()[0].equals("Search")) {

            if(!Brother.areBrothersDead()) {
                Time.sleepUntil(Brother::areBrothersDead, 2400);
                return;
            }

            Action.logPerform("SEARCH_CHEST");
            if(!chest.interact("Search")) {
                Action.logFail("SEARCH_CHEST");
                Time.sleepTick();
                return;
            }

            if(!Time.sleepUntil(BARROWS_CHEST::isWidgetVisible, 1200)) {
                Action.logTimeout("SEARCH_CHEST");
                Time.sleepTick();
                return;
            }

            Reaction.PREDICTABLE.sleep();
            return;
        }

        if(chest.getActions()[0].equals("Open"))
        {
            Action.logPerform("OPEN_CHEST");
            if(!chest.interact("Open")) {
                Action.logFail("OPEN_CHEST");
                Time.sleepTick();
                return;
            }

            if(Brother.areBrothersDead()) {
                Reaction.PREDICTABLE.sleep();
                return;
            }

            if(!Time.sleepUntil(Brother::isBrotherPresent, 2400)) {
                Action.logTimeout("OPEN_CHEST");
                Time.sleepTick();
                return;
            }

            Reaction.PREDICTABLE.sleep();
            return;
        }

        log.warn("Unknown first action for chest");
    }

    private static final WidgetAddress BARROWS_CHEST = new WidgetAddress(WidgetID.BARROWS_GROUP_ID, 1);
}
