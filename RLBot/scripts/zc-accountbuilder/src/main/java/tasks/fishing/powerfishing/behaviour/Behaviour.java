package tasks.fishing.powerfishing.behaviour;

import net.rlbot.script.api.tree.decisiontree.DTNode;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import tasks.fishing.powerfishing.behaviour.actions.DropFishAction;
import tasks.fishing.powerfishing.behaviour.actions.FishAction;
import tasks.fishing.powerfishing.behaviour.actions.WaitAction;
import tasks.fishing.powerfishing.behaviour.decisions.Decisions;

public class Behaviour {

    public static DTNode buildTree() {
        return DecisionNode.builder(Decisions.isDropping())
                    .yes(new DropFishAction())
                    .no(Decisions.isFishing())
                        .yes(new WaitAction())
                        .no(new FishAction())
                .build();
    }
}
