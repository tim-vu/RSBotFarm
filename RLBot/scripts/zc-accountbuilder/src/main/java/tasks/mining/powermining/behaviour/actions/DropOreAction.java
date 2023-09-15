package tasks.mining.powermining.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.mining.powermining.data.Keys;

@Slf4j
public class DropOreAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Dropping ore";
    }

    @Override
    public void execute() {

        var pickaxe = getBlackboard().get(Keys.PICKAXE);

        var result = Inventory.dropAllExcept(true, pickaxe.getItemId(), ItemId.COINS_995);

        if (result == ActionResult.FAILURE) {
            log.warn("Failed to drop item");
            Time.sleepTick();
            return;
        }

        if (result == ActionResult.IN_PROGRESS) {
            Reaction.PREDICTABLE.sleep();
            return;
        }

        if (!Time.sleepUntil(() -> !Inventory.contains(i -> i.getId() != pickaxe.getItemId()), 1200)) {
            log.warn("Failed to wait for the items to be dropped");
            Time.sleepTick();
            return;
        }

        getBlackboard().put(Keys.IS_DROPPING, false);
    }
}
