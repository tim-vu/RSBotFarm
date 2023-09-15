package net.rlbot.script.api.task.common.basicactivitytask.behaviour;

import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.RestockingBehaviour;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.action.*;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.decision.Decisions;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DTNode;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;

public class Behaviour {

    public static DecisionTree buildTree(Blackboard blackboard, RestockingSettings restockingSettings, DTNode trainingNode) {

        return DecisionTree.builder(blackboard)
                .decide(Decisions.isRestocking())
                    .yes(Decisions.isAtGrandExchange())
                        .yes(RestockingBehaviour.buildTree(blackboard, restockingSettings))
                        .no(new GoToGrandExchange())
                    .no(Decisions.hasLoadout())
                        .yes(Decisions.equipItems())
                            .yes(new EquipItems())
                            .no(Decisions.isAtTrainingArea())
                                .yes(trainingNode)
                                .no(new GoToTrainingArea())
                        .no(Decisions.isAtBank())
                            .yes(new SetupLoadoutAction())
                            .no(new GoToBankAction())
                .build();

    }

}
