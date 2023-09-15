package tasks.combat.common;

import lombok.NonNull;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.items.Inventory;

import java.util.*;

public class EquipmentTable {

    public List<Equipment> getEquipmentList(EquipmentSlot slot) {
        return equipment.getOrDefault(slot, Collections.emptyList());
    }

    private final Map<EquipmentSlot, List<Equipment>> equipment;

    private EquipmentTable(Map<EquipmentSlot, List<Equipment>> equipment) {
        this.equipment = equipment;
    }

    public List<Equipment> getBest() {

        var result = new ArrayList<Equipment>();

        for (var slot : EquipmentSlot.values()) {

            var list = this.equipment.get(slot);

            if(list == null) {
                continue;
            }

            var equipment = getBest(this.equipment.get(slot));

            if (equipment == null) {
                continue;
            }

            result.add(equipment);
        }

        return result;
    }

    private static Equipment getBest(@NonNull List<Equipment> equipmentList) {

        if(equipmentList.isEmpty()) {
            return null;
        }

        for(var i = equipmentList.size() - 1; i >= 0; i--) {

            var equipment = equipmentList.get(i);

            if(!equipment.hasRequirements()) {
                continue;
            }

            if(!equipment.isTradeable()) {

                if(Bank.isCached()) {
                    continue;
                }

                if(!Inventory.contains(equipment.getItemId()) && !Bank.contains(equipment.getItemId())) {
                    continue;
                }
            }

            return equipment;
        }

        return null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<EquipmentSlot, List<Equipment>> equipment;

        public Builder() {
            this.equipment = new HashMap<>();
        }

        public Builder withHeadItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.HEAD, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withAmuletItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.AMULET, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withCapeItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.CAPE, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withBodyItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.BODY, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withLegsItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.LEGS, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withWeaponItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.WEAPON, Arrays.asList(equipmentList));
            return this;
        }


        public Builder withShieldItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.SHIELD, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withGlovesItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.GLOVES, Arrays.asList(equipmentList));
            return this;
        }

        public Builder withBootsItems(Equipment... equipmentList) {
            this.equipment.put(EquipmentSlot.BOOTS, Arrays.asList(equipmentList));
            return this;
        }

        public EquipmentTable build() {
            return new EquipmentTable(this.equipment);
        }
    }

}
