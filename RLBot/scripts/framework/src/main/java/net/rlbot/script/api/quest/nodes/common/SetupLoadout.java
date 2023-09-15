package net.rlbot.script.api.quest.nodes.common;


import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.BankLocation;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

@Slf4j
public class SetupLoadout extends UnaryNode {

    private final Loadout loadout;

    private final boolean allowExtraItems;

    public SetupLoadout(Loadout loadout, boolean allowExtraItems) {
        super("Gathering supplies");
        this.loadout = loadout;
        this.allowExtraItems = allowExtraItems;
    }

    public SetupLoadout(Loadout loadout) {
        this(loadout, false);
    }

    @Override
    protected ActionResult doExecute() {

        if(LoadoutManager.isLoadoutSetup(loadout, allowExtraItems)) {

            if(Bank.isOpen()) {

                log.debug("Closing bank");
                if(!Bank.close()) {
                    log.warn("Failed to close the bank");
                    Time.sleepTick();
                    return ActionResult.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return ActionResult.SUCCESS;
            }

            return ActionResult.SUCCESS;
        }

        var bankLocation = BankLocation.getNearest();

        if(!bankLocation.getArea().contains(Players.getLocal())) {
            Movement.walkTo(bankLocation.getArea());
            return ActionResult.IN_PROGRESS;
        }

        var result = LoadoutManager.setup(loadout);

        if(result == LoadoutManager.Result.EXHAUSTED) {
            log.error("Failed to setup inventory, this should not be possible");
            return ActionResult.FAILURE;
        }

        return ActionResult.IN_PROGRESS;
    }
}
