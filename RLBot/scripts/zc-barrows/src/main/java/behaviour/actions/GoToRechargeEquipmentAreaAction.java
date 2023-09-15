package behaviour.actions;

import data.Keys;
import equipment.RechargeableEquipment;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class GoToRechargeEquipmentAreaAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to recharge area";
    }

    @Override
    public void execute() {

        var area = getBlackboard().get(Keys.SETTINGS).getRechargeableEquipment().stream()
                .filter(RechargeableEquipment::isInInventoryOrEquipment)
                .filter(RechargeableEquipment::needsRecharging)
                .findFirst()
                .get()
                .getRechargeArea();

        Movement.walkTo(area);
    }
}
