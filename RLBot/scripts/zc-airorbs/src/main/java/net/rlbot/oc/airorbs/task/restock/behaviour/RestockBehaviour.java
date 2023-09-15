package net.rlbot.oc.airorbs.task.restock.behaviour;

import net.rlbot.oc.airorbs.task.restock.behaviour.action.CheckTradeablesAction;
import net.rlbot.oc.airorbs.task.restock.behaviour.action.Actions;
import net.rlbot.oc.airorbs.task.restock.behaviour.decision.Decisions;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.RestockingBehaviour;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import net.rlbot.script.api.tree.decisiontree.common.CommonDecisions;

public class RestockBehaviour {

    public static DecisionTree buildTree(Blackboard blackboard, RestockingSettings restockingSettings) {

        return DecisionTree.builder(blackboard)
                    .decide(CommonDecisions.isAtGrandExchange())
                        .yes(Decisions.isRestocking())
                            .yes(RestockingBehaviour.buildTree(blackboard, restockingSettings))
                            .no(new CheckTradeablesAction())
                        .no(Actions.goToGrandExchange(), "Going to the grand exchange")
                .build();
    }
}
