package net.rlbot.api.items;

import net.rlbot.internal.ApiContext;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.adapter.component.Item;
import net.runelite.api.InventoryID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class Items {

    private final ApiContext apiContext;

    private final InventoryID inventoryId;

    private final Function<Item, Boolean> modification;

    public Items(ApiContext apiContext, InventoryID inventoryId, Function<Item, Boolean> modification) {
        this.apiContext = apiContext;
        this.inventoryId = inventoryId;
        this.modification = modification;
    }

    protected List<Item> all(Predicate<Item> filter) {

        var result = new ArrayList<Item>();

        var itemContainer = apiContext.getInventoryManager().getCachedItemContainers().get(inventoryId.getId());

        if(itemContainer == null) {
            return result;
        }

        for(var item : itemContainer) {

            if(!modification.apply(item) || !filter.test(item)) {
                continue;
            }

            result.add(item);
        }

        return result;
    }

    public boolean isCached_() {
        return apiContext.getInventoryManager().getCachedItemContainers().containsKey(inventoryId.getId());
    }

    protected Item first(Predicate<Item> filter) {
        return this.all(filter).stream().findFirst().orElse(null);
    }

    protected Item first(int... itemIds) {
        return this.all(Predicates.ids(itemIds)).stream().findFirst().orElse(null);
    }

    protected Item first(Collection<Integer> itemIds) {
        return this.all(Predicates.ids(itemIds)).stream().findFirst().orElse(null);
    }

    protected boolean contains_(Predicate<Item> filter) {
        return this.first(filter) != null;
    }

    protected boolean contains_(int... ids) {
        return this.first(ids) != null;
    }

    protected boolean contains_(Collection<Integer> itemIds) {
        return this.first(itemIds) != null;
    }

    protected int count(boolean stacks, Predicate<Item> filter) {
        return this.all(filter).stream().mapToInt(i -> stacks ? i.getQuantity() : 1).sum();
    }

    protected int count(boolean stacks, int... itemIds) {
        return this.all(Predicates.ids(itemIds)).stream().mapToInt(i -> stacks ? i.getQuantity() : 1).sum();
    }

    protected int count(boolean stacks, Collection<Integer> itemIds) {
        return this.all(Predicates.ids(itemIds)).stream().mapToInt(i -> stacks ? i.getQuantity() : 1).sum();
    }
}
