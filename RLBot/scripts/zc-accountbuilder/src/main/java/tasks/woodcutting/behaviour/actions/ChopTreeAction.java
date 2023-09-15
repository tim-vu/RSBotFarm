package tasks.woodcutting.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.woodcutting.data.Keys;

@Slf4j
public class ChopTreeAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Chopping tree";
    }

    @Override
    public void execute() {

        var treeKind = getBlackboard().get(Keys.TREE);
        var trainingArea = getBlackboard().get(BasicActivityKeys.TRAINING_AREA);

        var tree = SceneObjects.query()
                .names(treeKind.getNames())
                .within(trainingArea)
                .results()
                .nearest();

        if(tree == null) {
            log.debug("Unable to find tree");
            Time.sleepTick();
            return;
        }

        if(!tree.interact("Chop down") || !Time.sleepUntil(() -> Players.getLocal().isAnimating(),  () -> Players.getLocal().isMoving(), 2400)) {
            log.warn("Failed to chop tree");
            Time.sleepTick();
            return;
        }

        getBlackboard().put(Keys.CURRENT_TREE_POSITION, tree.getPosition());
    }

}
