package behaviour.actions;

import data.Areas;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class ExitMoundAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Exiting mound";
    }

    @Override
    public void execute() {

        var staircase = SceneObjects.getNearest("Staircase");

        if(staircase == null) {
            log.warn("Unable to find staircase");
            Time.sleepTick();
            return;
        }

        Action.logPerform("EXIT_MOUND");
        if(!staircase.interact("Climb-up")) {
            Action.logFail("EXIT_MOUND");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> !Areas.MOUNDS.contains(Players.getLocal()), () -> Players.getLocal().isMoving(), 2400)) {
            Action.logTimeout("EXIT_MOUND");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
