package tasks.agility.behaviour;

import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.agility.behaviour.action.EatFoodAction;
import tasks.agility.behaviour.action.GoToStartAction;
import tasks.agility.behaviour.action.PassObstacleAction;
import tasks.agility.behaviour.action.PickMarkOfGraceAction;
import tasks.agility.behaviour.decision.Decisions;

public class Behaviour {

    public static DecisionTree buildTree(Blackboard blackboard) {
        return DecisionTree.builder(blackboard)
                    .decide(Decisions.shouldEat())
                        .yes(new EatFoodAction())
                        .no(Decisions.isOnCourse())
                            .yes(Decisions.isMarkOfGraceAvailable())
                                .yes(new PickMarkOfGraceAction())
                                .no(new PassObstacleAction())
                            .no(new GoToStartAction())
                .build();
    }
}
