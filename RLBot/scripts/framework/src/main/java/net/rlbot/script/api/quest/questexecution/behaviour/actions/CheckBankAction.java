package net.rlbot.script.api.quest.questexecution.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.quest.questexecution.data.Keys;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Restocking;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class CheckBankAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Checking the bank";
    }

    @Override
    public void execute() {

        if(!Bank.isOpen()) {

            if(!Bank.open()) {
                log.warn("Failed to open the bank");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var tradeables = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables();

        if(tradeables.stream().noneMatch(Tradeable::needsBuying)) {
            log.info("No tradeables need buying, quest can be started");
            getBlackboard().put(Keys.IS_READY, true);
            return;
        }

        var result = Restocking.setupRestocking(true);

        if(result == LoadoutManager.Result.FAILURE) {
            Time.sleepTick();
            return;
        }

        if(result == LoadoutManager.Result.EXHAUSTED) {
            getBlackboard().put(RestockingKeys.IS_OUT_OF_COINS, true);
            return;
        }

        if (result == LoadoutManager.Result.IN_PROGRESS) {
            return;
        }

        if(Bank.isOpen()) {

            log.debug("Closing bank");
            if(!Bank.close()) {
                log.warn("Failed to close the bank");
                Time.sleepTick();
                return;
            }

        }

        Reaction.REGULAR.sleep();
        log.info("Some tradeables need buying, activating restocking");
        getBlackboard().put(RestockingKeys.IS_RESTOCKING, true);
    }
}
