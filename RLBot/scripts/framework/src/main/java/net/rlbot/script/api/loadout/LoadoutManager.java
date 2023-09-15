package net.rlbot.script.api.loadout;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Slf4j
public class LoadoutManager {

    private static int getTotalCount(Supply supply) {
        var count = 0;

        for(var itemId : supply.getItemIds()) {
            count += Inventory.getCount(true, itemId);
            count += Inventory.getCount(true, ItemDefinition.getNotedId(itemId));
            count += Bank.getCount(true, itemId);
            count += Equipment.getCount(true, itemId);
        }

        return count;
    }

    public static boolean isExhausted(Loadout loadout) {

        if(!Bank.isCached()) {
            return false;
        }

        for(var inventorySupply : loadout.getInventorySupplies()) {

            if(getTotalCount(inventorySupply) >= inventorySupply.getMinimumAmount()) {
                continue;
            }

            log.info("Supply exhausted: {}", ItemDefinition.getName(inventorySupply.getItemIds().get(0)));
            return true;
        }

        for(var equipmentSet : loadout.getEquipmentSets()) {
            for(var equipmentSupply : equipmentSet.getSupplies()) {

                if (getTotalCount(equipmentSupply) >= equipmentSupply.getMinimumAmount()) {
                    continue;
                }

                log.info("Supply exhausted: {}", ItemDefinition.getName(equipmentSupply.getItemIds().get(0)));
                return true;
            }
        }

        return false;
    }

    public static boolean isLoadoutSetup(Loadout loadout) {
        return isLoadoutSetup(loadout, true);
    }

    public static boolean isLoadoutSetup(Loadout loadout, boolean allowExtraItems) {

        if(!hasLoadout(loadout, allowExtraItems)) {
            return false;
        }

        var activeSet = getActiveSet(loadout);

        if(activeSet == null) {
            return true;
        }

        for (var equipmentSupply : activeSet.getSupplies()) {

            var inInventory = Inventory.contains(equipmentSupply);

            if(equipmentSupply.isStackable()) {

                if(inInventory) {
                    log.trace("We should equip {}", equipmentSupply);
                    return false;
                }

                continue;
            }

            if(!Equipment.contains(equipmentSupply) && inInventory) {
                return false;
            }

        }

        return true;
    }

    public static boolean hasLoadout(Loadout loadout) {
        return hasLoadout(loadout, i -> true);
    }

    public static boolean hasLoadout(Loadout loadout, boolean allowExtraItems) {
        return hasLoadout(loadout, i -> allowExtraItems);
    }

    public static boolean hasLoadout(Loadout loadout, Set<Integer> allowedExtraItems) {
        return hasLoadout(loadout, i -> allowedExtraItems.contains(i.getId()));
    }

    public static boolean hasLoadout(Loadout loadout, Predicate<Item> isAllowed) {

        var supplies = Stream.concat(
                loadout.getInventorySupplies().stream(),
                loadout.getEquipmentSets().stream().flatMap(s -> s.getSupplies().stream())
        ).toList();

        for(var supply : supplies) {

            var count = supply.getCurrentCount();
            if(count >= supply.getMinimumAmount() && count <= supply.getMaximumAmount()) {
                continue;
            }

            return false;
        }

        return !Inventory.contains(i -> !loadout.test(i) && !isAllowed.test(i)) && !Equipment.contains(i -> !loadout.test(i) && !isAllowed.test(i));
    }

    public static Result setup(Loadout loadout) {

        var activeSet = getActiveSet(loadout);
        return setup(loadout, activeSet);
    }

    private static EquipmentSet getActiveSet(Loadout loadout) {

        if(loadout.getEquipmentSets().size() == 0) {
            return null;
        }

        if(loadout.getEquipmentSets().size() == 1) {
            return loadout.getEquipmentSets().get(0);
        }

        var equipmentSetMatches = new HashMap<EquipmentSet, Integer>();

        for(var equipmentSet : loadout.getEquipmentSets()) {
            equipmentSetMatches.put(equipmentSet, 0);
        }

        for(var item : Equipment.getAll()) {

            for(var set : loadout.getEquipmentSets()) {

                if (!set.containsItem(item.getId())) {
                    continue;
                }

                equipmentSetMatches.put(set, equipmentSetMatches.get(set));
            }
        }

        return equipmentSetMatches.entrySet().stream()
                .max(Comparator.comparingInt(HashMap.Entry::getValue))
                .get()
                .getKey();
    }

    private static final int EQUIPMENT_TIMEOUT = 1800;

    private static final int BANK_TIMEOUT = 1800;

    public static Result setup(Loadout loadout, EquipmentSet activeSet) {

        if(!loadout.getEquipmentSets().isEmpty() && activeSet == null) {
            throw new IllegalStateException("Active set is null");
        }

        if(isExhausted(loadout)) {
            return Result.EXHAUSTED;
        }

        if(isDone(loadout, activeSet)) {

            if(Bank.isOpen()) {

                log.debug("Closing the bank");
                if(!Bank.close()) {
                    log.warn("Failed to close the bank");
                    return Result.IN_PROGRESS;
                }

                Reaction.REGULAR.sleep();
            }

            return Result.SUCCESS;
        }

        if(!Bank.isOpen()) {

            if(activeSet != null) {

                //Equip items in the inventory that are part of the active set
                for(var equipmentSupply : activeSet.getSupplies()) {

                    var item = Inventory.getFirst(equipmentSupply);

                    if(item == null) {
                        continue;
                    }

                    if(Equipment.contains(equipmentSupply) && !item.isStackable()) {
                        log.trace("Already have {} equipped", item.getName());
                        continue;
                    }

                    if(!equipItems(equipmentSupply, item)) {
                        return Result.FAILURE;
                    }

                    return Result.IN_PROGRESS;
                }

                //Unequip items that are not part of the active set
                if(!Inventory.isFull()) {

                    for (var item : Equipment.getAll()) {

                        if (activeSet.test(item)) {

                            var equipmentSupply = activeSet.getSupply(item);

                            if (equipmentSupply.getMaximumAmount() >= item.getQuantity()) {
                                continue;
                            }
                        }

                        log.debug("Unequipping {}", item.getName());
                        if (!item.interact(Predicates.always()) || !Time.sleepUntil(() -> Equipment.contains(item.getId()), EQUIPMENT_TIMEOUT)) {
                            log.warn("Failed to unequip {}", item.getName());
                            return Result.FAILURE;
                        }

                        Reaction.REGULAR.sleep();
                        return Result.IN_PROGRESS;
                    }
                }
            }
        }

        if(!Bank.isOpen()) {

            log.debug("Opening bank");
            if(!Bank.open()) {
                log.warn("Failed to open the bank");
                return Result.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        if(Equipment.getCount(i -> activeSet == null || !activeSet.test(i)) > 0) {

            log.debug("Depositing inventory");
            if(!Bank.depositEquipment() || Time.sleepUntil(Equipment::isEmpty, EQUIPMENT_TIMEOUT)) {
                log.warn("Failure to deposit equipment");
                return Result.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        if(!Inventory.isEmpty() && Inventory.getCount(loadout) == 0) {

            log.debug("Depositing inventory");
            if(!Bank.depositInventory() || !Time.sleepUntil(Inventory::isEmpty, BANK_TIMEOUT)) {
                log.warn("Unable to deposit inventory");
                return Result.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        //Depositing
        var supplyToItem = Inventory.getAll().stream()
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(loadout.getSupply(i))));

        for(var entry : supplyToItem.entrySet()) {

            var optionalSupply = entry.getKey();
            var items = entry.getValue();

            if(optionalSupply.isEmpty()) {

                var item = items.get(0);
                log.debug("Depositing non supply: " + item.getName());
                if(!Bank.depositAll(item.getId()) || !Time.sleepUntil(() -> !Inventory.contains(item.getId()), BANK_TIMEOUT)) {
                    log.warn("Unable to deposit: " + item.getName());
                    return Result.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return Result.IN_PROGRESS;
            }

            var supply = optionalSupply.get();
            var amount = items.stream().mapToInt(Item::getQuantity).sum();

            if(amount <= supply.getMaximumAmount()) {
                continue;
            }

            var depositAmount = supply.getMaximumAmount() - amount;
            log.trace("Deposit amount: {}", depositAmount);

            var comparator = Comparator.<Item>comparingInt(i -> supply.getItemIds().indexOf(i.getId()));

            if(supply.isMinimumDose()) {
                comparator = comparator.reversed();
            }

            var toDeposit = items.stream().min(comparator).get();
            var currentCount = Inventory.getCount(true, toDeposit.getId());

            log.debug("Depositing supply: {}", toDeposit.getName());
            if(!Bank.deposit(toDeposit.getId(), Math.min(toDeposit.getQuantity(), depositAmount)) || !Time.sleepUntil(() -> Inventory.getCount(true, toDeposit.getId()) < currentCount, BANK_TIMEOUT)) {
                log.warn("Failed to deposit: {}", toDeposit.getName());
                Time.sleepTick();
                return Result.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        //Withdraw active set
        if(activeSet != null) {

            log.trace("Withdrawing active set");
            for(var equipmentSupply : activeSet.getSupplies()) {

                var currentCount = equipmentSupply.getCurrentCount();

                if(currentCount == equipmentSupply.getMaximumAmount()) {
                    continue;
                }

                var bankCount = Bank.getCount(true, equipmentSupply);
                if(bankCount == 0) {
                    continue;
                }

                var comparator = Comparator.<Item>comparingInt(i -> equipmentSupply.getItemIds().indexOf(i.getId()));

                if(equipmentSupply.isMinimumDose()) {
                    comparator = comparator.reversed();
                }

                var toWithdraw = Bank.getAll(equipmentSupply).stream()
                        .min(comparator)
                        .get();

                var withdrawAmount = Math.min(toWithdraw.getQuantity(), equipmentSupply.getMaximumAmount() - currentCount);

                var slotsRequired = toWithdraw.isStackable() ? (Inventory.contains(toWithdraw.getSlot()) ? 0 : 1) : withdrawAmount;
                log.trace("Slots required for {}: {}", toWithdraw.getName(), slotsRequired);

                if(Inventory.getFreeSlots() >= slotsRequired) {
                    var currentQuantity = toWithdraw.getQuantity();
                    log.debug("Withdrawing {}: {}", toWithdraw.getName(), withdrawAmount);
                    if(!Bank.withdraw(toWithdraw.getId(), withdrawAmount, Bank.WithdrawMode.ITEM) ||
                        !Time.sleepUntil(() -> Inventory.getCount(true, toWithdraw.getId()) > currentCount, BANK_TIMEOUT)) {
                        log.warn("Failure to withdraw {}", toWithdraw.getName());
                        log.debug("Previous count: {}, Current count: {}", currentCount, Inventory.getCount(true, toWithdraw.getId()));
                        Time.sleepTick();
                        return Result.FAILURE;
                    }

                    Reaction.REGULAR.sleep();
                    return Result.IN_PROGRESS;
                }

                //Wear equipment
                var toWear = activeSet.getSupplies().stream()
                        .filter(Inventory::contains)
                        .filter(s -> !Equipment.contains(s) || s.isStackable())
                        .findFirst();

                if(toWear.isPresent()) {

                    var item = Bank.Inventory.getFirst(toWear.get());

                    if(item == null) {
                        log.warn("Unable to find item to wear");
                        Time.sleepTick();
                        return Result.FAILURE;
                    }

                    if(!equipItems(toWear.get(), item))  {
                        return Result.FAILURE;
                    }

                    return Result.IN_PROGRESS;
                }

                //Force out other item to make space
                var toDeposit = Inventory.getAll().stream()
                        .filter(i -> !activeSet.containsItem(i.getId()))
                        .max(Comparator.comparingInt(Item::getQuantity))
                        .get();

                log.debug("Depositing {} to make space", toDeposit.getName());
                if(!Bank.depositAll(toDeposit.getId()) || !Time.sleepUntil(() -> !Inventory.contains(toDeposit.getId()), BANK_TIMEOUT)) {
                    log.warn("Failed to deposit {}", toDeposit.getName());
                    Time.sleepTick();
                    return Result.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return Result.IN_PROGRESS;
            }
        }

        //Withdraw supplies
        var remainingSupplies = Stream.concat(
            loadout.getEquipmentSets().stream().filter(s -> s != activeSet).flatMap(s -> s.getSupplies().stream()),
            loadout.getInventorySupplies().stream()
        ).toList();

        for(var supply : remainingSupplies) {

            var currentCount = supply.getCurrentCount();

            if(currentCount >= supply.getMaximumAmount()) {
                continue;
            }

            var bankCount = Bank.getCount(true, supply.getItemIds());

            if(bankCount == 0) {
                continue;
            }

            var comparator = Comparator.<Item>comparingInt(i -> supply.getItemIds().indexOf(i.getId()));

            if(supply.isMinimumDose()) {
                comparator = comparator.reversed();
            }

            var toWithdraw = Bank.getAll(supply.getItemIds()).stream()
                    .min(comparator)
                    .get();

            var withdrawAmount = Math.min(toWithdraw.getQuantity(), supply.getMaximumAmount() - currentCount);

            var slotsRequired = supply.isStackable() ? (Inventory.contains(toWithdraw.getSlot()) ? 0 : 1) : withdrawAmount;

            if(Inventory.getFreeSlots() >= slotsRequired) {
                log.debug("Withdrawing {}: {}", toWithdraw.getName(), withdrawAmount);
                var withdrawMode = supply instanceof InventorySupply inventorySupply && inventorySupply.isNoted() ? Bank.WithdrawMode.NOTED : Bank.WithdrawMode.ITEM;

                Action.logPerform("WITHDRAW");
                if(!Bank.withdraw(toWithdraw.getId(), withdrawAmount, withdrawMode)) {
                    Action.logFail("WITHDRAW");
                    log.warn("Failed to withdraw: " + toWithdraw.getName());
                    Time.sleepTick();
                    return Result.FAILURE;
                }

                if(!Time.sleepUntil(() -> supply.getCurrentCount() > currentCount, BANK_TIMEOUT)) {
                    Action.logTimeout("WITHDRAW");
                    log.warn("Withdraw timed out: " + toWithdraw.getName());
                    Time.sleepTick();
                    return Result.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return Result.IN_PROGRESS;
            }

            //Wear equipment
            if (activeSet == null) {
                log.warn("Cannot make room in inventory");
                return Result.FAILURE;
            }

            var toWear = activeSet.getSupplies().stream()
                    .filter(Inventory::contains)
                    .filter(s -> !Equipment.contains(s) || s.isStackable())
                    .findFirst();

            if(toWear.isEmpty()) {
                return Result.FAILURE;
            }

            var item = Bank.Inventory.getFirst(toWear.get());

            if(item == null) {
                log.warn("Unable to find item to wear");
                Time.sleepTick();
                return Result.FAILURE;
            }

            if(!equipItems(toWear.get(), item)) {
                return Result.FAILURE;
            }

            return Result.IN_PROGRESS;
        }

        if(activeSet != null) {

            var toWear = activeSet.getSupplies().stream()
                    .filter(Inventory::contains)
                    .filter(s -> !Equipment.contains(s) || s.isStackable())
                    .findFirst();

            if(toWear.isEmpty()) {
                return Result.FAILURE;
            }

            var item = Bank.Inventory.getFirst(toWear.get());

            if(item == null) {
                log.warn("Unable to find item to wear");
                Time.sleepTick();
                return Result.FAILURE;
            }

            if(!equipItems(toWear.get(), item)) {
                return Result.FAILURE;
            }

            return Result.IN_PROGRESS;
        }

        log.warn("This case should not be reachable");
        return Result.FAILURE;
    }

    public static boolean equipItems(Loadout loadout) {
        var activeSet = getActiveSet(loadout);
        return equipItems(loadout, activeSet);
    }

    public static boolean equipItems(Loadout loadout, EquipmentSet equipmentSet) {
        var activeSet = getActiveSet(loadout);

        if(activeSet == null) {
            return false;
        }

        //Equip items in the inventory that are part of the active set
        for(var equipmentSupply : activeSet.getSupplies()) {

            var item = Inventory.getFirst(equipmentSupply);

            if(item == null) {
                continue;
            }

            if(Equipment.contains(equipmentSupply) && !item.isStackable()) {
                log.trace("Already have {} equipped", item.getName());
                continue;
            }

            return equipItems(equipmentSupply, item);
        }

        return false;
    }

    private static boolean equipItems(EquipmentSupply equipmentSupply, Item item) {

        log.debug("Equipping {}", equipmentSupply);
        if(!item.interact("Wear", "Equip", "Wield") || !Time.sleepUntil(() -> !Inventory.contains(equipmentSupply), EQUIPMENT_TIMEOUT)) {
            log.warn("Failed to equip {}", equipmentSupply);
            return false;
        }

        Reaction.PREDICTABLE.sleep();
        return true;
    }

    private static boolean isDone(Loadout loadout, EquipmentSet activeSet) {

        if(!Bank.isCached()) {
            return false;
        }

        var supplies = Stream.concat(
          loadout.getInventorySupplies().stream(),
          loadout.getEquipmentSets().stream().flatMap(s -> s.getSupplies().stream())
        ).toList();

        for(var supply : supplies) {

            var currentCount = supply.getCurrentCount();
            if(currentCount == supply.getMaximumAmount()) {
                continue;
            }

            if(currentCount < supply.getMinimumAmount()) {
                return false;
            }

            if(Bank.contains(supply)) {
                log.trace("Not done, more {} to withdraw", supply);
                return false;
            }
        }

        if(activeSet != null) {
            for(var equipmentSupply : activeSet.getSupplies()) {

                if(!Inventory.contains(equipmentSupply)) {
                    continue;
                }

                return false;
            }
        }

        return !Inventory.contains(i -> !loadout.test(i)) && !Equipment.contains(i -> !loadout.test(i));
    }

    public enum Result {
        IN_PROGRESS,
        SUCCESS,
        FAILURE,
        EXHAUSTED
    }
}