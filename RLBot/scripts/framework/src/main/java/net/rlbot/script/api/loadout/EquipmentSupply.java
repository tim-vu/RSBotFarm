package net.rlbot.script.api.loadout;

import java.util.Collections;
import java.util.List;

public class EquipmentSupply extends Supply {
    EquipmentSupply(List<Integer> itemIds, int minimumAmount, int maximumAmount, boolean minimumDose) {
        super(itemIds, minimumAmount, maximumAmount, minimumDose);
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

        public Builder minimumDose(boolean withdrawMinimumDose) {
            this.minimumDose = withdrawMinimumDose;
            return this;
        }

        public Builder minimumDose() {
            this.minimumDose = true;
            return this;
        }

        public EquipmentSupply build() {
            return new EquipmentSupply(this.itemIds, this.minimumAmount, this.maximumAmount, this.minimumDose);
        }
    }
}
