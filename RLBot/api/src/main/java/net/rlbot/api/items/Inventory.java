package net.rlbot.api.items;

import lombok.NonNull;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.ItemQuery;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.widgets.Tabs;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Tab;
import net.runelite.api.InventoryID;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Inventory extends Items {

    private static Inventory instance;

    private Inventory(ApiContext apiContext) {
        super(apiContext, InventoryID.INVENTORY, item -> {
            item.setWidgetId(WidgetInfo.INVENTORY.getPackedId());
            return true;
        });
    }

    private static void init(@NonNull ApiContext apiContext) {

        instance = new Inventory(apiContext);
    }

    public static final int SLOTS = 28;

    public static ItemQuery query() {
        return new ItemQuery(Inventory::getAll);
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

    public static Item getFirst(Collection<Integer> itemIds) {
        return instance.first(itemIds);
    }

    public static boolean contains(Predicate<Item> filter) {
        return instance.contains_(filter);
    }

    public static boolean contains(int... ids) {
        return instance.contains_(ids);
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

    public static int getCount(int... itemIds) {
        return instance.count(false, itemIds);
    }

    public static int getCount(boolean stacks) {
        return instance.all(i -> true).stream().mapToInt(i -> stacks ? i.getQuantity() : 1).sum();
    }

    public static int getCount() {
        return instance.count(false, Predicates.always());
    }

    private static final int INVENTORY_SLOTS = 28;

    public static boolean isFull() {
        return getFreeSlots() == 0;
    }

    public static boolean isEmpty() {
        return getFreeSlots() == INVENTORY_SLOTS;
    }

    public static int getFreeSlots() {
        return INVENTORY_SLOTS - getAll().size();
    }


    public static ActionResult dropAll(final boolean leftToRight, final int... itemIds) {
        return dropAll(leftToRight, i -> {
            for(var itemId : itemIds) {
                return i.getId() == itemId;
            }

            return false;
        });
    }

    public static ActionResult dropAllExcept(final boolean leftToRight, final int... itemIds) {
        return dropAllExcept(leftToRight, i -> {
            for(var id : itemIds) {
                if(i.getId() == id)
                    return true;
            }
            return false;
        });
    }

    public static ActionResult dropAllExcept(boolean leftToRight, Predicate<Item> filter) {
        return dropAll(leftToRight, i -> !filter.test(i));
    }

    private static final Map<Integer, Long> DropMap = new HashMap<>();
    private static final int DROP_DELAY = 1000;

    public static ActionResult dropAll(final boolean leftToRight, final Predicate<Item> predicate) {

        if (!Tabs.isOpen(Tab.INVENTORY)) {
            Tabs.open(Tab.INVENTORY);
        }

        for (int j = 0; j < 28; j++) {
            int c = leftToRight ? j % 4 : j / 7;
            int r = leftToRight ? j / 4 : j % 7;

            var index = c + r * 4;

            var item = getItem(index);

            int id;

            if (item == null) {
                continue;
            }

            if (!predicate.test(item))
                continue;

            var lastDropped = DropMap.getOrDefault(index, 0L);

            if (System.currentTimeMillis() - lastDropped < DROP_DELAY) {
                continue;
            }

            if (!item.interact("Drop")) {
                return ActionResult.FAILURE;
            }

            DropMap.put(index, System.currentTimeMillis());
            return ActionResult.IN_PROGRESS;
        }

        return ActionResult.SUCCESS;
    }
}
