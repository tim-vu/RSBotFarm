package net.rlbot.script.api.restocking.decisiontree.decisions;

import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class Decisions {

    public static Decision hasItemsInInventory() {
        return context -> context.get(RestockingKeys.SETTINGS).getTradeables().stream()
                .anyMatch(t ->
                        t.needsSelling() &&
                                Inventory.contains(t.getItemId(), ItemDefinition.getNotedId(t.getItemId())));
    }

    public static Decision isDone() {
        return context -> {
            var tradeables = context.get(RestockingKeys.SETTINGS).getTradeables();
            if (!context.get(RestockingKeys.SETTINGS).isSellItems()) {
                return tradeables.stream().noneMatch(t -> t.offerExists() || t.needsBuying());
            }

            return tradeables.stream().noneMatch(t -> t.offerExists() || t.needsSelling() || t.needsBuying());
        };
    }

    public static Decision canCreateBuyOffer() {
        return b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream().anyMatch(t -> !t.offerExists() && t.needsBuying());
        };
    }

    public static Decision itemNeedsSelling() {
        return b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream().anyMatch(t -> !t.needsSelling() && !t.offerExists());
        };
    }

    public static Decision isSellingItems() {
        return b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            if (!b.get(RestockingKeys.SETTINGS).isSellItems())
            {
                return false;
            }

            if (b.get(RestockingKeys.SETTINGS).isSellBeforeBuy())
            {
                return tradeables.stream().anyMatch(t -> t.needsSelling() || t.isSelling());
            }

            return tradeables.stream().anyMatch(Tradeable::needsSelling);
        };
    }

    public static Decision areSlotsFree() {
        return b -> GrandExchange.getEmptySlots() > 0;
    }

    public static Decision abortOffer() {
        return b -> {
            var tradeables = b.get(RestockingKeys.SETTINGS).getTradeables();
            return tradeables.stream()
                    .filter(t -> t.getOfferTracker() != null && t.offerExists())
                    .anyMatch(t -> t.getOfferTracker().shouldAbort());
        };
    }

    public static Decision collectOffers() {
        return b -> GrandExchangeEx.getFinishedOfferCount() > 0;
    }

    public static Decision isWithdrawing() {
        return b -> b.get(RestockingKeys.IS_WITHDRAWING);
    }

    public static Decision isBuying() {
        return b -> b.get(RestockingKeys.IS_BUYING);
    }
}
