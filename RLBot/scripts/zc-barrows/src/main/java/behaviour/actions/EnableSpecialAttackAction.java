package behaviour.actions;

import net.rlbot.api.common.Time;
import net.rlbot.api.game.Combat;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class EnableSpecialAttackAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Enabling special attack";
    }

    @Override
    public void execute() {

        Action.logPerform("ENABLE_SPECIAL_ATTACK");
        if(!Combat.toggleSpec()) {
            Action.logFail("ENABLE_SPECIAL_ATTACK");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
