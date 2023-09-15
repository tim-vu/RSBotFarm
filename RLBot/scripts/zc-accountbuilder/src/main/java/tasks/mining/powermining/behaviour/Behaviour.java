package tasks.mining.powermining.behaviour;

import net.rlbot.script.api.tree.decisiontree.DTNode;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import tasks.mining.powermining.behaviour.actions.DropOreAction;
import tasks.mining.powermining.behaviour.actions.MineRockAction;
import tasks.mining.powermining.behaviour.actions.WaitAction;
import tasks.mining.powermining.behaviour.decisions.Decisions;

public class Behaviour {

    public static DTNode buildTree() {
        return DecisionNode.builder(Decisions.isDropping())
                    .yes(new DropOreAction())
                    .no(Decisions.isMining())
                        .yes(new WaitAction())
                        .no(new MineRockAction())
                .build();
    }
}
