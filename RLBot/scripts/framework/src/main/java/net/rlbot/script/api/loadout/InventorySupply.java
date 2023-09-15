package net.rlbot.script.api.loadout;

import lombok.Getter;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;

import java.util.Collections;
import java.util.List;

public class InventorySupply extends Supply {

    @Override
    public int getCurrentCount() {

        var count = 0;

        for(var itemId : this.actualItemIds) {
            count += Inventory.getCount(true, itemId);
            count += Equipment.getCount(true, itemId);
        }

        return count;
    }

    @Override
    public boolean test(Item item) {
        return this.actualItemIds.contains(item.getId());
    }

    @Getter
    private final boolean noted;

    private final List<Integer> actualItemIds;

    public InventorySupply(List<Integer> itemIds, int minimumAmount, int maximumAmount, boolean noted, boolean minimumDose) {
        super(itemIds, minimumAmount, maximumAmount, minimumDose);
        this.noted = noted;
        this.actualItemIds = itemIds.stream().map(i -> noted ? ItemDefinition.getNotedId(i) : i).toList();
    }

    @Override
    public boolean isStackable() {
       return this.noted || super.isStackable();
    }

    public static Builder builder(int itemId) {
        return new Builder(Collections.singletonList(itemId));
    }

    public static Builder builder(List<Integer> itemIds) {
        return new Builder(itemIds);
    }

    public static class Builder {

        private final List<Integer> itemIds;

        private int minimumAmount = 1;

        private int maximumAmount = 1;

        private boolean noted = false;

        private boolean minimumDose = true;

        public Builder(List<Integer> itemIds) {
            this.itemIds = itemIds;
        }

        public Builder amount(int minimumAmount, int maximumAmount) {
            this.minimumAmount = minimumAmount;
            this.maximumAmount = maximumAmount;
            return this;
        }

        public Builder amount(int amount) {
            this.minimumAmount = amount;
            this.maximumAmount = amount;
            return this;
        }

        public Builder noted() {
            this.noted = true;
            return this;
        }

        public Builder minimumDose(boolean withdrawMinimumDose) {
            this.minimumDose = withdrawMinimumDose;
            return this;
        }

        public Builder minimumDose() {
            this.minimumDose = true;
            return this;
        }

        public InventorySupply build() {
            return new InventorySupply(itemIds, minimumAmount, maximumAmount, noted, minimumDose);
        }
    }
}
