package net.rlbot.script.api.quest.nodes.condition;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.game.Vars;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.QuestState;
import net.rlbot.api.quest.Quests;
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
public class Condition {

    public static BooleanSupplier isInArea(Area area) {

        return () -> area.contains(Players.getLocal());
    }

    public static BooleanSupplier isNotInArea(Area area) {

        return () -> !area.contains(Players.getLocal());
    }

    public static BooleanSupplier isAtStage(Quest quest, int value) {

        return () -> Quests.getStage(quest) == value;
    }

    public static BooleanSupplier isQuestStarted(Quest quest) {

        return () -> Quests.getState(quest) == QuestState.IN_PROGRESS;
    }

    public static BooleanSupplier isQuestDone(Quest quest) {

        return () -> Quests.getState(quest) == QuestState.FINISHED;
    }

    public static BooleanSupplier hasItem(int itemId, int amount) {

        return () -> Inventory.getCount(true, itemId) >= amount;
    }

    public static BooleanSupplier hasItem(int itemId) {

        return hasItem(itemId, 1);
    }

    public static BooleanSupplier doesntHaveItem(int itemId) {
        return () -> !Inventory.contains(itemId);
    }

    public static BooleanSupplier isWearing(int itemId) {

        return () -> Equipment.contains(itemId);
    }

    public static BooleanSupplier isNpcPresent(String name) {

        return isNpcPresent(() -> Npcs.getNearest(name));
    }

    public static BooleanSupplier isNpcPresent(Supplier<Npc> npcSupplier) {

        return () -> npcSupplier.get() != null;
    }

    public static BooleanSupplier objectHasFirstAction(String name, String action) {

        return objectHasFirstAction(() -> SceneObjects.getNearest(name), name, action);
    }

    public static BooleanSupplier objectHasFirstAction(Supplier<SceneObject> objectSupplier, String name, String action) {

        return () -> {

            var object = objectSupplier.get();

            if (object == null) {
                return false;
            }

            var actions = object.getActions();

            return actions.length > 0 && actions[0].equals(action);
        };
    }

    public static BooleanSupplier isNpcDead(String name) {

        return isNpcDead(() -> Npcs.getNearest(name));
    }

    public static BooleanSupplier isNpcDead(Supplier<Npc> npcSupplier) {

        return () -> {
            Npc npc = npcSupplier.get();

            return npc == null || npc.getHealthPercent() == 0;
        };
    }

    public static BooleanSupplier pickableExists(int itemId, int amount) {

        return () -> {
            var pickable = Pickables.getNearest(itemId);

            return pickable != null && pickable.getQuantity() >= amount;
        };
    }

    public static BooleanSupplier pickableExists(int itemId) {

        return pickableExists(itemId, 1);
    }

    public static BooleanSupplier isUnderground() {

        return () -> Players.getLocal().getPosition().getY() > 4000;
    }

    public static BooleanSupplier isAboveGround() {

        return () -> Players.getLocal().getPosition().getY() < 4000;
    }


    public static BooleanSupplier objectExists(String objectName) {

        return () -> SceneObjects.getNearest(objectName) != null;
    }

    public static BooleanSupplier not(BooleanSupplier condition) {

        return () -> !condition.getAsBoolean();
    }

    public static BooleanSupplier isWidgetVisible(WidgetInfo widgetInfo) {

        return () -> Widgets.isVisible(widgetInfo);
    }

    public static BooleanSupplier isWidgetVisible(WidgetAddress widgetAddress) {

        return widgetAddress::isWidgetVisible;
    }

    public static BooleanSupplier isWidgetHidden(WidgetAddress widgetAddress) {
        return () -> !widgetAddress.isWidgetVisible();
    }

    public static BooleanSupplier isMoving() {
        return () -> Players.getLocal().isMoving();
    }

    public static BooleanSupplier isAnimating() {
        return () -> Players.getLocal().isMoving();
    }

    public static BooleanSupplier varbitHasValue(int varbit, int value) {
        return () -> Vars.getBit(varbit) == value;
    }

    public static BooleanSupplier isReachable(Position position) {
        return () -> Reachable.isReachable(position);
    }
}
