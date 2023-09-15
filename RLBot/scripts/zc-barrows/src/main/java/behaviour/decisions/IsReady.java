package behaviour.decisions;

import data.Keys;
import equipment.RechargeableEquipment;
import net.rlbot.api.game.Health;
import net.rlbot.api.game.Prayers;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class IsReady implements Decision {
    @Override
    public boolean isValid(Blackboard blackboard) {

        if(!blackboard.get(Keys.HAS_LOADOUT).get()) {
            return false;
        }

        if(blackboard.get(Keys.SETTINGS).getRechargeableEquipment().stream().anyMatch(RechargeableEquipment::needsRecharging)) {
            return false;
        }

        return Prayers.getPoints() == Skills.getLevel(Skill.PRAYER) && Health.getPercent() == 100;
    }
}
