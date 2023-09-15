package behaviour.decisions;

import data.Keys;
import enums.Brother;
import enums.CombatStyle;
import equipment.Equipment;
import lombok.AllArgsConstructor;
import net.rlbot.script.api.loadout.EquipmentSet;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Decision;

import java.util.function.Supplier;

@AllArgsConstructor
public class HasCorrectEquipment implements Decision {

    private final Supplier<EquipmentSet> equipmentSetSupplier;

    public boolean isValid(Blackboard blackboard) {
        var equipmentSet = equipmentSetSupplier.get();
        return equipmentSet.getSupplies().stream().noneMatch(Equipment::canEquip);
    }
}
