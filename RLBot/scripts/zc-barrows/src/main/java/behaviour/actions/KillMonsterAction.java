package behaviour.actions;

import data.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class KillMonsterAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Killing monster";
    }

    @Override
    public void execute() {

        var target = Npcs.getNearest(getBlackboard().get(Keys.TARGET_INDEX));

        if(target == null) {
            log.warn("Target null when trying to kill monster");
            Time.sleepTick();
            return;
        }

        if(!target.equals(Players.getLocal().getTarget())) {

            Action.logPerform("ATTACK_TARGET");
            if(!target.interact("Attack")) {
                Action.logFail("ATTACK_TARGET");
                Time.sleepTick();
                return;
            }

            if(!Time.sleepUntil(() -> { var me = Players.getLocal(); return !me.isMoving() && me.isMoving(); }, () -> Players.getLocal().isMoving(), 2400)) {
                Action.logTimeout("ATTACK_TARGET");
                Time.sleepTick();
                return;
            }

        }

        Time.sleepUntil(() -> Npcs.getNearest(n -> n.getIndex() == target.getIndex()) == null, 2000);
    }
}
