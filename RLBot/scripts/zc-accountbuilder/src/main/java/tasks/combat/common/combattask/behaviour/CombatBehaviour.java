package tasks.combat.common.combattask.behaviour;

import net.rlbot.script.api.tree.decisiontree.DTNode;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import tasks.combat.common.combattask.behaviour.actions.*;
import tasks.combat.common.combattask.behaviour.decisions.Decisions;

public class CombatBehaviour {

    public static DTNode buildTree() {
        return DecisionNode.builder(Decisions.eatFood())
                    .yes(new EatFoodAction())
                    .no(Decisions.buryBones())
                        .yes(new BuryBonesAction())
                        .no(Decisions.waitForDeath())
                            .yes(new WaitForDeathAction())
                            .no(Decisions.lootItems())
                                .yes(Decisions.dropItems())
                                    .yes(new DropItemsAction())
                                    .no(new LootItemsAction())
                                .no(Decisions.isCombatStyleSetup())
                                    .yes(new FightAction())
                                    .no(new SetupCombatStyleAction())
                .build();
    }

}
