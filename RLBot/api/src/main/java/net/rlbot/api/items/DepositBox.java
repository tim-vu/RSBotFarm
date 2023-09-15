package net.rlbot.api.items;


import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;

import java.util.function.Supplier;

public class DepositBox
{
    private DepositBox()
    {
    }

    private static final WidgetAddress DEPOSIT_INV = new WidgetAddress(192, 4);
    private static final WidgetAddress DEPOSIT_EQUIPS = new WidgetAddress(192, 6);
    private static final WidgetAddress DEPOSIT_LOOTINGBAG = new WidgetAddress(192, 8);
    private static final WidgetAddress ROOT = new WidgetAddress(192, 1);
    private static final WidgetAddress EXIT = new WidgetAddress(192, 1, 11);
    private static final WidgetAddress QUANTITY_ONE = new WidgetAddress(192, 11);
    private static final WidgetAddress QUANTITY_FIVE = new WidgetAddress(192, 13);
    private static final WidgetAddress QUANTITY_TEN = new WidgetAddress(192, 15);
    private static final WidgetAddress QUANTITY_X = new WidgetAddress(192, 17);
    private static final WidgetAddress QUANTITY_ALL = new WidgetAddress(192, 19);

    public static boolean depositInventory()
    {
        var depositInventory = DEPOSIT_INV.resolve();

        if(!Widgets.isVisible(depositInventory)) {
            return false;
        }

        return depositInventory.interact("Deposit inventory") && Time.sleepUntil(Inventory::isEmpty, 2000);
    }

    public static boolean depositEquipment()
    {
        var depositEquipment = DEPOSIT_EQUIPS.resolve();

        if(!Widgets.isVisible(depositEquipment)) {
            return false;
        }

        return depositEquipment.interact("Deposit worn items") && Time.sleepUntil(Equipment::isEmpty, 2000);
    }

    public static boolean depositLootingBag()
    {
        var depositLootingbag = DEPOSIT_LOOTINGBAG.resolve();

        if(!Widgets.isVisible(depositLootingbag)) {
            return false;
        }

        //TODO: Add conditional sleep?
        return depositLootingbag.interact("Deposit loot");
    }

    public static boolean isOpen()
    {
        return ROOT.isWidgetVisible();
    }

    public static boolean close()
    {
        var exitDepositBox = EXIT.resolve();

        if (!Widgets.isVisible(exitDepositBox))
        {
            return false;
        }

        return exitDepositBox.interact("Close") && !Time.sleepUntil(() -> !isOpen(), 1000);
    }
}