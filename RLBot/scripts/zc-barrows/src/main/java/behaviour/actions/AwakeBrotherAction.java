package behaviour.actions;

import data.Keys;
import enums.Brother;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class AwakeBrotherAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Awaking brother";
    }

    @Override
    public void execute() {

        var sarcophagus = SceneObjects.getNearest("Sarcophagus");

        if(sarcophagus == null) {
            log.warn("Unable to find sarcophagus");
            Time.sleepTick();
            return;
        }

        Action.logPerform("AWAKE_BROTHER");
        if(sarcophagus.interact("Search")) {
            Action.logFail("AWAKE_BROTHER");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Dialog.isOpen() || Brother.getMyBrother() != null, 1800)) {
            Action.logTimeout("AWAKE_BROTHER");
            Time.sleepTick();
            return;
        }

        if(Dialog.isOpen()) {
            var brother = getBlackboard().get(Keys.REMAINING_BROTHERS).poll();
            getBlackboard().put(Keys.BROTHER_IN_TUNNELS, brother);

            Reaction.REGULAR.sleep();
            return;
        }

        //noinspection DataFlowIssue
        if(!Brother.getBrotherByMound().isAlive()) {
            log.info("Brother seems to be dead already");
            getBlackboard().get(Keys.REMAINING_BROTHERS).poll();
        }

        Reaction.REGULAR.sleep();
    }
}
