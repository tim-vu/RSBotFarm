package net.rlbot.script.api.quest.nodes.common;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.movement.pathfinder.Walker;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Pickables;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.script.api.Teleportation;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.condition.Condition;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public class CommonNodes {

    public static UnaryNode watchCutScene(BooleanSupplier endCondition) {

        return new UnaryNode("Watching cutscene") {
            @Override
            protected ActionResult doExecute() {

                if(Dialog.canContinue()) {
                    Dialog.continueSpace();
                    Time.sleepTick();
                    Reaction.REGULAR.sleep();
                }

                if(Game.isInCutscene()) {

                    if(!Time.sleepUntil(() -> !Game.isInCutscene() || Dialog.canContinue(), 2000)) {
                        return ActionResult.FAILURE;
                    }

                    Reaction.REGULAR.sleep();
                    return ActionResult.IN_PROGRESS;
                }

                return endCondition.getAsBoolean() ? ActionResult.SUCCESS : ActionResult.IN_PROGRESS;
            }
        };
    }

    public static UnaryNode walkToNpc(String name, Area area) {
        return walkToSceneEntity(() -> Npcs.getNearest(name), name, area);
    }

    public static UnaryNode walkToObject(String name, Area area) {
        return walkToSceneEntity(() -> SceneObjects.getNearest(name), name, area);
    }

    public static UnaryNode walkToSceneEntity(Supplier<SceneEntity> supplier, String name, Area area) {

        return new UnaryNode("Walking to " + name) {
            @Override
            protected ActionResult doExecute() {

                Position destination = null;

                if (area.contains(Players.getLocal())) {
                    var entity = supplier.get();

                    if (entity == null) {
                        log.warn("Failed to find " + name);
                        Time.sleepTick();
                        return ActionResult.FAILURE;
                    }

                    if (Reachable.isReachable(entity.getPosition()) && entity.distance() <= 7) {
                        return ActionResult.SUCCESS;
                    }

                    Movement.walkTo(entity.getPosition());
                    return ActionResult.IN_PROGRESS;
                }

                Movement.walkTo(area);
                return ActionResult.IN_PROGRESS;
            }
        };
    }

    public static UnaryNode walkToNpcLocal(String name, int sufficientDistance) {

        return walkTo(() -> {
            var npc = Npcs.getNearest(name);

            if (npc == null) {
                return null;
            }

            return npc.getPosition();
        }, sufficientDistance);
    }

    public static UnaryNode walkToPickableLocal(int itemId, int sufficientDistance) {
        return walkTo(() -> {

            var pickable = Pickables.getNearest(itemId);

            if(pickable == null) {
                log.warn("Unable to find " + ItemDefinition.getName(itemId));
                return null;
            }

            return pickable.getPosition();
        }, sufficientDistance);
    }

    public static UnaryNode walkToObjectLocal(String name, int sufficientDistance) {
        return walkToSceneEntityLocal(() -> SceneObjects.getNearest(name), name, sufficientDistance);
    }

    public static UnaryNode walkToSceneEntityLocal(Supplier<? extends SceneEntity> supplier, String name, int sufficientDistance) {
        return walkTo(() -> {
            var object = supplier.get();

            if (object == null) {
                log.warn("Unable to find {}", name);
                return null;
            }

            return object.getPosition();
        }, sufficientDistance);
    }

    public static UnaryNode walkTo(Position position, int sufficientDistance) {

        return walkTo(() -> position, sufficientDistance);
    }

    public static UnaryNode walkPath(List<Position> path, int sufficientDistance) {
        return new UnaryNode("Walking") {
            @Override
            protected ActionResult doExecute() {

                var position = path.get(path.size() - 1);
                if(position.distance() <= sufficientDistance && Reachable.isReachable(position)) {
                    return ActionResult.SUCCESS;
                }

                Walker.walkAlong(path);
                return ActionResult.IN_PROGRESS;
            }
        };
    }

    public static UnaryNode walkTo(Position position, String destination) {
        return walkTo(Area.singular(position), destination);
    }

    public static UnaryNode walkTo(Area area) {
        return walkTo(area, null);
    }

    public static UnaryNode walkTo(Area area, String destination) {
        return new UnaryNode(destination == null ? "Walking" : "Walking to " + destination) {
            @Override
            protected ActionResult doExecute() {

                if (area.contains(Players.getLocal())) {
                    return ActionResult.SUCCESS;
                }

                Movement.walkTo(area);
                return ActionResult.IN_PROGRESS;
            }
        };
    }

    public static UnaryNode walkTo(Supplier<Positionable> positionableSupplier, int sufficientDistance) {

        return new UnaryNode("Walking") {
            @Override
            protected ActionResult doExecute() {

                var position = positionableSupplier.get();

                if (position == null) {
                    log.warn("Unable to get position");
                    return ActionResult.FAILURE;
                }

                if (position.distance() <= sufficientDistance && Reachable.isReachable(position.getPosition())) {
                    return ActionResult.SUCCESS;
                }

                Movement.walkTo(position.getPosition());
                return ActionResult.IN_PROGRESS;
            }
        };
    }

    public static UnaryNode waitUntil(BooleanSupplier condition, int timeout) {

        return new AnonUnaryNode.Builder(() -> true)
                .condition(condition)
                .usePrecondition()
                .timeout(timeout)
                .status("Waiting").build();
    }

    public static UnaryNode wait(int duration) {

        return new UnaryNode("Waiting") {
            @Override
            protected ActionResult doExecute() {

                Time.sleep(duration);
                return ActionResult.SUCCESS;
            }
        };
    }

    public static UnaryNode pickupItem(int itemId) {

        return AnonUnaryNode.builder(Action.pickup(itemId))
                .condition(Condition.hasItem(itemId))
                .reset(Condition.isMoving())
                .usePrecondition()
                .status("Picking up " + ItemDefinition.getName(itemId)).build();
    }

    public static UnaryNode equipItems(int... itemIds) {

        return new UnaryNode("Equipping " + Arrays.stream(itemIds).mapToObj(Integer::toString).reduce((i1, i2) -> i1 + ", " + i2)) {

            @Override
            protected ActionResult doExecute() {

                var itemId = Arrays.stream(itemIds)
                        .filter(Inventory::contains)
                        .filter(i -> (ItemDefinition.isStackable(i) || !Equipment.contains(i)))
                        .findFirst();

                if(itemId.isEmpty()) {
                    return ActionResult.SUCCESS;
                }

                var item = Inventory.getFirst(itemId.getAsInt());

                if(!item.interact(Predicates.always()) || !Time.sleepUntil(() -> !Inventory.contains(item.getId()), 1200)) {
                    log.warn("Failed to equip item: {}", item.getName());
                    return ActionResult.FAILURE;
                }

                Reaction.PREDICTABLE.sleep();
                return ActionResult.IN_PROGRESS;
            }
        };
    }

    public static UnaryNode dismissDialogue() {

        return AnonUnaryNode.builder(new DialogAction())
                .condition(() -> !Dialog.isOpen())
                .usePrecondition()
                .status("Dismissing dialogue").build();
    }

    public static UnaryNode unequipItem(int itemId) {
        return AnonUnaryNode.builder(() -> {
                    var item = Equipment.getFirst(itemId);

                    if(item == null) {
                        log.warn("Unable to find item: " + ItemDefinition.getName(itemId));
                        return false;
                    }

                    return item.interact(Predicates.always());
                })
                .condition(() -> !Equipment.contains(itemId))
                .usePrecondition()
                .status("Unequipping " + ItemDefinition.getName(itemId)).build();
    }

    public static UnaryNode useStaircase(boolean up, int destinationPlane) {

        return changeFloorLevel("Staircase", up ? "Climb-up" : "Climb-down", destinationPlane);
    }

    public static UnaryNode useStairs(boolean up, int destinationPlane) {

        return changeFloorLevel("Stairs", up ? "Walk-up" : "Walk-down", destinationPlane);
    }

    public static UnaryNode useLadder(boolean up, int destinationPlane) {

        return changeFloorLevel("Ladder", up ? "Climb-up" : "Climb-down", destinationPlane);
    }

    public static UnaryNode useTrapdoor(boolean up, int destinationPlane) {
        return changeFloorLevel("Trapdoor", up ? "Climb-up" : "Climb-down", destinationPlane);
    }

    public static UnaryNode changeFloorLevel(String objectName, String action, int destinationLevel) {

        return AnonUnaryNode.builder(Action.interactWithObject(objectName, action))
                .condition(() -> {
                    Position currentPosition = Players.getLocal().getPosition();
                    int currentLevel = currentPosition.getY() > 4000 ? -1 : currentPosition.getPlane();

                    return destinationLevel == currentLevel;
                })
                .reset(Condition.isMoving())
                .usePrecondition()
                .status("Using " + objectName)
                .timeout(8000)
                .build();
    }

    public static UnaryNode useTablet(int itemId, Area destinationArea) {

        return new UnaryNode("Teleporting") {
            @Override
            protected ActionResult doExecute() {

                if (destinationArea.contains(Players.getLocal()))
                {
                    return ActionResult.IN_PROGRESS;
                }

                var tablet = Inventory.getFirst(itemId);

                if (tablet == null || !tablet.interact("Break"))
                {
                    return ActionResult.FAILURE;
                }

                if(!Time.sleepUntil(() -> destinationArea.contains(Players.getLocal()), () -> Players.getLocal().isAnimating(), 8000)) {
                    return ActionResult.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return ActionResult.SUCCESS;
            }
        };
    }

    public static UnaryNode closeBook(int groupId, int id) {

        return new UnaryNode("Closing the book") {
            @Override
            protected ActionResult doExecute() {

                var closeButton = Widgets.get(groupId, id);

                if (closeButton == null) {
                    return ActionResult.SUCCESS;
                }

                if(!closeButton.interact(Predicates.always()) || Time.sleepUntil(() -> !Widgets.isVisible(groupId, id), 4000)) {
                    return ActionResult.FAILURE;
                }

                return ActionResult.SUCCESS;
            }
        };
    }

    public static UnaryNode enableAutoCast(Spell spell, Magic.Autocast.Mode mode){
        return AnonUnaryNode.builder(() -> Magic.Autocast.selectSpell(mode, spell))
                .condition(() -> Magic.Autocast.getSelectedSpell() == spell)
                .usePrecondition()
                .status("Enabling autocast").build();
    }

    public static UnaryNode useTeleport(List<Integer> itemIds, String destination, Area destinationArea) {
        return new UnaryNode("Using teleport") {
            @Override
            protected ActionResult doExecute() {

                if(destinationArea.contains(Players.getLocal())) {
                    return ActionResult.SUCCESS;
                }

                if(!Teleportation.useTeleport(itemIds, destination, destinationArea)) {
                    log.warn("Failed to use teleport");
                    Time.sleepTick();
                    return ActionResult.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return ActionResult.SUCCESS;
            }
        };
    }

}
