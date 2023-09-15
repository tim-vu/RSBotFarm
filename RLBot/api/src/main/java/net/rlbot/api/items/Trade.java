package net.rlbot.api.items;

import net.rlbot.api.widgets.*;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.VarClientStr;
import net.rlbot.api.game.Vars;
import net.rlbot.api.input.Keyboard;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Trade
{
    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final WidgetAddress OUR_ITEMS = new WidgetAddress(WidgetID.PLAYER_TRADE_SCREEN_GROUP_ID, 25);
    private static final WidgetAddress THEIR_ITEMS = new WidgetAddress(WidgetID.PLAYER_TRADE_SCREEN_GROUP_ID, 28);
    private static final WidgetAddress INVENTORY = new WidgetAddress(WidgetID.PLAYER_TRADE_INVENTORY_GROUP_ID, 0);
    private static final WidgetAddress ACCEPT_1 = new WidgetAddress(WidgetID.PLAYER_TRADE_SCREEN_GROUP_ID, 10);
    private static final WidgetAddress ACCEPT_2 = new WidgetAddress(WidgetID.PLAYER_TRADE_CONFIRM_GROUP_ID, WidgetID.TradeScreen.SECOND_ACCEPT_FUNC);
    private static final WidgetAddress DECLINE_1 = new WidgetAddress(WidgetID.PLAYER_TRADE_SCREEN_GROUP_ID, 13);
    private static final WidgetAddress DECLINE_2 = new WidgetAddress(WidgetID.PLAYER_TRADE_CONFIRM_GROUP_ID, WidgetID.TradeScreen.SECOND_DECLINE_FUNC);
    private static final WidgetAddress ACCEPT_STATUS_1 = new WidgetAddress(WidgetID.PLAYER_TRADE_SCREEN_GROUP_ID, 30);
    private static final WidgetAddress ACCEPT_STATUS_2 = new WidgetAddress(WidgetID.PLAYER_TRADE_CONFIRM_GROUP_ID, 4);

    public static boolean isOpen()
    {
        return isFirstScreenOpen() || isSecondScreenOpen();
    }

    public static boolean isSecondScreenOpen()
    {
        return ACCEPT_2.isWidgetVisible();
    }

    public static boolean isFirstScreenOpen()
    {
        return ACCEPT_1.isWidgetVisible();
    }

    public static boolean acceptFirstScreen()
    {
        var button = ACCEPT_1.resolve();

        if (!Widgets.isVisible(button))
        {
            return false;
        }

        if(!button.interact("Accept"))  {
            return false;
        }

        if(!Time.sleepUntil(() -> isSecondScreenOpen() || !isOpen(), 1200)) {
            return false;
        }

        return isSecondScreenOpen();
    }

    public static boolean acceptSecondScreen()
    {
        var button = ACCEPT_2.resolve();

        if (!Widgets.isVisible(button))
        {
            return false;
        }

        if(!button.interact("Accept"))  {
            return false;
        }

        return Time.sleepUntil(() -> !isOpen() || hasAcceptedSecondScreen(false), 1200);
    }

    public static boolean declineFirstScreen()
    {
        var button = DECLINE_1.resolve();

        if (!Widgets.isVisible(button))
        {
            return false;
        }

        return button.interact("Decline") && Time.sleepUntil(() -> !isOpen(), 1000);
    }

    public static boolean declineSecondScreen() {

        var button = DECLINE_2.resolve();

        if (!Widgets.isVisible(button)) {
            return false;
        }

        return button.interact("Decline") && Time.sleepUntil(() -> !isOpen(), 1000);
    }

    public static boolean hasAccepted(boolean them)
    {
        return hasAcceptedFirstScreen(them) || hasAcceptedSecondScreen(them);
    }

    public static boolean hasAcceptedFirstScreen(boolean them)
    {
        Widget widget = ACCEPT_STATUS_1.resolve();
        return widget != null && widget.getText().equals(them ? "Other player has accepted." : "Waiting for other player...");
    }

    public static boolean hasAcceptedSecondScreen(boolean them)
    {
        Widget widget = ACCEPT_STATUS_2.resolve();
        return widget != null && widget.getText().equals(them ? "Other player has accepted." : "Waiting for other player...");
    }

    public static boolean offerAll(Predicate<Item> filter) {
        Item item = Trade.getInventory(filter).stream().findFirst().orElse(null);

        if (item == null)
        {
            return false;
        }

        var count = getInventoryCount();

        if(!item.interact("Offer-All")) {
            return false;
        }


        return Time.sleepUntil(() -> getInventoryCount() < count, 2000);
    }

    public static boolean offerAll(int itemId) {
        return offerAll(i -> i.getId() == itemId);
    }

    public static boolean offerAll(String name) {
        return offerAll(i -> i.getName().equals(name));
    }

    public static boolean offer(Predicate<Item> filter, int amount)
    {
        Item item = Trade.getInventory(filter).stream().findFirst().orElse(null);

        if (item == null)
        {
            return false;
        }

        var action = switch (amount) {
            case 1 -> "Offer";
            case 5 -> "Offer-5";
            case 10 -> "Offer-10";
            default -> {
                if (amount > Inventory.getCount(true, item.getId())) {
                    yield "Offer-All";
                }

                yield "Offer-X";
            }
        };

        var count = getInventoryCount();

        if(!item.interact(action)) {
            return false;
        }

        if(action.equals("Offer-X")) {

            if(!Time.sleepUntil(Dialog::isEnterInputOpen, 2000)) {
                return false;
            }

            Keyboard.sendText(Integer.toString(amount), true);

            return Time.sleepUntil(() -> !Dialog.isEnterInputOpen() && getInventoryCount() < count, 2000);
        }

        return Time.sleepUntil(() -> getInventoryCount() < count, 2000);
    }

    private static int getInventoryCount() {
        return getInventory(Predicates.always()).stream().mapToInt(Item::getQuantity).sum();
    }

    public static void offer(int id, int quantity)
    {
        offer(x -> x.getId() == id, quantity);
    }

    public static void offer(String name, int quantity)
    {
        offer(x -> x.getName() != null && x.getName().equals(name), quantity);
    }

    public static List<Item> getAll(boolean theirs, Predicate<Item> filter)
    {
        List<Item> items = new ArrayList<>();

        ItemContainer container = API_CONTEXT.getClient().getItemContainer(theirs ? InventoryID.TRADEOTHER : InventoryID.TRADE);

        if (container == null)
        {
            return items;
        }

        var containerItems = container.getItems();

        var result = new ArrayList<Item>();

        for (int i = 0, containerItemsLength = containerItems.length; i < containerItemsLength; i++)
        {
            var rItem = containerItems[i];

            if (rItem != null && rItem.getId() != -1)
            {
                var item = new Item(rItem, i);
                var containerWidget = theirs ? THEIR_ITEMS.resolve() : OUR_ITEMS.resolve();
                item.setWidgetId(item.calculateWidgetId(containerWidget));

                if (!filter.test(item)) {
                    continue;
                }

                items.add(item);
            }
        }

        return items;
    }

    public static List<Item> getInventory(Predicate<Item> filter)
    {
        List<Item> items = new ArrayList<>();
        ItemContainer container = API_CONTEXT.getClient().getItemContainer(InventoryID.INVENTORY);
        if (container == null)
        {
            return items;
        }

        var containerItems = container.getItems();

        var result = new ArrayList<Item>();

        for (int i = 0, containerItemsLength = containerItems.length; i < containerItemsLength; i++)
        {
            var rItem = containerItems[i];

            if (rItem != null && rItem.getId() != -1)
            {
                var item = new Item(rItem, i);
                item.setWidgetId(item.calculateWidgetId(INVENTORY.resolve()));

                if (!filter.test(item)) {
                    continue;
                }

                items.add(item);
            }
        }

        return items;
    }

    public static List<Item> getAll(boolean theirs)
    {
        return getAll(theirs, x -> true);
    }

    public static List<Item> getAll(boolean theirs, int... ids)
    {
        return getAll(theirs, Predicates.ids(ids));
    }

    public static List<Item> getAll(boolean theirs, String... names)
    {
        return getAll(theirs, Predicates.names(names));
    }

    public static Item getFirst(boolean theirs, Predicate<Item> filter)
    {
        return getAll(theirs, filter).stream().findFirst().orElse(null);
    }

    public static Item getFirst(boolean theirs, int... ids)
    {
        return getFirst(theirs, Predicates.ids(ids));
    }

    public static Item getFirst(boolean theirs, String... names)
    {
        return getFirst(theirs, Predicates.names(names));
    }

    public static boolean contains(boolean theirs, Predicate<Item> filter)
    {
        return getFirst(theirs, filter) != null;
    }

    public static boolean contains(boolean theirs, int... ids)
    {
        return contains(theirs, Predicates.ids(ids));
    }

    public static boolean contains(boolean theirs, String... names)
    {
        return contains(theirs, Predicates.names(names));
    }

    public static String getTradingPlayer()
    {
        return Vars.getVarcStr(VarClientStr.DUEL_OPPONENT_NAME);
    }
}