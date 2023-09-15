package data;

import enums.Brother;
import equipment.RechargeableEquipment;
import equipment.weapons.RechargeableWeapon;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.tree.Key;

import java.util.Queue;

public class Keys {

    public static final Key<TempMemory<Boolean>> TOGGLE_PRAYERS = new Key<>();

    public static final Key<TempMemory<Boolean>> HAS_LOADOUT = new Key<>();

    public static final Key<Boolean> HAS_RECHARGE_LOADOUT = new Key<>();

    public static final Key<Settings> SETTINGS = new Key<>();

    public static final Key<Queue<Brother>> REMAINING_BROTHERS = new Key<>();

    public static final Key<Brother> BROTHER_IN_TUNNELS = new Key<>();

    public static final Key<Boolean> SEARCHED_CHEST = new Key<>();

    public static final Key<Integer> TARGET_INDEX = new Key<>();

    public static final Key<Integer> REWARD_POTENTIAL = new Key<>();

    public static final Key<RechargeableEquipment> EQUIPMENT_TO_RECHARGE = new Key<>();


}
