package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.Inventory;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class WithdrawGoldAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Withdrawing gold";
    }

    @Override
    public void execute() {

        if(!Bank.isOpen()) {

            Action.logPerform("OPEN_BANK");
            if(!Bank.open())
            {
                Action.logFail("OPEN_BANK");
                return;
            }

            Reaction.REGULAR.sleep();
        }

        if(Inventory.getCount(true, ItemId.COINS_995) == Constants.COINS_TO_MULE) {

            Action.logPerform("CLOSE_BANK");
            if(!Bank.close()) {
                Action.logTimeout("CLOSE_BANK");
                return;
            }

            log.info("Coin withdraw complete");
            getBlackboard().put(Keys.HAS_GOLD, true);
            return;
        }

        if(!Inventory.isEmpty()){

            Action.logPerform("DEPOSIT_INVENTORY");
            if(!Bank.depositInventory() || !Time.sleepUntil(Inventory::isEmpty, 2000))
            {
                Action.logFail("DEPOSIT_INVENTORY");
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        Action.logFail("WITHDRAW_COINS");
        if(!Bank.withdraw(ItemId.COINS_995, Constants.COINS_TO_MULE, Bank.WithdrawMode.DEFAULT) || !Time.sleepUntil(() -> Inventory.contains(ItemId.COINS_995), 1800))
        {
            Action.logFail("WITHDRAW_COINS");
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
