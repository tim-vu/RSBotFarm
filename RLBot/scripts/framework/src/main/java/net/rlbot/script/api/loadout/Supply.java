package net.rlbot.script.api.loadout;

import lombok.Getter;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class Supply implements Predicate<Item> {

    public int getCurrentCount() {
        var count = 0;
        for(var itemId : this.itemIds) {
            count += Inventory.getCount(true, itemId);
            count += Equipment.getCount(true, itemId);
        }

        return count;
    }

    public List<Integer> getItemIds() {
        return Collections.unmodifiableList(itemIds);
    }

    private final List<Integer> itemIds;

    @Getter
    private final int minimumAmount;

    @Getter
    private final int maximumAmount;

    @Getter
    private final boolean minimumDose;

    public Supply(List<Integer> itemIds, int minimumAmount, int maximumAmount, boolean minimumDose) {
        this.itemIds = itemIds;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.minimumDose = minimumDose;
    }

    @Override
    public boolean test(Item item) {
        return this.itemIds.contains(item.getId());
    }

    public boolean isStackable() {
        return ItemDefinition.isStackable(this.itemIds.get(0));
    }

    @Override
    public String toString() {
        return String.format("%s, minimumAmount: %d, maximumAmount: %d", ItemDefinition.getName(this.getItemIds().get(0)), this.minimumAmount, this.maximumAmount);
    }

    public static Item getLowestDose(List<Integer> itemIds) {
        for(var i = itemIds.size() - 1; i >= 0; i--) {
            var item = Inventory.getFirst(i);

            if(item == null) {
                continue;
            }

            return item;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supply supply = (Supply) o;
        return minimumAmount == supply.minimumAmount && maximumAmount == supply.maximumAmount && minimumDose == supply.minimumDose && Objects.equals(itemIds, supply.itemIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemIds, minimumAmount, maximumAmount, minimumDose);
    }
}
