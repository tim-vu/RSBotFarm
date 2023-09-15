package net.rlbot.oc.airorbs.task.airorbs.behaviour;

import net.rlbot.oc.airorbs.task.airorbs.behaviour.action.*;
import net.rlbot.oc.airorbs.task.airorbs.behaviour.decision.ShouldMule;
import net.rlbot.oc.airorbs.task.airorbs.behaviour.decision.Decisions;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.RestockingBehaviour;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import net.rlbot.script.api.tree.decisiontree.common.CommonDecisions;

public class Behaviour {

    public static DecisionTree buildTree(Blackboard blackboard, RestockingSettings restockingSettings){

        var restocking = DecisionNode.builder(CommonDecisions.isAtGrandExchange())
                    .yes(RestockingBehaviour.buildTree(blackboard, restockingSettings))
                    .no(new GoToGrandExchangeAction())
                .build();

        var banking = DecisionNode.builder(new ShouldMule())
                    .yes(Decisions.hasGold())
                        .yes(Decisions.isNearMule())
                            .yes(new TradeMuleAction())
                            .no(new GoToMuleAction())
                        .no(new WithdrawGoldAction())
                    .no(Decisions.hopToMemberWorld())
                        .yes(Actions.hopToMemberWorld())
                        .no(Decisions.hasLoadout())
                            .yes(Decisions.replenishStats())
                                .yes(Decisions.isAtFeroxEnclave())
                                    .yes(new UsePoolOfRefreshment())
                                    .no(new GoToFeroxEnclave())
                                .no(new StartChargingAction())
                            .no(Decisions.isAtBank())
                                .yes(new GatherSuppliesAction())
                                .no(new GoToBankAction())
                .build();

        var charging = DecisionNode.builder(Decisions.shouldEscape())
                    .yes(new EscapeAction())
                    .no(Decisions.isAtObelisk())
                        .yes(new ChargeAirOrbsAction())
                        .no(new GoToObeliskAction())
                .build();

        //TODO: UpdateSettings
        return DecisionTree.builder(blackboard)
                .decide(Decisions.isRestocking())
                    .yes(restocking)
                    .no(Decisions.isCharging())
                        .yes(charging)
                        .no(banking)
                .build();
    }
}
