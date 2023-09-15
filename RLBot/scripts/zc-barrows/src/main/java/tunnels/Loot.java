package tunnels;

import data.Settings;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Pickables;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.ItemPriceAPI;

import java.util.List;

@Slf4j
public class Loot {

    public static boolean shouldPickupItems(Settings settings) {

        var pickables = getLoot();

        if(!pickables.isEmpty() && !Inventory.isFull()) {
            log.info("Inventory not full and loot on the ground");
            return true;
        }

        if(pickables.stream().anyMatch(p -> p.isStackable() && Inventory.contains(p.getId()))) {
            log.info("Stackable loot on the ground that's already in inventory");
            return true;
        }

        var droppableItems = getDroppableItems(settings);
        for(var pickable : pickables) {

            var pickableValue = pickable.getQuantity() * ItemPriceAPI.getSellPrice(pickable.getId());

            for(var item : droppableItems) {

                var itemValue = item.getQuantity() * ItemPriceAPI.getSellPrice(item.getId());

                if(pickableValue > itemValue) {
                    log.info("Loot found that is more valuable than an item in our inventory");
                    log.info("Loot: {} ({}) > Item: {} ({})", pickable.getName(), pickable.getQuantity(), item.getName(), item.getQuantity());
                    return true;
                }
            }
        }

        return false;
    }

    public static List<Pickable> getLoot() {
        return Pickables.query()
                .within(Tunnels.CHEST_ROOM)
                .filter(p -> p.getId() != ItemId.BONES)
                .results()
                .list();
    }
    private static List<Item> getDroppableItems(Settings settings) {
        return Inventory.query()
                .filter(Item::isTradeable)
                .filter(i -> !settings.getLoadout().test(i) || settings.getFoodItemIds().contains(i.getId()) || ItemIds.PRAYER_POTION.contains(i.getId()))
                .results()
                .list();
    }

}
