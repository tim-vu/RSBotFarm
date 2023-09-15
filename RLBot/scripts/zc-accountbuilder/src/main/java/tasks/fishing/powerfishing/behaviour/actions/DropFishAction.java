package tasks.fishing.powerfishing.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.fishing.powerfishing.data.Keys;

@Slf4j
public class DropFishAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Dropping fish";
    }

    @Override
    public void execute() {

        var itemIds = getBlackboard().get(Keys.FISHING_SPOT).getFishingType().getLoadout().getAllItemIds();

        var result = Inventory.dropAllExcept(true, i -> itemIds.contains(i.getId()));

        if (result == ActionResult.FAILURE) {
            log.warn("Failed to drop item");
            Time.sleepTick();
            return;
        }

        if (result == ActionResult.IN_PROGRESS) {
            Reaction.PREDICTABLE.sleep();
            return;
        }

        if (!Time.sleepUntil(() -> !Inventory.contains(i -> !itemIds.contains(i.getId())), 1200)) {
            log.warn("Failed to wait for the items to be dropped");
            Time.sleepTick();
            return;
        }

        getBlackboard().put(Keys.IS_DROPPING, true);
        return;
    }
}
