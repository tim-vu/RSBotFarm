package net.rlbot.script.api.restocking.behaviourtree.condition;

import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.behaviourtree.condition.AnonConditionNode;
import net.rlbot.script.api.tree.behaviourtree.condition.ConditionNode;

public class Condition {

    public static ConditionNode sellItems() {
        return new AnonConditionNode(b -> b.get(RestockingKeys.SETTINGS).isSellItems());
    }

    public static ConditionNode sellBeforeBuy() {
        return new AnonConditionNode(b -> b.get(RestockingKeys.SETTINGS).isSellBeforeBuy());
    }

    public static ConditionNode itemsNeedSelling() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream().anyMatch(Tradeable::needsSelling);
        });
    }

    public static ConditionNode areItemsSelling() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream().anyMatch(Tradeable::isSelling);
        });
    }

    public static ConditionNode areSlotsFree() {
        return new AnonConditionNode(b -> GrandExchange.getEmptySlots() > 0);
    }

    public static ConditionNode canCreateSellOffer() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream().anyMatch(t ->
                            !t.needsSelling() &&
                            !t.offerExists() &&
                            t.getInventoryAmount() > 0
            );
        });
    }

    public static ConditionNode abortOffer() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream()
                    .filter(t -> t.getOfferTracker() != null && t.isOfferInProgress())
                    .anyMatch(t -> t.getOfferTracker().shouldAbort());
        });
    }

    public static ConditionNode isOfferCompleted() {
        return new AnonConditionNode(GrandExchange::canCollect);
    }

    public static ConditionNode itemsToSellInBank() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream()
                    .filter(Tradeable::needsSelling)
                    .filter(t -> !t.isSelling())
                    .anyMatch(t -> t.getCurrentAmount() - t.getRestockAmount() != t.getInventoryAmount());
        });
    }

    public static ConditionNode isInventoryFull() {
        return new AnonConditionNode(Inventory::isFull);
    }

    public static ConditionNode canCreateBuyOffer() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream().filter(Tradeable::needsBuying).anyMatch(t -> !t.offerExists());
        });
    }

    public static ConditionNode isDone() {
        return new AnonConditionNode(b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            var sellExtra = b.get(RestockingKeys.SETTINGS).isSellItems();

            if(!sellExtra) {
                return tradeables.stream().noneMatch(t -> t.offerExists() || t.needsBuying());
            }

            return tradeables.stream().noneMatch(t -> t.offerExists() || t.needsSelling() || t.needsBuying());
        });
    }

}
