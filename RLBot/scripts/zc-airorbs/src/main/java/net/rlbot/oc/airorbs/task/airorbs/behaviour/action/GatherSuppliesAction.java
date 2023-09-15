package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.items.Bank;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.restocking.Restocking;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class GatherSuppliesAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Gathering supplies";
    }

    @Override
    public void execute() {

        var result = LoadoutManager.setup(Constants.LOADOUT);

        if(result == LoadoutManager.Result.EXHAUSTED){

            log.debug("Supplies exhausted, setting up restocking");
            result = Restocking.setupRestocking(true);

            if(result == LoadoutManager.Result.SUCCESS){
                getBlackboard().put(RestockingKeys.IS_RESTOCKING, true);
                log.info("Setup restocking done, closing the bank");
                Bank.close();
                return;
            }

            if(result == LoadoutManager.Result.EXHAUSTED){
                log.error("The player has ran out of supplies, stopping the script");
                getBlackboard().put(RestockingKeys.IS_OUT_OF_COINS, true);
                return;
            }

            if(result == LoadoutManager.Result.IN_PROGRESS) {
                getBlackboard().get(Keys.HAS_LOADOUT).set(false);
            }

            return;
        }

        if(result == LoadoutManager.Result.IN_PROGRESS) {
            getBlackboard().get(Keys.HAS_LOADOUT).set(false);
            return;
        }

        if(result == LoadoutManager.Result.SUCCESS) {
            getBlackboard().get(Keys.HAS_LOADOUT).set(true);
            return;
        }
    }
}
