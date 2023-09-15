package net.rlbot.script.api.loadout;

import net.rlbot.api.adapter.component.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;


public class EquipmentSet implements Predicate<Item> {

    public List<EquipmentSupply> getSupplies() {
        return equipmentSupplies;
    }

    private final List<EquipmentSupply> equipmentSupplies;

    EquipmentSet(List<EquipmentSupply> equipmentSupplies) {
        this.equipmentSupplies = equipmentSupplies;
    }

    public boolean containsItem(int itemId) {
        return containsItem(Collections.singletonList(itemId));
    }

    public boolean containsItem(List<Integer> itemIds) {
        return this.equipmentSupplies.stream()
                .anyMatch(equipmentSupply -> equipmentSupply.getItemIds().equals(itemIds));
    }

    public EquipmentSupply getSupply(Item item) {
        for(var equipmentSupply : this.equipmentSupplies) {

            if(!equipmentSupply.test(item)) {
                continue;
            }

            return equipmentSupply;
        }

        return null;
    }

    @Override
    public boolean test(Item item) {
        for(var supply : this.equipmentSupplies) {

            if(!supply.test(item)) {
                continue;
            }

            return true;
        }

        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<EquipmentSupply> equipmentSupplies = new LinkedList<>();

        public EquipmentSupplyBuilder with(int itemId) {
            return new EquipmentSupplyBuilder(this, Collections.singletonList(itemId));
        }

        public EquipmentSupplyBuilder with(List<Integer> itemIds) {
            return new EquipmentSupplyBuilder(this, itemIds);
        }

        public EquipmentSet build() {
            return new EquipmentSet(equipmentSupplies);
        }

        public static class EquipmentSupplyBuilder {

            private final Builder parent;

            private final List<Integer> itemIds;

            private int minimumAmount = 1;

            private int maximumAmount = 1;

            private boolean minimumDose = true;

            public EquipmentSupplyBuilder(Builder parent, List<Integer> itemIds) {
                this.parent = parent;
                this.itemIds = itemIds;
            }

            public EquipmentSupplyBuilder amount(int minimumAmount, int maximumAmount) {
                this.minimumAmount = minimumAmount;
                this.maximumAmount = maximumAmount;
                return this;
            }

            public EquipmentSupplyBuilder amount(int amount) {
                this.minimumAmount = amount;
                this.maximumAmount = amount;
                return this;
            }

            public EquipmentSupplyBuilder minimumDose(boolean withdrawMinimumDose) {
                this.minimumDose = withdrawMinimumDose;
                return this;
            }

            public EquipmentSupplyBuilder minimumDose() {
                this.minimumDose = true;
                return this;
            }

            public Builder build() {
                this.parent.equipmentSupplies.add(new EquipmentSupply(this.itemIds, this.minimumAmount, this.maximumAmount, this.minimumDose));
                return parent;
            }
        }

    }
}
