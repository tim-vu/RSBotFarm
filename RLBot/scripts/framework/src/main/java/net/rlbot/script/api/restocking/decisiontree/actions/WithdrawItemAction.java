package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;

import static net.rlbot.script.api.data.ItemId.COINS;

@Slf4j
public class WithdrawItemAction implements Action {

    @Override
    public void execute(Blackboard context) {


        context.put(RestockingKeys.IS_WITHDRAWING, true);

        if(GrandExchange.isOpen()) {

            if(!GrandExchange.close()) {
                log.warn("Failed to close the GrandExchange");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        if(!Bank.isOpen()) {

            if(!Bank.open()) {
                log.warn("Failed to open the bank");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        if (Bank.contains(COINS)) {

            if(!Bank.withdrawAll(COINS, Bank.WithdrawMode.DEFAULT))
            {
                log.warn("Failed to withdraw coins");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var tradeables = context.get(RestockingKeys.SETTINGS).getTradeables();
        var toWithdraw = tradeables.stream()
                .filter(Tradeable::needsSelling)
                .filter(t -> getInventoryQuantity(t) < t.getCurrentAmount() - t.getRestockAmount())
                .findFirst()
                .orElse(null);


        if(toWithdraw != null && !Inventory.isFull()) {

            var inventoryCount = getInventoryQuantity(toWithdraw);
            var withdrawAmount = toWithdraw.getCurrentAmount() - toWithdraw.getRestockAmount() - inventoryCount;

            var withdrawMode = ItemDefinition.isStackable(toWithdraw.getItemId()) ? Bank.WithdrawMode.DEFAULT : Bank.WithdrawMode.NOTED;

            if(!Bank.withdraw(toWithdraw.getItemId(), withdrawAmount, withdrawMode)) {
                log.warn("Failed to withdraw " + toWithdraw);
                Time.sleepTick();
                return;
            }

            if(!Time.sleepUntil(() -> getInventoryQuantity(toWithdraw) > inventoryCount, 2400)) {
                log.warn("Failed to withdraw " + toWithdraw);
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        log.debug("Closing bank");
        if(!Bank.close()) {
            log.warn("Failed to close the bank");
            Time.sleepTick();
            return;
        }

        context.put(RestockingKeys.IS_WITHDRAWING, false);
    }

    private static int getInventoryQuantity(Tradeable tradeable) {
        return Inventory.getCount(true, tradeable.getItemId()) + Inventory.getCount(true, ItemDefinition.getNotedId(tradeable.getItemId()));
    }
}
