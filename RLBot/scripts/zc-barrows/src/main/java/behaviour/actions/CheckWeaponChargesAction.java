package behaviour.actions;

import data.Keys;
import equipment.weapons.RechargeableWeapon;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class CheckWeaponChargesAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Checking weapon charges";
    }

    @Override
    public void execute() {

        var weapon = getBlackboard().get(Keys.SETTINGS).getRechargeableEquipment().stream()
                .filter(e -> e instanceof RechargeableWeapon)
                .map(e -> (RechargeableWeapon) e)
                .filter(w -> w.getRemainingCharges() < -1)
                .findFirst();

        if (weapon.isEmpty()) {
            log.warn("No weapon to check charges when trying to check charges");
            Time.sleepTick();
            return;
        }

        Action.logPerform("CHECK_WEAPON_CHARGES");
        if (!weapon.get().checkCharges()) {
            Action.logFail("CHECK_WEAPON_CHARGES");
            Time.sleepTick();
            return;
        }
    }
}
