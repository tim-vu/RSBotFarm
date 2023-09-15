package net.rlbot.script.api.task.common.basicactivitytask.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Restocking;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class SetupLoadoutAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void execute() {

        var result = LoadoutManager.setup(getBlackboard().get(BasicActivityKeys.LOADOUT));

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
                return;
            }

            if(result == LoadoutManager.Result.IN_PROGRESS) {
                getBlackboard().get(BasicActivityKeys.HAS_LOADOUT).set(false);
            }

            return;
        }

        if(result == LoadoutManager.Result.IN_PROGRESS) {
            getBlackboard().get(BasicActivityKeys.HAS_LOADOUT).set(false);
            return;
        }

        if(result == LoadoutManager.Result.SUCCESS) {

            if(Bank.isOpen()) {

                log.debug("Closing the bank");
                if(!Bank.close()) {
                    log.warn("Failed to close the bank");
                    Time.sleepTick();
                    return;
                }

                Reaction.REGULAR.sleep();
                getBlackboard().get(BasicActivityKeys.HAS_LOADOUT).set(true);
                return;
            }

            getBlackboard().get(BasicActivityKeys.HAS_LOADOUT).set(true);
        }
    }

}
