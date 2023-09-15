package net.rlbot.api.widgets;

import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Time;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shop {

    private static final WidgetAddress SHOP = new WidgetAddress(300, 0);

    private static final WidgetAddress CLOSE = new WidgetAddress(300, 1, 11);
    private static final WidgetAddress SHOP_ITEMS = new WidgetAddress(300, 16);
    private static final WidgetAddress INVENTORY = new WidgetAddress(301, 0);

    public static boolean isOpen()
    {
        return SHOP.isWidgetVisible();
    }

    public static boolean close() {

        var close = CLOSE.resolve();

        if(close == null) {
            return false;
        }

        return close.interact("Close") && Time.sleepUntil(() -> !isOpen(), 1200);
    }

    public static int getStock(int itemId)
    {
        var items = SHOP_ITEMS.resolve();

        if (!Widgets.isVisible(items))
        {
            return 0;
        }

        var children = items.getChildren();
        if (children == null)
        {
            return 0;
        }

        return Arrays.stream(children)
                .filter(child -> child.getItemId() == itemId)
                .mapToInt(Widget::getItemQuantity)
                .sum();
    }

    public static boolean buyOne(int itemId)
    {
        return buy(itemId, 1);
    }

    public static boolean buyOne(String itemName)
    {
        return buy(itemName, 1);
    }

    public static boolean buyFive(int itemId)
    {
        return buy(itemId, 5);
    }

    public static boolean buyFive(String itemName)
    {
        return buy(itemName, 5);
    }

    public static boolean buyTen(int itemId)
    {
        return buy(itemId, 10);
    }

    public static boolean buyTen(String itemName)
    {
        return buy(itemName, 10);
    }

    public static boolean buyFifty(int itemId)
    {
        return buy(itemId, 50);
    }

    public static boolean buyFifty(String itemName)
    {
        return buy(itemName, 50);
    }

    public static boolean sellOne(int itemId)
    {
        return sell(itemId, 1);
    }

    public static boolean sellFive(int itemId)
    {
        return sell(itemId, 5);
    }

    public static boolean sellTen(int itemId)
    {
        return sell(itemId, 10);
    }

    public static boolean sellFifty(int itemId)
    {
        return sell(itemId, 50);
    }

    public static List<Integer> getItems()
    {
        List<Integer> out = new ArrayList<>();

        var container = SHOP_ITEMS.resolve();

        if (container == null)
        {
            return out;
        }

        var items = container.getChildren();

        if (items == null)
        {
            return out;
        }

        for (var item : items)
        {
            if (item.getItemId() != -1)
            {
                out.add(item.getItemId());
            }
        }

        return out;
    }

    private static boolean buy(int itemId, int amount)
    {
        return exchange(itemId, amount, SHOP_ITEMS.resolve());
    }

    private static boolean buy(String itemName, int amount)
    {
        return exchange(itemName, amount, SHOP_ITEMS.resolve());
    }

    private static boolean sell(int itemId, int amount)
    {
        return exchange(itemId, amount, INVENTORY.resolve());
    }

    private static boolean exchange(int itemId, int amount, Widget container)
    {
        if (container == null)
        {
            return false;
        }

        var items = container.getChildren();
        if (items == null)
        {
            return false;
        }

        for (var item : items)
        {
            if (item.getItemId() == itemId)
            {
                String action = Arrays.stream(item.getActions())
                        .filter(x -> x != null && x.contains(String.valueOf(amount)))
                        .findFirst()
                        .orElse(null);

                if (action == null)
                {
                    return false;
                }

                return item.interact(action);
            }
        }

        return false;
    }

    private static boolean exchange(String itemName, int amount, Widget container)
    {
        if (container == null)
        {
            return false;
        }

        var items = container.getChildren();
        if (items == null)
        {
            return false;
        }

        for (Widget item : items)
        {
            var nestedName = StringUtils.substringBetween(item.getName(), ">", "<");
            if (nestedName != null && nestedName.equals(itemName))
            {
                String action = Arrays.stream(item.getActions())
                        .filter(x -> x != null && x.contains(String.valueOf(amount)))
                        .findFirst()
                        .orElse(null);

                if (action == null)
                {
                    return false;
                }

                return item.interact(action);
            }
        }

        return false;
    }

}
