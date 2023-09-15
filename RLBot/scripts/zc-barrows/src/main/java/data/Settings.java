package data;

import enums.Brother;
import enums.SpecialAttackWeapon;
import equipment.RechargeableEquipment;
import lombok.Builder;
import lombok.Value;
import net.rlbot.script.api.loadout.EquipmentSet;
import net.rlbot.script.api.loadout.Loadout;

import java.util.List;
import java.util.Set;

@Builder
@Value
public class Settings {
    Set<Brother> brotherToPray;

    int minimumHealthPercent;

    List<Integer> foodItemIds;

    int minimumPrayerPoints;

    Loadout loadout;

    EquipmentSet magicEquipmentSet;

    EquipmentSet rangedEquipmentSet;

    EquipmentSet monsterEquipmentSet;

    Set<SpecialAttackWeapon> specialAttackWeapons;

    Set<RechargeableEquipment> rechargeableEquipment;

}
