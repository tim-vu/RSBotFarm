package net.rlbot.script.api.restocking;

import lombok.Getter;
import lombok.Setter;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.GrandExchangeOffer;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.restocking.offer.OfferTracker;

public class Tradeable {

    private final int itemId;

    private final int restockAmount;

    @Getter
    private final int initialPriceIncreases;

    @Getter
    @Setter
    private OfferTracker offerTracker;

    public Tradeable(int itemId) {

        this(itemId, 0);
    }

    public Tradeable(int itemId, int restockAmount) {

        this(itemId, restockAmount, 1);
    }

    public Tradeable(int itemId, int restockAmount, int initialPriceIncreases) {

        this.itemId = itemId;
        this.restockAmount = restockAmount;
        this.initialPriceIncreases = initialPriceIncreases;
    }

    public int getItemId() {

        return itemId;
    }

    public int getRestockAmount() {

        return restockAmount;
    }

    public boolean needsBuying() {

        return getCurrentAmount() < this.restockAmount;
    }

    public boolean needsSelling() {
        return getCurrentAmount() > this.restockAmount;
    }

    public boolean isSelling() {

        var offer = GrandExchangeEx.getOffer(this.itemId);
        return offer != null && offer.getState() == GrandExchangeOffer.State.SELLING;
    }

    public boolean isBuying() {

        var offer = GrandExchangeEx.getOffer(this.itemId);
        return offer != null && offer.getState() == GrandExchangeOffer.State.BUYING;
    }

    public boolean offerExists() {
        return GrandExchangeEx.offerExists(this.itemId);
    }

    public boolean isOfferInProgress() {
        return GrandExchangeEx.isOfferInProgress(this.itemId);
    }

    public int getCurrentAmount() {
        var count = Bank.getCount(true, this.itemId);
        count += Inventory.getCount(true, this.itemId);
        count += Inventory.getCount(true, ItemDefinition.getNotedId(this.itemId));

        return count;
    }

    public int getInventoryAmount() {
        var count = 0;
        count += Inventory.getCount(true, itemId);
        count += Inventory.getCount(true, ItemDefinition.getNotedId(itemId));
        return count;
    }

    @Override
    public int hashCode() {

        return Integer.hashCode(this.itemId);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (!(obj instanceof Tradeable))
            return false;

        return this.itemId == ((Tradeable) obj).getItemId();
    }

    @Override
    public String toString() {

        return ItemDefinition.getName(getItemId()) + ", amount: " + getRestockAmount();
    }

}
