package tasks.combat.common.combattask.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.combat.common.combattask.behaviour.Looting;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class LootItemsAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Picking up items";
    }

    private static final Set<Integer> LOOTED_ITEM_IDS = new HashSet<>();

    @Override
    public void execute() {

        var loot = Looting.getLoot(getBlackboard());

        if(loot == null) {
            log.warn("Unable to find pickable to loot");
            Time.sleepTick();
            return;
        }

        var count = Inventory.getCount(true, loot.getId());
        var equipmentCount = Equipment.getCount(true, loot.getId());
        if(!loot.interact("Take") || !Time.sleepUntil(() ->
                Inventory.getCount(true, loot.getId()) > count ||
                        Equipment.getCount(true, loot.getId()) > equipmentCount, () -> Players.getLocal().isMoving(), 2400)) {
            log.warn("Failed to pick up item");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
