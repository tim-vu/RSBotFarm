package net.rlbot.api.items;

import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.Definitions;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.game.VarPlayer;
import net.rlbot.api.game.Vars;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.widgets.*;
import net.rlbot.internal.ApiContext;

public class GrandExchangeSetup {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext){
        API_CONTEXT = apiContext;
    }

    private static final int PRICE_VARBIT = 4398;
    private static final int QUANTITY_VARBIT = 4396;

    private static final WidgetAddress CONFIRM_BUTTON = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 29);

    private static final WidgetAddress GO_BACK_BUTTON = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 4);

    private static final WidgetAddress ABORT_BUTTON = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 23, 0);

    private static final WidgetAddress PROGRESS_BAR = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 23, 2);

    public static boolean isOpen() {
        return GrandExchange.getView().isSetup();
    }

    public static boolean isOccupied() {
        return PROGRESS_BAR.isWidgetVisible();
    }

    public static int getItemId()
    {
        return Vars.getVarp(VarPlayer.CURRENT_GE_ITEM.getId());
    }

    public static boolean setItem(int id)
    {
        if(isOccupied()) {
            return false;
        }

        Game.runScript(754, id, 84);
        return Time.sleepUntil(() -> getItemId() == id, 1800);
    }

    private static final WidgetAddress GUID_PRICE = new WidgetAddress(WidgetID.GRAND_EXCHANGE_GROUP_ID, 27);

    public static int getGuidePrice() {

        var price = GUID_PRICE.resolve();

        if(!Widgets.isVisible(price)) {
            return -1;
        }

        return Integer.parseInt(price.getText().replace(",", ""));
    }

    public static int getItemPrice()
    {
        return Vars.getBit(PRICE_VARBIT);
    }

    public static boolean setPrice(int price)
    {
        var enterPriceButton = Widgets.get(WidgetInfo.GRAND_EXCHANGE_OFFER_CONTAINER);

        if (enterPriceButton == null) {
            return false;
        }

        var child = enterPriceButton.getChild(12);

        if(child == null) {
            return false;
        }

        if(!child.interact("Enter price") || !Time.sleepUntil(Dialog::isEnterInputOpen, 1800)) {
            return false;
        }

        Time.sleep(600);
        Keyboard.sendText(String.valueOf(price), true);
        return Time.sleepUntil(() -> getItemPrice() == price && !Dialog.isEnterInputOpen(), 1800);
    }

    public static int getQuantity()
    {
        return Vars.getBit(QUANTITY_VARBIT);
    }

    public static boolean setQuantity(int quantity)
    {
        var enterPriceButton = Widgets.get(WidgetInfo.GRAND_EXCHANGE_OFFER_CONTAINER);
        if (enterPriceButton == null) {
            return false;
        }

        var child = enterPriceButton.getChild(7);

        if(child == null) {
            return false;
        }

        if(!child.interact("Enter quantity") || !Time.sleepUntil(Dialog::isEnterInputOpen, 1200)) {
            return false;
        }

        Time.sleep(600);
        Keyboard.sendText(String.valueOf(quantity), true);
        return Time.sleepUntil(() -> getQuantity() == quantity, 1200);
    }

    public static boolean goBack() {
        var backButton = GO_BACK_BUTTON.resolve();

        if(!Widgets.isVisible(backButton)) {
            return false;
        }

        return backButton.interact("Back") && Time.sleepUntil(() -> GrandExchange.getView() == GrandExchange.View.OFFERS, 1200);
    }

    public static boolean confirm()
    {
        var confirm = CONFIRM_BUTTON.resolve();

        if (!Widgets.isVisible(confirm)) {
            return false;
        }

        return confirm.interact("Confirm") && Time.sleepUntil(() -> GrandExchange.getView() == GrandExchange.View.OFFERS, 1200);
    }

    public enum State {
        CREATE_BUY_OFFER,
        CREATE_SELL_OFFER,
        BUY_OFFER_IN_PROGRESS,
        SELL_OFFER_IN_PROGRESS,
        BUY_OFFER_CANCELED,
        SELL_OFFER_CANCELED,
        BUY_OFFER_COMPLETED,
        SELL_OFFERED_COMPLETED
    }
}
