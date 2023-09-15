package net.rlbot.script.api.restocking.decisiontree;

import lombok.NonNull;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.decisiontree.actions.*;
import net.rlbot.script.api.restocking.decisiontree.decisions.Decisions;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionNode;
import net.rlbot.script.api.tree.decisiontree.DTNode;

public class RestockingBehaviour {

    public static DTNode buildTree(@NonNull Blackboard blackboard, @NonNull RestockingSettings restockingSettings) {

        blackboard.put(RestockingKeys.IS_RESTOCKING, false);
        blackboard.put(RestockingKeys.IS_OUT_OF_COINS, false);
        blackboard.put(RestockingKeys.IS_WITHDRAWING, false);
        blackboard.put(RestockingKeys.IS_BUYING, false);
        blackboard.put(RestockingKeys.SETTINGS, restockingSettings);

        return DecisionNode.builder(Decisions.isDone())
                    .yes(new StopAction(), "Closing the Grand Exchange")
                    .no(Decisions.isWithdrawing())
                        .yes(new WithdrawItemAction(), "Withdrawing Items")
                        .no(Decisions.isBuying())
                            .yes(new CreateBuyOfferAction(), "Creating buy offer")
                            .no(Decisions.collectOffers())
                                .yes(new CollectOffersAction(), "Collecting offers")
                                .no(Decisions.abortOffer())
                                    .yes(new AbortOfferAction(), "Aborting offer")
                                    .no(Decisions.areSlotsFree())
                                        .yes(Decisions.isSellingItems())
                                            .yes(Decisions.itemNeedsSelling())
                                                .yes(Decisions.hasItemsInInventory())
                                                    .yes(new CreateSellOfferAction(), "Creating sell offer")
                                                    .no(new WithdrawItemAction(), "Withdrawing items")
                                                .no(new WaitAction(), "Waiting for sell offers to complete")
                                            .no(Decisions.canCreateBuyOffer())
                                                .yes(new CreateBuyOfferAction(), "Creating buy offer")
                                                .no(new WaitAction(), "Waiting for offers to complete")
                                        .no(new WaitAction(), "Waiting for offers to complete")
                .build();
    }
}
