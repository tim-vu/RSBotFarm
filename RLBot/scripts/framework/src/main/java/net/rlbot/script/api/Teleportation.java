package net.rlbot.script.api;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.common.Time;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.script.api.reaction.Reaction;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class Teleportation {

    private static final WidgetAddress TELEPORT_POPUP = new WidgetAddress(187, 3);

    public static boolean useTeleport(List<Integer> itemIds, String destination, Area destinationArea) {

        if (!hasTeleportationItem(itemIds))
        {
            return false;
        }

        if (itemIds.stream().anyMatch(Inventory::contains)) {

            var item = Inventory.getFirst(i -> itemIds.contains(i.getId()));

            if (!item.interact("Rub") || !Time.sleepUntil(() -> Dialog.isOpen() || TELEPORT_POPUP.isWidgetVisible(), 3000))
            {
                log.warn("Failed to rub item: {}", item);
                return false;
            }

            var popup = TELEPORT_POPUP.resolve();
            if(Widgets.isVisible(popup)) {

                var children = popup.getChildren();

                if (children == null) {
                    log.warn("Teleport popup children are null");
                    Time.sleepTick();
                    return false;
                }

                for (int i = 0; i < children.length; i++) {

                    var teleportItem = children[i];

                    if (!teleportItem.getText().contains(destination)) {
                        continue;
                    }

                    Keyboard.sendText(String.valueOf((i + 1)), true);
                    return Time.sleepUntil(() -> destinationArea.contains(Players.getLocal()), () -> Players.getLocal().isAnimating(), 8000);
                }
            }

            Reaction.REGULAR.sleep();

            if (!Dialog.chooseOption(destination))
            {
                log.warn("Failed to choose option: {}", destination);
                return false;
            }

        } else {

            log.debug("Teleport item in equipment");

            var item = Equipment.getFirst(i -> itemIds.contains(i.getId()));

            if(item == null) {
                return false;
            }

            if (!item.interact(o -> o.contains(destination))) {
                log.warn("Failed to interact with item: {}", item);
                return false;
            }
        }

        return waitForDestination(destinationArea);
    }

    private static boolean waitForDestination(Area destinationArea) {

        return Time.sleepUntil(() -> destinationArea.contains(Players.getLocal()) && !Game.isLoadingRegion(), () -> Players.getLocal().isAnimating(), 8000);
    }

    public static boolean hasTeleportationItem(List<Integer> itemIds) {

        Predicate<Item> predicate = item -> itemIds.contains(item.getId());

        return Inventory.contains(predicate) || Equipment.contains(predicate);
    }

    public static boolean useTablet(int itemId, Area destination) {

        Item tablet = Inventory.getFirst(itemId);

        if (tablet == null) {
            return false;
        }

        return tablet.interact("Break") && waitForDestination(destination);
    }

}
