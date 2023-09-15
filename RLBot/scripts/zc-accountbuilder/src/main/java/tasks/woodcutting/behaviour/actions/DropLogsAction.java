package tasks.woodcutting.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.woodcutting.data.Keys;

@Slf4j
public class DropLogsAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Dropping logs";
    }

    @Override
    public void execute() {

        var axe = getBlackboard().get(Keys.AXE);

        var result = Inventory.dropAllExcept(true, axe.getItemId());

        if(result == ActionResult.FAILURE) {
            log.warn("Failed to drop item");
            Time.sleepTick();
            return;
        }

        if(result == ActionResult.IN_PROGRESS) {
            Reaction.PREDICTABLE.sleep();
            return;
        }

        if(!Time.sleepUntil(() -> !Inventory.contains(i -> i.getId() != axe.getItemId()), 1200)) {
            log.warn("Failed to wait for the items to be dropped");
            Time.sleepTick();
            return;
        }

        getBlackboard().put(Keys.IS_DROPPING, true);
    }
}
