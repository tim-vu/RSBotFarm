package behaviour.actions;

import data.Keys;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class RechargeEquipmentAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Recharging weapon";
    }

    @Override
    public void execute() {

        var equipment = getBlackboard().get(Keys.EQUIPMENT_TO_RECHARGE);

        Action.logPerform("RECHARGE_EQUIPMENT");
        var result = equipment.recharge();
        if(result == ActionResult.FAILURE) {
            Action.logFail("RECHARGE_EQUIPMENT");
            Time.sleepTick();
            return;
        }

        if(result == ActionResult.IN_PROGRESS) {
            return;
        }

        getBlackboard().put(Keys.EQUIPMENT_TO_RECHARGE, null);
        getBlackboard().put(Keys.HAS_RECHARGE_LOADOUT, false);
    }
}
