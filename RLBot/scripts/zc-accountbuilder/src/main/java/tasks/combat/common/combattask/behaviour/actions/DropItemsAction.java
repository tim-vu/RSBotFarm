package tasks.combat.common.combattask.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.combat.common.combattask.data.CombatKeys;

@Slf4j
public class DropItemsAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Dropping items";
    }

    @Override
    public void execute() {

        var food = Inventory.getFirst(getBlackboard().get(CombatKeys.FOOD_ITEM_ID));

        if(food == null) {
            log.warn("Unable to find food to drop");
            Time.sleepTick();
            return;
        }

        var count = Inventory.getCount(food.getId());
        if(!food.interact("Drop") || !Time.sleepUntil(() -> Inventory.getCount(food.getId()) < count, 2400)) {
            log.warn("Failed to drop food");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
