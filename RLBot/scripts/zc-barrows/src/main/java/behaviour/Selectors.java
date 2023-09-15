package behaviour;

import data.Keys;
import enums.Brother;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.loadout.EquipmentSet;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.tree.Blackboard;

import java.util.function.Supplier;

@Slf4j
public class Selectors {

    public static Supplier<EquipmentSet> presentBrotherEquipmentSet(Blackboard blackboard) {
        return () -> {
            var brother = Brother.getMyBrotherType();

            if(brother == null) {
                log.warn("Brother null when trying to determine equipmentset for brother");
                return null;
            }

            return switch(brother.getCombatStyle()) {

                case MAGIC -> blackboard.get(Keys.SETTINGS).getMagicEquipmentSet();
                case RANGED -> blackboard.get(Keys.SETTINGS).getRangedEquipmentSet();
            };
        };
    }

    public static Supplier<Brother> brotherInTunnels(Blackboard blackboard) {
        return () -> blackboard.get(Keys.BROTHER_IN_TUNNELS);
    }

    public static Supplier<EquipmentSet> monsterEquipmentSet(Blackboard blackboard) {
        return () -> blackboard.get(Keys.SETTINGS).getMonsterEquipmentSet();
    }

    public static Supplier<EquipmentSet> moundEquipment(Blackboard blackboard) {
        return () -> {
            var brother = Brother.getBrotherByMound();

            if(brother == null) {
                log.warn("Brother null when trying to determine equipmentset for mound");
                return null;
            }

            return switch(brother.getCombatStyle()) {

                case MAGIC -> blackboard.get(Keys.SETTINGS).getMagicEquipmentSet();
                case RANGED -> blackboard.get(Keys.SETTINGS).getRangedEquipmentSet();
            };
        };
    }

    public static Supplier<Brother> remainingBrother(Blackboard blackboard) {
        return () -> blackboard.get(Keys.REMAINING_BROTHERS).peek();
    };

    public static Supplier<Loadout> rechargeLoadout(Blackboard blackboard) {
        return () -> blackboard.get(Keys.EQUIPMENT_TO_RECHARGE).getRechargeLoadout();
    }

    public static Supplier<Boolean> hasRechargeLoadout(Blackboard blackboard) {
        return () -> blackboard.get(Keys.HAS_RECHARGE_LOADOUT);
    }

    public static Supplier<Loadout> loadout(Blackboard blackboard) {
        return () -> blackboard.get(Keys.SETTINGS).getLoadout();
    }

    public static Supplier<TempMemory<Boolean>> hasLoadout(Blackboard blackboard) {
        return () -> blackboard.get(Keys.HAS_LOADOUT);
    }

}
