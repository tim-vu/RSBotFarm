package behaviour;

import behaviour.actions.*;
import behaviour.decisions.Decisions;
import behaviour.decisions.EscapeAggro;
import behaviour.decisions.HasCorrectEquipment;
import behaviour.decisions.KillMonster;
import behaviour.selectors.*;
import data.Keys;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.RestockingBehaviour;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import net.rlbot.script.api.tree.decisiontree.common.CommonDecisions;

@Slf4j
public class Behaviour {

    public static DecisionTree createBehaviour(Blackboard blackboard, RestockingSettings restockingSettings) {

        var clearMounds = DecisionNode.builder(Decisions.isInMound())
                    .yes(Decisions.isBrotherGone())
                        .yes(new ExitMoundAction())
                        .no(new HasCorrectEquipment(Selectors.moundEquipment(blackboard)))
                            .yes(Decisions.isBrotherPresent())
                                .yes(Decisions.enableSpecialAttack())
                                    .yes(new EnableSpecialAttackAction())
                                    .no(Decisions.moveToStairs())
                                        .yes(new MoveToStairsAction())
                                        .no(new FightBrotherAction())
                                .no(new AwakeBrotherAction())
                            .no(new SetupEquipmentAction(Selectors.moundEquipment(blackboard)))
                    .no(new EnterMoundAction(new RemainingBrotherSelector(blackboard)))
                .build();

        var enterTunnels = DecisionNode.builder(Decisions.isInMound())
                    .yes(new EnterMoundAction(Selectors.brotherInTunnels(blackboard)))
                    .no(Decisions.isInWrongMound())
                        .yes(new ExitMoundAction())
                        .no(new EnterTunnelsAction())
                .build();

        var clearMoundsAndEnterTunnels = DecisionNode.builder(Decisions.isMoundRemaining())
                    .yes(clearMounds)
                    .no(enterTunnels)
                .build();

        var fightBrotherInTunnels = DecisionNode.builder(new HasCorrectEquipment(Selectors.presentBrotherEquipmentSet(blackboard)))
                    .yes(Decisions.enableSpecialAttack())
                        .yes(new EnableSpecialAttackAction())
                        .no(new FightBrotherAction())
                    .no(new SetupEquipmentAction(Selectors.presentBrotherEquipmentSet(blackboard)))
                .build();

        var moveTowardsChestRoom = DecisionNode.builder(new KillMonster())
                    .yes(new HasCorrectEquipment(Selectors.monsterEquipmentSet(blackboard)))
                        .yes(Decisions.enableSpecialAttack())
                            .yes(new EnableSpecialAttackAction())
                            .no(new KillMonsterAction())
                        .no(new SetupEquipmentAction(Selectors.monsterEquipmentSet(blackboard)))
                    .no(Decisions.isInChestRoom())
                        .yes(new GoToChestRoomAction())
                        .no(new ClaimRewardsAction())
                .build();

        var finishUp = DecisionNode.builder(Decisions.pickupLoot())
                    .yes(new PickupLootAction())
                    .no(new EscapeAggro())
                        .yes(new EscapeAggroAction())
                        .no(new RestartAction())
                .build();

        var clearTunnels = DecisionNode.builder(Decisions.isBrotherPresent())
                    .yes(fightBrotherInTunnels)
                    .no(Decisions.isPuzzleOpen())
                        .yes(new SolvePuzzleAction())
                        .no(Decisions.hasSearchedChest())
                            .yes(finishUp)
                            .no(moveTowardsChestRoom)
                .build();

        var barrows = DecisionNode.builder(Decisions.togglePlayers())
                    .yes(new TogglePrayersAction())
                    .no(Decisions.eatFood())
                        .yes(new EatFoodAction())
                        .no(Decisions.usePrayerPotion())
                            .yes(new UsePrayerPotionAction())
                            .no(Decisions.enableAutocast())
                                .yes(new EnableAutocastAction())
                                .no(Decisions.isInTunnels())
                                    .yes(clearTunnels)
                                    .no(clearMoundsAndEnterTunnels)
                .build();

        var rechargeEquipment = DecisionNode.builder(Decisions.hasRechargeLoadout())
                    .yes(Decisions.isAtRechargeArea())
                        .yes(new RechargeEquipmentAction())
                        .no(new GoToRechargeEquipmentAreaAction())
                    .no(new SetupLoadoutAction(Selectors.rechargeLoadout(blackboard), x -> blackboard.put(Keys.HAS_RECHARGE_LOADOUT, x)))
                .build();

        var prepareAndStartRun = DecisionNode.builder(Decisions.hasLoadout())
                    .yes(Decisions.hasFullStats())
                        .yes(new TeleportToBarrowsAction())
                        .no(new UsePoolOfRefreshment())
                    .no(new SetupLoadoutAction(Selectors.loadout(blackboard), x -> blackboard.get(Keys.HAS_LOADOUT).set(x)))
                .build();

        var restock = DecisionNode.builder(CommonDecisions.isAtGrandExchange())
                    .yes(RestockingBehaviour.buildTree(blackboard, restockingSettings))
                    .no(new GoToGrandExchangeAction())
                .build();

        return DecisionTree.builder(blackboard).decide(Decisions.isAtBarrows())
                .yes(barrows)
                .no(Decisions.isRestocking())
                    .yes(restock)
                    .no(Decisions.checkWeaponCharges())
                        .yes(new CheckWeaponChargesAction())
                        .no(Decisions.rechargeEquipment())
                            .yes(rechargeEquipment)
                            .no(prepareAndStartRun)
                .build();

    }
}
