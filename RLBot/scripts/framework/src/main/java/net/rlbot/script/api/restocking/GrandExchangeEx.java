package net.rlbot.script.api.restocking;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.*;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.offer.OfferResult;
import net.rlbot.script.api.restocking.offer.OfferTracker;

import java.util.Collection;

@Slf4j
public class GrandExchangeEx {

    public static boolean collectFinishedOffers(Collection<Tradeable> tradeables) {

        if(GrandExchange.getView() != GrandExchange.View.OFFERS) {
            return false;
        }

        int finishedOfferCount = getFinishedOfferCount();
        int currentOfferCount = getCurrentOfferCount();

        if (!GrandExchange.collect(false) || !Time.sleepUntil(() -> getCurrentOfferCount() == currentOfferCount - finishedOfferCount, 5000)) {
            log.info("Before: finished {}, current: {}", finishedOfferCount, currentOfferCount);
            log.info("Now: current: {}", getCurrentOfferCount());
            return false;
        }

        for (var tradeable : tradeables) {

            if(tradeable.offerExists()) {
                continue;
            }

            if (tradeable.needsSelling() || tradeable.needsBuying()) {
                log.info("Not setting OfferTracker to null for " + tradeable + " because it still needs to be bought or sold");
                continue;
            }

            if(tradeable.getOfferTracker() == null) {
                continue;
            }

            log.info("Setting OfferTracker to null for " + tradeable);
            tradeable.setOfferTracker(null);
        }

        return true;
    }

    public static boolean abortOffer(Tradeable tradeable) {

        if (GrandExchangeSetup.isOpen()) {

            if (!GrandExchangeSetup.goBack())
            {
                log.warn("Failed to go back");
                return false;
            }

            Reaction.REGULAR.sleep();
        }

        return GrandExchange.abortOffer(tradeable.getItemId());
    }

    public static ActionResult createSellOffer(Tradeable tradeable) {
        OfferTracker offerTracker;
        if (tradeable.getOfferTracker() == null) {
            offerTracker = new OfferTracker(tradeable.getInitialPriceIncreases(), false);
        } else {
            offerTracker = tradeable.getOfferTracker();
        }

        var quantity = tradeable.getCurrentAmount() - tradeable.getRestockAmount();

        var result = createSellOffer(tradeable.getItemId(), quantity, offerTracker);

        if(result == ActionResult.FAILURE || result == ActionResult.IN_PROGRESS) {
            return result;
        }

        if (tradeable.getOfferTracker() != null) {
            tradeable.getOfferTracker().setCurrentPriceIncreases(offerTracker.getGoalPriceIncreases());
        } else {
            log.info("Offer tracker created");
            tradeable.setOfferTracker(offerTracker);
        }

        return ActionResult.SUCCESS;
    }

    public static OfferResult createBuyOffer(Tradeable tradeable) {
        var quantity = tradeable.getRestockAmount() - tradeable.getCurrentAmount();

        OfferTracker offerTracker;
        if (tradeable.getOfferTracker() == null) {
            offerTracker = new OfferTracker(tradeable.getInitialPriceIncreases(), true);
        } else {
            offerTracker = tradeable.getOfferTracker();
        }

        var offerResult = createBuyOffer(tradeable.getItemId(), quantity, offerTracker);

        if (offerResult != OfferResult.SUCCESS)
        {
            return offerResult;
        }

        if (tradeable.getOfferTracker() != null) {
            tradeable.getOfferTracker().setCurrentPriceIncreases(offerTracker.getGoalPriceIncreases());
        } else {
            log.info("Offer tracker created");
            tradeable.setOfferTracker(offerTracker);
        }

        return OfferResult.SUCCESS;
    }

    private static OfferResult createBuyOffer(int itemId, int quantity, OfferTracker offerTracker) {

        if((GrandExchange.getView() == GrandExchange.View.BUYING && GrandExchangeSetup.isOccupied()) || GrandExchange.getView() == GrandExchange.View.SELLING) {

            log.debug("Going back");
            if (!GrandExchangeSetup.goBack()) {
                log.warn("Failed to go back");
                return OfferResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return OfferResult.IN_PROGRESS;
        }

        if(GrandExchange.getView() != GrandExchange.View.BUYING) {

            log.debug("Creating buy offer");
            if(!GrandExchange.createBuyOffer()) {
                log.warn("Failed to create buy offer");
                return OfferResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return OfferResult.IN_PROGRESS;
        }

        if(GrandExchangeSetup.getItemId() != itemId) {

            log.debug("Setting item: " + ItemDefinition.getName(itemId));
            if(!GrandExchangeSetup.setItem(itemId)) {
                log.warn("Failed to set item");
                return OfferResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return OfferResult.IN_PROGRESS;
        }

        var price = offerTracker.getGoalPrice(GrandExchangeSetup.getGuidePrice());

        if(GrandExchangeSetup.getItemPrice() != price) {

            log.debug("Guide price: " + GrandExchangeSetup.getGuidePrice());
            log.debug("Setting price to: " + price);
            if (!GrandExchangeSetup.setPrice(price)) {
                log.warn("Failed to set the item price");
                return OfferResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return OfferResult.IN_PROGRESS;
        }

        if (GrandExchangeSetup.getItemPrice() * quantity > Bank.getCount(true, ItemId.COINS_995) + Inventory.getCount(true, ItemId.COINS_995)) {
            return OfferResult.NOT_ENOUGH_COINS;
        }

        if(GrandExchangeSetup.getQuantity() != quantity) {

            log.debug("Setting quantity: " + quantity);
            if (!GrandExchangeSetup.setQuantity(quantity)) {
                log.warn("Failed to set the item quantity");
                return OfferResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return OfferResult.IN_PROGRESS;
        }

        log.debug("Confirming offer");
        if (!GrandExchangeSetup.confirm()) {
            return OfferResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return OfferResult.SUCCESS;
    }

    private static ActionResult createSellOffer(int itemId, int quantity, OfferTracker offerTracker) {

        if((GrandExchange.getView() == GrandExchange.View.SELLING && GrandExchangeSetup.isOccupied()) || GrandExchange.getView() == GrandExchange.View.BUYING) {

            log.debug("Going back");
            if (!GrandExchangeSetup.goBack()) {
                log.warn("Failed to go back");
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(GrandExchange.getView() != GrandExchange.View.SELLING) {

            log.debug("Creating sell offer");
            if(!GrandExchange.createSellOffer(itemId)) {
                log.warn("Failed to create buy offer");
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        var price = offerTracker.getGoalPrice(GrandExchangeSetup.getGuidePrice());

        if(GrandExchangeSetup.getItemPrice() != price) {
            log.debug("Guide price: " + GrandExchangeSetup.getGuidePrice());
            log.debug("Setting price to: " + price);

            if (!GrandExchangeSetup.setPrice(price)) {
                log.warn("Failed to set the item price");
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(GrandExchangeSetup.getQuantity() != quantity) {

            log.debug("Setting quantity to: " + quantity);
            if (!GrandExchangeSetup.setQuantity(quantity)) {
                log.warn("Failed to set the item quantity");
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        log.debug("Confirming offer");
        if (!GrandExchangeSetup.confirm()) {
            log.warn("Failed to confirm the offer");
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.SUCCESS;
    }

    static GrandExchangeOffer getOffer(int itemId) {

        return GrandExchange.getOffers(o -> o.getItemId() == itemId).stream().findFirst().orElse(null);
    }

    public static boolean close() {

        if (!GrandExchange.isOpen())
        {
            log.warn("Grand Exchange is not open");
            return true;
        }

        if (!GrandExchange.close() || !Time.sleepUntil(() -> !GrandExchange.isOpen(), 2000))
        {
            log.warn("Failed to close Grand Exchange");
            return false;
        }

        return true;
    }

    public static int getFinishedOfferCount() {

        return GrandExchange.getOffers(o -> o.getState().isCompleted() || o.getState().isCancelled()).size();
    }

    static int getCurrentOfferCount() {

        return GrandExchange.getOffers(o -> o.getState() != GrandExchangeOffer.State.EMPTY).size();
    }

    public static boolean offerExists(int itemId) {

        for (var offer : GrandExchange.getOffers()) {

            if (offer.getItemId() == itemId)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isOfferInProgress(int itemId) {
        return GrandExchange.getOffers().stream()
                .anyMatch(o -> o.getItemId() == itemId && (o.getState() == GrandExchangeOffer.State.BUYING || o.getState() == GrandExchangeOffer.State.SELLING));
    }

}
