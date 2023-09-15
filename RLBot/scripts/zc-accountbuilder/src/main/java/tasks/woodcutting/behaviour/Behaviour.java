package tasks.woodcutting.behaviour;

import net.rlbot.script.api.tree.decisiontree.DTNode;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import tasks.woodcutting.behaviour.actions.ChopTreeAction;
import tasks.woodcutting.behaviour.actions.DropLogsAction;
import tasks.woodcutting.behaviour.actions.WaitAction;
import tasks.woodcutting.behaviour.decisions.Decisions;

public class Behaviour {

    public static DTNode buildTree() {
        return DecisionNode.builder(Decisions.isDropping())
                    .yes(new DropLogsAction())
                    .no(Decisions.isChopping())
                        .yes(new WaitAction())
                        .no(new ChopTreeAction())
                .build();
    }
}
