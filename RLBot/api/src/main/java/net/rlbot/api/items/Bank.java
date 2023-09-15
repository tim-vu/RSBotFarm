package net.rlbot.api.items;

import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.queries.ItemQuery;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.*;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Vars;
import net.rlbot.api.input.Keyboard;
import net.runelite.api.InventoryID;
import net.runelite.api.Varbits;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bank extends Items
{
    private static Bank instance;

    private static Inventory inventoryInstance;

    private Bank(ApiContext apiContext)
    {
        super(apiContext, InventoryID.BANK, item ->
        {
            item.setWidgetId(WidgetInfo.BANK_ITEM_CONTAINER.getPackedId());
            return true;
        });
    }

    private static void init(ApiContext apiContext) {
        instance = new Bank(apiContext);
        inventoryInstance = new Inventory(apiContext);
    }

    private static final int WITHDRAW_MODE_VARBIT = 3958;
    private static final int QUANTITY_MODE_VARP = 6590;
    private static final WidgetAddress TAB_CONTAINER = new WidgetAddress(WidgetID.BANK_GROUP_ID, WidgetID.Bank.TAB_CONTAINER);
    private static final WidgetAddress BANK_CAPACITY = new WidgetAddress(WidgetID.BANK_GROUP_ID, 9);
    private static final WidgetAddress RELEASE_PLACEHOLDERS = new WidgetAddress(WidgetID.BANK_GROUP_ID, 56);
    private static final WidgetAddress SETTINGS_CONTAINER = new WidgetAddress(WidgetID.BANK_GROUP_ID, 48);
    private static final WidgetAddress WITHDRAW_ITEM = new WidgetAddress(WidgetID.BANK_GROUP_ID, Component.BANK_WITHDRAW_ITEM.childId);
    private static final WidgetAddress WITHDRAW_NOTE = new WidgetAddress(WidgetID.BANK_GROUP_ID, Component.BANK_WITHDRAW_NOTE.childId);
    private static final WidgetAddress EXIT = new WidgetAddress(WidgetID.BANK_GROUP_ID, 2, 11);

    public static ItemQuery query() {
        return new ItemQuery(Bank::getAll);
    }

    public static boolean isCached() {
        return instance.isCached_();
    }

    public static int getFreeSlots()
    {
        if (!isOpen())
        {
            return -1;
        }

        return getCapacity() - getOccupiedSlots();
    }

    public static int getCapacity()
    {
        var widget = BANK_CAPACITY.resolve();

        if(!Widgets.isVisible(widget)) {
            return -1;
        }

        return Integer.parseInt(widget.getText());
    }

    public static int getOccupiedSlots()
    {
        var widget = Widgets.get(WidgetInfo.BANK_ITEM_COUNT_TOP);

        if(!Widgets.isVisible(widget)) {
            return -1;
        }

        return Integer.parseInt(widget.getText());
    }

    public static boolean depositInventory()
    {
        var widget = Widgets.get(WidgetInfo.BANK_DEPOSIT_INVENTORY);

        if (!Widgets.isVisible(widget)) {
            return false;
        }

        return widget.interact("Deposit inventory");
    }

    public static boolean depositEquipment()
    {
        var widget = Widgets.get(WidgetInfo.BANK_DEPOSIT_EQUIPMENT);

        if (!Widgets.isVisible(widget)) {
            return false;
        }

        var bankCount = getCount();
        return widget.interact("Deposit worn items") && Time.sleepUntil(() -> Bank.getCount() > bankCount, 1000);
    }

    public static boolean isOpen()
    {
        return Widgets.isVisible(WidgetInfo.BANK_ITEM_CONTAINER);
    }

    public static boolean open() {

        var objects = SceneObjects.query().names("Grand Exchange booth", "Bank booth", "Bank chest")
                .actions("Bank", "Use")
                .results();

        var npcs = Npcs.query().names("Banker")
                .results();

        var best = Stream.concat(objects.stream(), npcs.stream())
                .min(Comparator.comparingDouble(Positionable::distance))
                .orElse(null);

        if(best == null) {
            return false;
        }

        return best.interact("Bank", "Use") && Time.sleepUntil(Bank::isOpen, () -> Players.getLocal().isMoving(), 1200);
    }

    public static boolean isEmpty()
    {
        return getAll().isEmpty();
    }

    public static boolean depositAll(String... names)
    {
        return depositAll(Predicates.names(names));
    }

    public static boolean depositAll(int... ids)
    {
        return depositAll(Predicates.ids(ids));
    }

    public static boolean depositAll(Predicate<Item> filter)
    {
        return deposit(filter, Integer.MAX_VALUE);
    }

    public static boolean depositAllExcept(String... names)
    {
       return depositAllExcept(Predicates.names(names));
    }

    public static boolean depositAllExcept(int... ids)
    {
        return depositAllExcept(Predicates.ids(ids));
    }

    public static boolean depositAllExcept(Predicate<Item> filter)
    {
        return depositAll(filter.negate());
    }

    public static boolean deposit(String name, int amount)
    {
       return deposit(x -> Objects.equals(x.getName(), name), amount);
    }

    public static boolean deposit(int id, int amount)
    {
        return deposit(x -> x.getId() == id, amount);
    }

    public static boolean deposit(Predicate<Item> filter, int amount)
    {
        Item item = Inventory.getFirst(filter);

        if (item == null)
        {
            return false;
        }

        String action = getAction(item, amount, false);

        var count = Bank.getCount();

        if(!item.interact(action)) {
            return false;
        }

        if (action.equals("Deposit-X"))
        {
            if(!Time.sleepUntil(Dialog::isEnterInputOpen, 1800)) {
                return false;
            }

            Time.sleep(600);
            Keyboard.sendText(String.valueOf(amount), true);
            return Time.sleepUntil(() -> !Dialog.isEnterInputOpen(), 1800);
        }

        return true;
    }

    public static boolean withdrawAll(String name, WithdrawMode withdrawMode)
    {
        return withdrawAll(x -> Objects.equals(x.getName(), name), withdrawMode);
    }

    public static boolean withdrawAll(int id, WithdrawMode withdrawMode)
    {
        return withdrawAll(x -> x.getId() == id, withdrawMode);
    }

    public static boolean withdrawAll(Predicate<Item> filter, WithdrawMode withdrawMode)
    {
        return withdraw(filter, Integer.MAX_VALUE, withdrawMode);
    }

    public static boolean withdraw(String name, int amount, WithdrawMode withdrawMode)
    {
        return withdraw(x -> Objects.equals(x.getName(), name), amount, withdrawMode);
    }

    public static boolean withdraw(int id, int amount, WithdrawMode withdrawMode)
    {
        return withdraw(x -> x.getId() == id, amount, withdrawMode);
    }

    public static boolean withdraw(Predicate<Item> filter, int amount, WithdrawMode withdrawMode) {

        Item item = getFirst(filter.and(x -> !x.isPlaceholder()));

        if (item == null) {
            return false;
        }

        String action = getAction(item, amount, true);

        if (withdrawMode == WithdrawMode.NOTED && !isNotedWithdrawMode()) {

            if(!setWithdrawMode(true)) {
                return false;
            }

            Time.sleepUntil(Bank::isNotedWithdrawMode, 1000);
            return false;
        }

        if (withdrawMode == WithdrawMode.ITEM && isNotedWithdrawMode()) {

            if(!setWithdrawMode(false)) {
                return false;
            }

            Time.sleepUntil(() -> !isNotedWithdrawMode(), 1000);
            return false;
        }

        var count = Bank.getCount();

        if(!item.interact(action)) {
            return false;
        }

        if (action.equals("Withdraw-X")) {
            if(!Time.sleepUntil(Dialog::isEnterInputOpen, 1800)) {
                return false;
            }

            Time.sleep(600);
            Keyboard.sendText(String.valueOf(amount), true);
            return Time.sleepUntil(() -> !Dialog.isEnterInputOpen(), 1800);
        }

        return true;
    }

    public static boolean setWithdrawMode(boolean noted)
    {
        var widget = noted ? WITHDRAW_NOTE.resolve() : WITHDRAW_ITEM.resolve();

        if (!Widgets.isVisible(widget)) {
            return false;
        }

        var action = noted ? "Note" : "Item";

        return widget.interact(action) && Time.sleepUntil(() -> isNotedWithdrawMode() == noted, 1000);
    }

    public static boolean isNotedWithdrawMode()
    {
        return Vars.getBit(WITHDRAW_MODE_VARBIT) == 1;
    }

    public static List<Item> getAll() {

        return getAll(x -> true);
    }

    public static List<Item> getAll(Predicate<Item> filter)
    {
        return instance.all(filter);
    }

    public static List<Item> getAll(Collection<Integer> itemIds) {
        return instance.all(Predicates.ids(itemIds));
    }

    public static Item getFirst(Predicate<Item> filter)
    {
        return instance.first(filter);
    }

    public static Item getFirst(int... itemIds) {
        return instance.first(itemIds);
    }

    public static Item getFirst(Collection<Integer> itemIds) {
        return instance.first(itemIds);
    }

    public static boolean contains(Predicate<Item> filter)
    {
        return instance.contains_(filter);
    }

    public static boolean contains(int... itemIds) {
        return instance.contains_(itemIds);
    }

    public static boolean contains(Collection<Integer> itemIds)
    {
        return instance.contains_(itemIds);
    }

    public static int getCount(boolean stacks, Predicate<Item> filter)
    {
        return instance.count(stacks, filter);
    }

    public static int getCount(Predicate<Item> filter)
    {
        return instance.count(false, filter);
    }

    public static int getCount(boolean stacks, int... itemIds) {
        return instance.count(stacks, itemIds);
    }

    public static int getCount(int... itemIds) {
        return instance.count(false, itemIds);
    }

    public static int getCount(boolean stacks, Collection<Integer> itemIds) {
        return instance.count(stacks, itemIds);
    }

    public static int getCount(Collection<Integer> itemIds) {
        return instance.count(false, itemIds);
    }

    public static int getCount() {
        return instance.count(true, Predicates.always());
    }

    private static List<Widget> getTabs()
    {
        var widget = Widgets.get(WidgetInfo.BANK_TAB_CONTAINER);

        if(!Widgets.isVisible(widget)) {
            return Collections.emptyList();
        }

        return Arrays.stream(widget.getDynamicChildren()).filter(w -> w.hasAction("Collapse tab")).collect(Collectors.toList());
    }

    public static boolean hasTabs()
    {
        return !getTabs().isEmpty();
    }

    public static boolean isMainTabOpen()
    {
        return isTabOpen(0);
    }

    public static boolean isTabOpen(int index)
    {
        return Vars.getBit(Varbits.CURRENT_BANK_TAB) == index;
    }

    public static boolean openMainTab()
    {
        return openTab(0);
    }

    public static boolean openTab(int index)
    {
        if (index < 0 || index > getTabs().size() || isTabOpen(index))
        {
            return false;
        }

        var tabContainer = TAB_CONTAINER.resolve();

        if(!Widgets.isVisible(tabContainer)) {
            return false;
        }

        var tabWidget = tabContainer.getChild(10 + index);

        if(tabWidget == null) {
            return false;
        }

        var action = index == 0 ? "View all items" : "View tab";

        return tabWidget.interact(action) && Time.sleepUntil(() -> isTabOpen(index), 500);
    }

    public static class Inventory extends Items
    {
        public Inventory(ApiContext apiContext)
        {
            super(apiContext, InventoryID.INVENTORY, item ->
            {
                item.setWidgetId(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId());
                return true;
            });
        }

        public static List<Item> getAll()
        {
            return getAll(x -> true);
        }

        public static List<Item> getAll(Predicate<Item> filter)
        {
            return inventoryInstance.all(filter);
        }

        public static List<Item> getAll(String... names)
        {
            return inventoryInstance.all(Predicates.names(names));
        }

        public static Item getFirst(Predicate<Item> filter)
        {
            return inventoryInstance.first(filter);
        }

        public static int getCount(boolean stacks, Predicate<Item> filter)
        {
            return instance.count(stacks, filter);
        }

        public static int getCount(Predicate<Item> filter)
        {
            return instance.count(false, filter);
        }

        public static int getCount(boolean stacks, int itemId) {
            return instance.count(stacks, itemId);
        }

        public static int getCount(int itemId) {
            return instance.count(false, itemId);
        }
    }

    private static String getAction(Item item, int amount, Boolean withdraw)
    {
        String action = withdraw ? "Withdraw" : "Deposit";
        if (amount == 1)
        {
            action += "-1";
        }
        else if (amount == 5)
        {
            action += "-5";
        }
        else if (amount == 10)
        {
            action += "-10";
        }
        else if (withdraw && amount >= item.getQuantity())
        {
            action += "-All";
        }
        else if(withdraw && !item.isStackable() && amount == net.rlbot.api.items.Inventory.getFreeSlots()) {
            action += "-All";
        }
        else if (!withdraw && amount >= Inventory.getCount(true, item.getId()))
        {
            action += "-All";
        }
        else
        {
            if (item.hasAction(action + "-" + amount))
            {
                action += "-" + amount;
            }
            else
            {
                action += "-X";
            }
        }
        return action;
    }

    public enum Component
    {
        BANK_REARRANGE_SWAP(WidgetID.BANK_GROUP_ID, 17),
        BANK_REARRANGE_INSERT(WidgetID.BANK_GROUP_ID, 19),
        BANK_WITHDRAW_ITEM(WidgetID.BANK_GROUP_ID, 22),
        BANK_WITHDRAW_NOTE(WidgetID.BANK_GROUP_ID, 24),
        BANK_QUANTITY_BUTTONS_CONTAINER(WidgetID.BANK_GROUP_ID, 26),
        BANK_QUANTITY_ONE(WidgetID.BANK_GROUP_ID, 28),
        BANK_QUANTITY_FIVE(WidgetID.BANK_GROUP_ID, 30),
        BANK_QUANTITY_TEN(WidgetID.BANK_GROUP_ID, 32),
        BANK_QUANTITY_X(WidgetID.BANK_GROUP_ID, 34),
        BANK_QUANTITY_ALL(WidgetID.BANK_GROUP_ID, 36),
        BANK_PLACEHOLDERS_BUTTON(WidgetID.BANK_GROUP_ID, 38),
        EMPTY(-1, -1);

        private final int groupId;
        private final int childId;

        Component(int groupId, int childId)
        {
            this.groupId = groupId;
            this.childId = childId;
        }
    }

    public enum WithdrawMode
    {
        NOTED, ITEM, DEFAULT
    }

    public static boolean close()
    {
        var exitBank = EXIT.resolve();

        if(!Widgets.isVisible(exitBank)) {
            return false;
        }

        return exitBank.interact("Close") && Time.sleepUntil(() -> !Bank.isOpen(), 1000);
    }
}