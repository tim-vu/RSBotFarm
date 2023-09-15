package net.rlbot.script.api.quest.nodes.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.Interactable;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Pickables;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public class Action {

    public static BooleanSupplier interactWithNpc(String name, String action) {

        return interactWithNpc(() -> Npcs.getNearest(name), action);
    }

    public static BooleanSupplier interactWithNpc(Supplier<Npc> npcSupplier, String action) {

        return () -> {
            var npc = npcSupplier.get();

            if (npc == null) {
                return false;
            }

            return npc.interact(action) && Time.sleepUntil(() -> npc.distance(Distance.CHEBYSHEV) <= 2, () -> Players.getLocal().isMoving(), 6000);
        };
    }

    public static BooleanSupplier interactWithItem(int itemId, String action) {

        return () -> {
            Item item = Inventory.getFirst(itemId);

            if (item == null) {
                log.warn(String.format("Item with id %d not found", itemId));
                return false;
            }

            //TODO: Rework interact to accept predicates
            return item.interact(action);
        };
    }

    public static BooleanSupplier closeWidget() {

        return () -> {
            Keyboard.sendEscape();
            return true;
        };
    }

    public static BooleanSupplier interactWithObject(String name, String action) {

        return interactWithObject(() -> SceneObjects.getNearest(name), action);
    }

    public static BooleanSupplier interactWithObject(Position position, String name, String action) {

        return interactWithObject(() -> SceneObjects.getAt(position, name).stream().findFirst().orElse(null), action);
    }

    public static BooleanSupplier interactWithObject(Supplier<SceneObject> sceneObjectSupplier, String action) {

        return () -> {

            SceneObject object = sceneObjectSupplier.get();

            if (object == null) {
                log.warn("Failed to find object");
                Time.sleepTick();
                return false;
            }

            return object.interact(action) && Time.sleepUntil(() -> object.distance(Distance.CHEBYSHEV) <= 2, () -> Players.getLocal().isMoving(), 6000);
        };
    }

    public static BooleanSupplier interactWithObject(Supplier<SceneObject> sceneObjectSupplier, int index) {

        return () -> {

            SceneObject object = sceneObjectSupplier.get();

            if (object == null) {
                log.warn("Failed to find object");
                Time.sleepTick();
                return false;
            }

            return object.interact(index) && Time.sleepUntil(() -> object.distance(Distance.CHEBYSHEV) <= 2, () -> Players.getLocal().isMoving(), 6000);
        };
    }

    public static BooleanSupplier useItemOn(int itemId, Supplier<? extends Interactable> targetSupplier) {

        return () -> {
            var item = Inventory.getFirst(itemId);

            if (item == null) {
                log.warn("Failed to find item");
                Time.sleepTick();
                return false;
            }

            var target = targetSupplier.get();

            if (target == null) {
                log.warn("Failed to find target");
                Time.sleepTick();
                return false;
            }

            if(!item.useOn(target)) {
                log.warn("Failed to use item on target");
                Time.sleepTick();
                return false;
            }

            if(target instanceof SceneEntity sceneEntity) {
                return Time.sleepUntil(() -> sceneEntity.distance(Distance.CHEBYSHEV) <= 2, () -> Players.getLocal().isMoving(), 6000);
            }

            return true;
        };
    }
    public static BooleanSupplier pickup(int itemId) {

        return () -> {

            var pickable = Pickables.getNearest(itemId);

            if(pickable == null) {
                log.warn("Unable to find pickable");
                Time.sleepTick();
                return false;
            }

            return pickable.interact("Take");
        };
    }

    public static BooleanSupplier clickInterface(WidgetInfo widgetInfo) {

        return clickInterface(() -> Widgets.get(widgetInfo));
    }

    public static BooleanSupplier clickInterface(WidgetAddress interfaceAddress) {

        return clickInterface(interfaceAddress::resolve);
    }

    public static BooleanSupplier clickInterface(Supplier<Widget> widgetSupplier) {

        return () -> {

            var widget = widgetSupplier.get();

            if (widget == null) {
                log.warn("Failed to find widget");
                Time.sleepTick();
                return false;
            }

            return widget.interact(Predicates.always());
        };
    }

}
