package behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.BaseItem;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.ItemPriceAPI;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tunnels.Loot;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PickupLootAction extends LeafNodeBase {

    private static final Comparator<BaseItem> ITEM_COMPARATOR = Comparator.comparingInt(i -> ItemPriceAPI.getSellPrice(i.getId()) * i.getQuantity());

    @Override
    public String getStatus() {
        return null;
    }
    @Override
    public void execute() {

        List<Item> items = Inventory.getAll();

        List<Pickable> loot = Loot.getLoot();

        var stackableLoot = loot.stream().filter(p -> p.isStackable() && Inventory.contains(p.getId())).findFirst();

        if(stackableLoot.isPresent()) {
            pickupItem(stackableLoot.get());
            return;
        }

        var combined = Stream.concat(items.stream(), loot.stream())
                .sorted(ITEM_COMPARATOR.reversed())
                .toList();

        if(combined.size() <= Inventory.SLOTS) {

            if(loot.isEmpty()) {
                log.warn("No loot when trying to pick up loot");
                Time.sleepTick();
                return;
            }

            pickupItem(loot.get(0));
            return;
        }

        var itemsToLeave = combined.size() - Inventory.SLOTS;

        for(var i = 0; i < itemsToLeave; i++) {

            var baseItem = combined.get(i);

            if(!(baseItem instanceof Item item)) {
                continue;
            }

            dropItem(item);
            return;
        }

        for(var i = itemsToLeave; i < combined.size(); i++) {

            var baseItem = combined.get(i);

            if(!(baseItem instanceof Pickable pickable)) {
                continue;
            }

            pickupItem(pickable);
        }
    }

    private static void dropItem(Item item) {
        var count = Inventory.getCount(item.getId());

        Action.logPerform("DROP_ITEM");
        if(!item.interact("Drop")) {
            Action.logFail("DROP_ITEM");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Inventory.getCount(item.getId()) < count, 1800)) {
            Action.logTimeout("DROP_ITEM");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }

    private static void pickupItem(Pickable pickable) {

        var count = Inventory.getCount(true, pickable.getId());

        Action.logPerform("PICKUP_LOOT");
        if(!pickable.interact("Take")) {
            Action.logFail("PICKUP_LOOT");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Inventory.getCount(true, count) < count, () -> Players.getLocal().isMoving(), 1800)) {
            Action.logTimeout("PICKUP_LOOT");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
