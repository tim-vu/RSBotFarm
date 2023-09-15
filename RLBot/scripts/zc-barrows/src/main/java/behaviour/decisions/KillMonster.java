package behaviour.decisions;

import data.Keys;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.scene.Npcs;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tunnels.Tunnels;

@Slf4j
public class KillMonster implements Decision {
    @Override
    public boolean isValid(Blackboard blackboard) {

        var targetIndex = blackboard.get(Keys.TARGET_INDEX);
        var rewardPotential = blackboard.get(Keys.REWARD_POTENTIAL);
        var target = targetIndex == -1 ? null : Npcs.getNearest(targetIndex);

        if(target != null && Tunnels.isTargetValid(target, rewardPotential)) {
            return true;
        }

        blackboard.put(Keys.TARGET_INDEX, -1);

        target = Tunnels.getValidTarget(rewardPotential);

        if(target != null) {
            log.info("New target found");
            blackboard.put(Keys.TARGET_INDEX, target.getIndex());
            return true;
        }

        return false;
    }
}
