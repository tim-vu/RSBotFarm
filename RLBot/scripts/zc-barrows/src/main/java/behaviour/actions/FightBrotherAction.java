package behaviour.actions;

import enums.Brother;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class FightBrotherAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Fighting brother";
    }

    @Override
    public void execute() {

        var brother = Brother.getMyBrother();

        if(brother == null) {
            log.warn("No brother found while trying to fight");
            Time.sleepTick();
            return;
        }

        if(!brother.equals(Players.getLocal().getTarget())) {

            Action.logPerform("ATTACK_BROTHER");
            if(!brother.interact("Attack")) {
                Action.logFail("ATTACK_BROTHER");
                Time.sleepTick();
                return;
            }

            if(!Time.sleepUntil(() -> { var me = Players.getLocal(); return !me.isMoving() && me.isAnimating(); }, () -> Players.getLocal().isMoving(), 1800)) {
                Action.logTimeout("ATTACK_BROTHER");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
        }


        if(!Time.sleepUntil(() -> Brother.getMyBrother() == null, 2000)) {
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
