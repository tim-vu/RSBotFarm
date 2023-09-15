package tasks.prayer.gildedaltar.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.BankLocation;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Restocking;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.prayer.gildedaltar.data.Keys;

@Slf4j
public class SetupLoadoutAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Setting up loadout";
    }

    @Override
    public void execute() {

        var bankLocation = BankLocation.getNearest();

        if(bankLocation.getArea().getPosition().distance() > 8){
            Movement.walkTo(bankLocation.getArea());
            return;
        }

        var result = LoadoutManager.setup(getBlackboard().get(Keys.LOADOUT));

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
                getBlackboard().get(Keys.HAS_LOADOUT).set(false);
            }

            return;
        }

        if(result == LoadoutManager.Result.IN_PROGRESS) {
            getBlackboard().get(Keys.HAS_LOADOUT).set(false);
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
                getBlackboard().get(Keys.HAS_LOADOUT).set(true);
                return;
            }

            getBlackboard().get(Keys.HAS_LOADOUT).set(true);
        }
    }
}
