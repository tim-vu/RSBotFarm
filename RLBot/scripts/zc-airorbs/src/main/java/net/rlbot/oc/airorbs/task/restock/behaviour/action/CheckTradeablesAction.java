package net.rlbot.oc.airorbs.task.restock.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.items.Bank;
import net.rlbot.oc.airorbs.task.restock.data.Keys;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Restocking;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class CheckTradeablesAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Checking resources";
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
            return;
        }

        var tradeables = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables();

        if(tradeables.stream().noneMatch(t -> t.needsSelling() || t.needsBuying())){

            Action.logPerform("CLOSE_BANK");
            if(!Bank.close()) {
                Action.logFail("CLOSE_BANK");
                return;
            }

            Reaction.REGULAR.sleep();
            getBlackboard().put(Keys.IS_DONE, true);
            return;
        }

        var result = Restocking.setupRestocking();

        if(result == LoadoutManager.Result.FAILURE || result == LoadoutManager.Result.IN_PROGRESS) {
            return;
        }

        if(result == LoadoutManager.Result.EXHAUSTED) {
            log.error("The player has ran out of supplies, stopping the script");
            return;
        }

        log.info("Enabling restock");
        getBlackboard().put(RestockingKeys.IS_RESTOCKING, true);
    }
}
