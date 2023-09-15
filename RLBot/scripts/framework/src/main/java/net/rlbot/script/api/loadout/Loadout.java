package net.rlbot.script.api.loadout;

import net.rlbot.api.adapter.component.Item;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Loadout implements Predicate<Item> {

    public List<InventorySupply> getInventorySupplies() {
        return Collections.unmodifiableList(inventorySupplies);
    }

    private final List<InventorySupply> inventorySupplies;

    public List<EquipmentSet> getEquipmentSets() {
        return Collections.unmodifiableList(equipmentSets);
    }

    private final List<EquipmentSet> equipmentSets;

    public Loadout(List<InventorySupply> inventorySupplies) {
        this.inventorySupplies = inventorySupplies;
        this.equipmentSets = Collections.emptyList();
    }

    public Loadout(List<InventorySupply> inventorySupplies, List<EquipmentSet> equipmentSets) {
        this.inventorySupplies = inventorySupplies;
        this.equipmentSets = equipmentSets;
    }

    public static Loadout empty() {
        return new Loadout(Collections.emptyList(), Collections.emptyList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public Set<Integer> getAllItemIds() {
        return Stream.concat(
                getEquipmentSets().stream().flatMap(s -> s.getSupplies().stream()),
                getInventorySupplies().stream()
        ).flatMap(s -> s.getItemIds().stream()).collect(Collectors.toSet());
    }

    public Supply getSupply(Item item) {

        for(var supply : inventorySupplies) {

            if(!supply.test(item)) {
                continue;
            }

            return supply;
        }

        for(var set : equipmentSets) {

            if(!set.test(item)) {
                continue;
            }

            return set.getSupply(item);
        }

        return null;
    }

    @Override
    public boolean test(Item item) {
        for(var supply : inventorySupplies) {

            if(!supply.test(item)) {
                continue;
            }

            return true;
        }

        for(var set : equipmentSets) {

            if(!set.test(item)) {
                continue;
            }

            return true;
        }

        return false;
    }

    public static class Builder {

        public List<InventorySupply> getInventorySupplies() {
            return Collections.unmodifiableList(this.inventorySupplies);
        }

        private final List<InventorySupply> inventorySupplies = new LinkedList<>();

        public List<EquipmentSet> getEquipmentSets() {
            return Collections.unmodifiableList(this.equipmentSets);
        }

        private final List<EquipmentSet> equipmentSets = new LinkedList<>();

        public Builder with(InventorySupply inventorySupply) {
            this.inventorySupplies.add(inventorySupply);
            return this;
        }

        public Builder with(EquipmentSet equipmentSet) {
            this.equipmentSets.add(equipmentSet);
            return this;
        }

        public InventorySupplyBuilder withItem(int itemId) {
            return new InventorySupplyBuilder(this, Collections.singletonList(itemId));
        }

        public InventorySupplyBuilder withItem(List<Integer> itemIds) {
            return new InventorySupplyBuilder(this, itemIds);
        }

        public EquipmentSetBuilder withEquipmentSet() {
            return new EquipmentSetBuilder(this);
        }

        public Builder fromLoadout(Loadout loadout) {
            this.inventorySupplies.addAll(loadout.getInventorySupplies());
            this.equipmentSets.addAll(loadout.getEquipmentSets());
            return this;
        }

        public Loadout build() {
            return new Loadout(inventorySupplies, equipmentSets);
        }

        public static class InventorySupplyBuilder {

            private final Builder parent;

            private final List<Integer> itemIds;

            private int minimumAmount = 1;

            private int maximumAmount = 1;

            private boolean noted = false;

            private boolean minimumDose = true;

            public InventorySupplyBuilder(Builder parent, List<Integer> itemIds) {
                this.parent = parent;
                this.itemIds = itemIds;
            }

            public InventorySupplyBuilder amount(int minimumAmount, int maximumAmount) {
                this.minimumAmount = minimumAmount;
                this.maximumAmount = maximumAmount;
                return this;
            }

            public InventorySupplyBuilder amount(int amount) {
                this.minimumAmount = amount;
                this.maximumAmount = amount;
                return this;
            }

            public InventorySupplyBuilder noted() {
                this.noted = true;
                return this;
            }

            public InventorySupplyBuilder minimumDose(boolean withdrawMinimumDose) {
                this.minimumDose = withdrawMinimumDose;
                return this;
            }

            public InventorySupplyBuilder minimumDose() {
                this.minimumDose = true;
                return this;
            }

            public Builder build() {
                this.parent.inventorySupplies.add(new InventorySupply(this.itemIds, this.minimumAmount, this.maximumAmount, this.noted, this.minimumDose));
                return parent;
            }
        }

        public static class EquipmentSetBuilder {

            private final Builder parent;

            private final List<EquipmentSupply> equipmentSupplies = new LinkedList<>();

            public EquipmentSetBuilder(Builder parent) {
                this.parent = parent;
            }

            public EquipmentSupplyBuilder with(int itemId) {
                return new EquipmentSupplyBuilder(this, Collections.singletonList(itemId));
            }

            public EquipmentSupplyBuilder with(List<Integer> itemIds) {
                return new EquipmentSupplyBuilder(this, itemIds);
            }

            public Builder build() {
                this.parent.equipmentSets.add(new EquipmentSet(this.equipmentSupplies));
                return parent;
            }

            public static class EquipmentSupplyBuilder {

                private final EquipmentSetBuilder parent;

                private final List<Integer> itemIds;

                private int minimumAmount = 1;

                private int maximumAmount = 1;

                private boolean minimumDose = true;

                public EquipmentSupplyBuilder(EquipmentSetBuilder parent, List<Integer> itemIds) {
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

                public EquipmentSetBuilder build() {
                    this.parent.equipmentSupplies.add(new EquipmentSupply(this.itemIds, this.minimumAmount, this.maximumAmount, this.minimumDose));
                    return parent;
                }
            }
        }
    }
}
