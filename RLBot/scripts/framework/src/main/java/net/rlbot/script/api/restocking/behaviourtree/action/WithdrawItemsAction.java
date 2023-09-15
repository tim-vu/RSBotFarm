package net.rlbot.script.api.restocking.behaviourtree.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.behaviourtree.ActionNodeBase;
import net.rlbot.script.api.tree.behaviourtree.Result;

import java.util.Set;

import static net.rlbot.script.api.data.ItemId.COINS;

@Slf4j
public class WithdrawItemsAction extends ActionNodeBase {

    @Override
    public Result tick() {

        log.debug("Withdrawing item");

        Set<Tradeable> tradeables = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables();
        var remainingTradeables = tradeables.stream()
                .filter(Tradeable::needsSelling)
                .filter(t -> t.getInventoryAmount() < t.getCurrentAmount() - t.getRestockAmount())
                .findFirst();

        if(Inventory.isFull() || remainingTradeables.isEmpty()) {

            if(Bank.isOpen()) {
                log.debug("Closing the bank");

                if(!Bank.close()) {
                    log.warn("Failed to close the bank");
                    Time.sleepTick();
                    return Result.IN_PROGRESS;
                }

                Reaction.REGULAR.sleep();
                return Result.SUCCESS;
            }

            return Result.SUCCESS;
        }

        if(GrandExchange.isOpen()) {

            if(!GrandExchange.close()) {
                log.warn("Failed to close the GrandExchange");
                Time.sleepTick();
                return Result.IN_PROGRESS;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        if(!Bank.isOpen()) {

            if(!Bank.open()) {
                log.warn("Failed to open the bank");
                Time.sleepTick();
                return Result.IN_PROGRESS;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        if (Bank.contains(COINS)) {

            if(!Bank.withdrawAll(COINS, Bank.WithdrawMode.DEFAULT))
            {
                log.warn("Failed to withdraw coins");
                Time.sleepTick();
                return Result.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        var toWithdraw = remainingTradeables.get();

        var inventoryAmount = toWithdraw.getInventoryAmount();
        var withdrawAmount = toWithdraw.getCurrentAmount() - toWithdraw.getRestockAmount() - inventoryAmount;

        var withdrawMode = ItemDefinition.isStackable(toWithdraw.getItemId()) ? Bank.WithdrawMode.DEFAULT : Bank.WithdrawMode.NOTED;

        if(!Bank.withdraw(toWithdraw.getItemId(), withdrawAmount, withdrawMode)) {
            log.warn("Failed to withdraw " + remainingTradeables);
            Time.sleepTick();
            return Result.IN_PROGRESS;
        }

        if(!Time.sleepUntil(() -> toWithdraw.getInventoryAmount() > inventoryAmount, 2400)) {
            log.warn("Failed to withdraw " + remainingTradeables);
            Time.sleepTick();
            return Result.IN_PROGRESS;
        }

        Reaction.REGULAR.sleep();
        return Result.IN_PROGRESS;
    }
}
