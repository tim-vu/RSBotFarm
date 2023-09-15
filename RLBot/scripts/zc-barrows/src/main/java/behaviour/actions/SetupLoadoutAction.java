package behaviour.actions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Restocking;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
public class SetupLoadoutAction extends LeafNodeBase {

    private final Supplier<Loadout> loadoutSupplier;

    private final Consumer<Boolean> hasLoadout;

    @Override
    public String getStatus() {
        return "Setting up loadout";
    }

    @Override
    public void execute() {

        var result = LoadoutManager.setup(loadoutSupplier.get());

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
                hasLoadout.accept(false);
            }

            return;
        }

        if(result == LoadoutManager.Result.IN_PROGRESS) {
            hasLoadout.accept(false);
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
                hasLoadout.accept(true);
                return;
            }

            hasLoadout.accept(true);
        }

    }
}
