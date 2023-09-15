package equipment;

import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.loadout.EquipmentSupply;

public class Equipment {

    public static boolean canEquip(EquipmentSupply equipmentSupply) {

        var inInventory = Inventory.contains(equipmentSupply);

        if(!inInventory) {
            return false;
        }

        var stackable = equipmentSupply.isStackable();

        return stackable || !net.rlbot.api.items.Equipment.contains(equipmentSupply);
    }
}
