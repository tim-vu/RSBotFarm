package net.rlbot.script.api.restocking.behaviourtree;

import net.rlbot.script.api.restocking.decisiontree.actions.CloseGrandExchange;
import net.rlbot.script.api.restocking.behaviourtree.action.*;
import net.rlbot.script.api.restocking.behaviourtree.condition.Condition;
import net.rlbot.script.api.tree.behaviourtree.BTNode;
import net.rlbot.script.api.tree.behaviourtree.SelectorNode;
import net.rlbot.script.api.tree.behaviourtree.SequenceNode;

public class RestockingBehaviour {

    public static BTNode buildTree() {

        var sellItemAction = SelectorNode.builder("SellItems?")
                    .with(Condition.sellItems().not()).build()
                    .with(Condition.itemsNeedSelling().not()).build()
                    .withSequence("WithdrawItems_CreateSellOffers")
                        .with(new WithdrawItemsAction()).build()
                        .with(new CreateSellOfferAction()).build()
                    .end()
                .end();

        var buyItemAction = SelectorNode.builder("BuyItems?")
                    .with(Condition.itemsNeedSelling()).build()
                    .with(Condition.sellBeforeBuy().and(Condition.areItemsSelling())).build()
                    .with(Condition.areSlotsFree().not()).build()
                    .with(new CreateBuyOfferAction()).build()
                .end();

        var abortOffer = SelectorNode.builder("AbortOffer?")
                    .with(Condition.abortOffer().not()).build()
                    .with(new AbortOfferAction()).build()
                .end();

        var collectOffers = SelectorNode.builder("CollectOffers?")
                    .with(Condition.isOfferCompleted().not()).build()
                    .with(new CollectOffersAction()).build()
                .end();

        return SequenceNode.builder("Restock_Finish")
                        .withSequence("Sell_Buy_Abort_Collect")
                            .repeatUntil(Condition.isDone())
                            .alwaysSucceed()
                            .with(sellItemAction).build()
                            .with(buyItemAction).build()
                            .with(abortOffer).build()
                            .with(collectOffers).build()
                        .end()
                        .with(new CloseGrandExchange()).build()
                    .end();
    }
}
