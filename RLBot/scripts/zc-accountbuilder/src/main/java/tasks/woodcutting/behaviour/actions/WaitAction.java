package tasks.woodcutting.behaviour.actions;

import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class WaitAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Waiting";
    }

    @Override
    public void execute() {

        if(!Time.sleepUntil(() -> !Players.getLocal().isAnimating(), 2000)) {
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
