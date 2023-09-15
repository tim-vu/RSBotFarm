package net.rlbot.script.api.quest.questexecution.behaviour;

import net.rlbot.script.api.quest.questexecution.behaviour.actions.CheckBankAction;
import net.rlbot.script.api.quest.questexecution.behaviour.actions.GoToBankAction;
import net.rlbot.script.api.quest.questexecution.behaviour.actions.GoToGrandExchange;
import net.rlbot.script.api.quest.questexecution.behaviour.actions.PerformQuestStepAction;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.RestockingBehaviour;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import net.rlbot.script.api.tree.decisiontree.common.CommonDecisions;

public class QuestExecutorBehaviour {

    public static DecisionTree buildTree(Blackboard blackboard, RestockingSettings restockingSettings) {

        return DecisionTree.builder(blackboard)
                .decide(Decisions.isReady())
                    .yes(new PerformQuestStepAction())
                    .no(Decisions.isRestocking())
                        .yes(CommonDecisions.isAtGrandExchange())
                            .yes(RestockingBehaviour.buildTree(blackboard, restockingSettings))
                            .no(new GoToGrandExchange())
                        .no(Decisions.isAtBank())
                            .yes(new CheckBankAction())
                            .no(new GoToBankAction())
                .build();
    }
}
