package tasks.prayer.gildedaltar.behaviour;

import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.RestockingBehaviour;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.prayer.gildedaltar.behaviour.actions.*;
import tasks.prayer.gildedaltar.behaviour.decisions.Decisions;

public class Behaviour {

    public static DecisionTree buildTree(Blackboard blackboard, RestockingSettings restockingSettings) {

        var setup = DecisionNode.builder(Decisions.isRestocking())
                    .yes(Decisions.isAtGrandExchange())
                        .yes(RestockingBehaviour.buildTree(blackboard, restockingSettings))
                        .no(Actions.goToGrandExchange())
                    .no(Decisions.hasLoadout())
                        .yes(Actions.teleportToWilderness())
                        .no(new SetupLoadoutAction())
                .build();

        var praying = DecisionNode.builder(Decisions.arePlayersNearby())
                    .yes(Actions.logout())
                    .no(Decisions.hasBones())
                        .yes(Decisions.isAtGildedAltar())
                            .yes(Decisions.hasUnnotedBones())
                                .yes(new PrayAction())
                                .no(new UnnoteBonesAction())
                            .no(Actions.goToGildedAltar())
                        .no(new ExitWildernessAction())
                .build();

        return DecisionTree.builder(blackboard)
                    .decide(Decisions.isInWilderness())
                        .yes(praying)
                        .no(setup)
                .build();
    }

}
