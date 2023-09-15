package net.rlbot.api.items;

import lombok.NonNull;
import net.rlbot.api.Game;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.WidgetID;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.internal.ApiContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GrandExchange {

    private static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {

        API_CONTEXT = apiContext;
    }

    private static final int F2P_SLOTS = 3;

    private static final int P2P_SLOTS = 8;

    private static final WidgetAddress COLLECT_BUTTON = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 6, 0);

    private static final WidgetAddress CONFIRM_BUTTON = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 29);

    private static final WidgetAddress OFFER_PRICE = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 27);

    private static final WidgetAddress CLOSE_BUTTON = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 2, 11);

    private static final WidgetAddress GRAND_EXCHANGE_INVENTORY = new WidgetAddress(WidgetID.GRAND_EXCHANGE_INVENTORY_GROUP_ID, 0);

    public static boolean isOpen() {

        return getView() != View.CLOSED && getView() != View.UNKNOWN;
    }

    public static boolean open() {

        if(isOpen()) {
            return true;
        }

        var npc = Npcs.query()
                .names("Grand Exchange Clerk")
                .actions("Exchange")
                .results()
                .nearest();

        if(npc == null) {
            return false;
        }

        return npc.interact("Exchange") && Time.sleepUntil(GrandExchange::isOpen, () -> Players.getLocal().isMoving(), 1200);
    }

    public static boolean close() {

        var close = CLOSE_BUTTON.resolve();

        if(!Widgets.isVisible(close)) {
            return false;
        }

        return close.interact("Close") && Time.sleepUntil(() -> !isOpen(), 1200);
    }

    public static boolean isSetupOpen() {

        return getView() == View.BUYING || getView() == View.SELLING;
    }

    public static View getView() {

        var setupWindow = Widgets.get(WidgetInfo.GRAND_EXCHANGE_OFFER_CONTAINER);

        if (Widgets.isVisible(setupWindow)) {
            var text = setupWindow.getChild(18).getText();

            if (text == null || text.isEmpty()) {
                return View.UNKNOWN;
            }

            if (text.equals("Sell offer")) {
                return View.SELLING;
            }

            if (text.equals("Buy offer")) {
                return View.BUYING;
            }

            // Widgets broke
            return View.UNKNOWN;
        }

        var geWindow = Widgets.get(WidgetInfo.GRAND_EXCHANGE_WINDOW_CONTAINER);

        if (Widgets.isVisible(geWindow)) {
            return View.OFFERS;
        }

        return View.CLOSED;
    }

    public static boolean isSelling()
    {
        return getView() == View.SELLING;
    }

    public static boolean isBuying()
    {
        return getView() == View.BUYING;
    }

    public static boolean isFull()
    {
        return getEmptySlots() == 0;
    }

    public static boolean isEmpty()
    {
        return getOffers().size() == 0;
    }

    public static int getEmptySlots()
    {
        var offerCount = getOffers().size();
        return Game.getMembershipDays() <= 0 ? F2P_SLOTS - offerCount : P2P_SLOTS - offerCount;
    }

    public static List<GrandExchangeOffer> getOffers() {
        return getOffers(Predicates.always());
    }

    public static List<GrandExchangeOffer> getOffers(Predicate<GrandExchangeOffer> predicate)
    {
        List<GrandExchangeOffer> out = new ArrayList<>();
        var offers = API_CONTEXT.getClient().getGrandExchangeOffers();

        if (offers == null) {
            return out;
        }

        for (var rlOffer : offers)
        {
            if (rlOffer.getItemId() <= 0) {
                continue;
            }

            var offer = new GrandExchangeOffer(rlOffer);

            if(!predicate.test(offer)) {
                continue;
            }

            out.add(offer);
        }

        return out;
    }

    public static boolean createBuyOffer() {

        if(getView() != View.OFFERS) {
            return false;
        }

        for(var i = 7; i < 15; i++) {

            var offerBox = Widgets.get(WidgetID.GRAND_EXCHANGE_GROUP_ID, i);

            if(!Widgets.isVisible(offerBox)) {
                continue;
            }

            var buyButton = offerBox.getChild(3);

            if(!Widgets.isVisible(buyButton)) {
                continue;
            }

            return buyButton.interact(Predicates.always()) && Time.sleepUntil(() -> getView() == View.BUYING, 1200);
        }

        return false;
    }

    public static boolean abortOffer(int itemId) {

        if(getView() != View.OFFERS) {
            return false;
        }

        for(var i = 7; i < 15; i++) {

            var offerBox = Widgets.get(WidgetID.GRAND_EXCHANGE_GROUP_ID, i);

            if(!Widgets.isVisible(offerBox)) {
                continue;
            }

            var abortBox = offerBox.getChild(2);
            if (!Widgets.isVisible(abortBox) || !abortBox.hasAction("Abort offer"))
            {
                continue;
            }

            var itemBox = offerBox.getChild(18);

            if(!Widgets.isVisible(itemBox)) {
                continue;
            }

            if(itemBox.getItemId() != itemId) {
                continue;
            }

            var abortedOffers = getAbortedOfferCount();
            return abortBox.interact("Abort offer") && Time.sleepUntil(() -> getAbortedOfferCount() > abortedOffers, 1200);
        }

        return false;
    }

    private static int getAbortedOfferCount() {
        return getOffers(o -> o.getState() == GrandExchangeOffer.State.CANCELLED_BUY || o.getState() == GrandExchangeOffer.State.CANCELLED_SELL).size();
    }

    public static boolean createSellOffer(int itemId) {
        var view = getView();

        if(view != View.SELLING && view != View.OFFERS || (view == View.SELLING && GrandExchangeSetup.isOccupied())) {
            return false;
        }

        if(getEmptySlots() == 0) {
            return false;
        }

        var inventory = GRAND_EXCHANGE_INVENTORY.resolve();

        if(!Widgets.isVisible(inventory)) {
            return false;
        }

        var notedId = ItemDefinition.getNotedId(itemId);

        for(var child : inventory.getDynamicChildren()) {

            if(child.getItemId() != itemId && child.getItemId() != notedId) {
                continue;
            }

            return child.interact("Offer") && Time.sleepUntil(() -> GrandExchangeSetup.getItemId() == itemId, 1200);
        }

        return false;
    }

    public static boolean canCollect() {
        return COLLECT_BUTTON.isWidgetVisible();
    }

    public static boolean collect() {
        return collect(false);
    }

    public static boolean collect(boolean toBank) {
        var collect = COLLECT_BUTTON.resolve();

        if(!Widgets.isVisible(collect)) {
            return false;
        }

        return collect.interact(toBank ? "Collect to bank" : "Collect to inventory") && Time.sleepUntil(() -> !canCollect(), 1200);
    }

    public enum View {
        CLOSED, OFFERS, BUYING, SELLING, UNKNOWN;

        public boolean isSetup() {
            return this == BUYING || this == SELLING;
        }
    }
}
