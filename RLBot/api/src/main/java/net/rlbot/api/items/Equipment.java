package net.rlbot.api.items;

import lombok.NonNull;
import net.rlbot.api.queries.ItemQuery;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.widgets.WidgetInfo;
import net.runelite.api.InventoryID;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Equipment extends Items {

    private static final Map<Integer, WidgetInfo> SLOT_TO_EQUIPMENT_WIDGET_INFO = Map.ofEntries(
            Map.entry(0, WidgetInfo.EQUIPMENT_HELMET),
            Map.entry(1, WidgetInfo.EQUIPMENT_CAPE),
            Map.entry(2, WidgetInfo.EQUIPMENT_AMULET),
            Map.entry(3, WidgetInfo.EQUIPMENT_WEAPON),
            Map.entry(4, WidgetInfo.EQUIPMENT_BODY),
            Map.entry(5, WidgetInfo.EQUIPMENT_SHIELD),
            Map.entry(7, WidgetInfo.EQUIPMENT_LEGS),
            Map.entry(9, WidgetInfo.EQUIPMENT_GLOVES),
            Map.entry(10, WidgetInfo.EQUIPMENT_BOOTS),
            Map.entry(12, WidgetInfo.EQUIPMENT_RING),
            Map.entry(13, WidgetInfo.EQUIPMENT_AMMO)
    );

    private static Equipment instance;

    Equipment(ApiContext apiContext) {

        super(apiContext, InventoryID.EQUIPMENT, i -> {

            var widgetInfo = SLOT_TO_EQUIPMENT_WIDGET_INFO.getOrDefault(i.getSlot(), null);

            if(widgetInfo == null) {
                return false;
            }

            i.setWidgetId(widgetInfo.getPackedId());
            return true;
        });
    }

    private static void init(@NonNull ApiContext apiContext) {

        instance = new Equipment(apiContext);
    }

    public static ItemQuery query() {
        return new ItemQuery(Equipment::getAll);
    }

    public static List<Item> getAll() {
        return instance.all(i -> true);
    }

    public static List<Item> getAll(Predicate<Item> filter) {
        return instance.all(filter);
    }

    public static Item getItem(int slot) {
        return getAll().stream().filter(i -> i.getSlot() == slot).findFirst().orElse(null);
    }

    public static Item getFirst(Predicate<Item> filter) {
        return instance.first(filter);
    }

    public static Item getFirst(int... itemIds) {
        return instance.first(itemIds);
    }

    public static boolean contains(Predicate<Item> filter) {
        return instance.contains_(filter);
    }

    public static boolean contains(int... itemIds) {
        return instance.contains_(itemIds);
    }

    public static boolean contains(Collection<Integer> itemIds) {
        return instance.contains_(itemIds);
    }

    public static int getCount(boolean stacks, Predicate<Item> filter) {
        return instance.count(stacks, filter);
    }

    public static int getCount(Predicate<Item> filter) {
        return instance.count(false, filter);
    }

    public static int getCount(boolean stacks, int... itemIds) {
        return instance.count(stacks, itemIds);
    }

    public static int getCount(boolean stacks) {
        return instance.all(i -> true).stream().mapToInt(i -> stacks ? i.getQuantity() : 1).sum();
    }

    public static int getCount() {
        return instance.all(i -> true).size();
    }

    public static boolean isEmpty() {
        return instance.all(i -> true).size() == 0;
    }

}
