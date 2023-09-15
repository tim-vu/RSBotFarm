package equipment;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.reaction.Reaction;

import java.util.List;

@Slf4j
public class BarrowsArmourPiece extends RechargeableEquipment {

    private static final Area BOBS_SHOP = Area.rectangular(3228, 3205, 3233, 3201);

    public BarrowsArmourPiece(List<Integer> itemIds, int repairCost) {
        super(itemIds, getLoadout(itemIds, repairCost), BOBS_SHOP);
    }

    private static Loadout getLoadout(List<Integer> itemIds, int repairCost) {
        return Loadout.builder()
                .withItem(itemIds).build()
                .withItem(ItemId.COINS_995).amount(repairCost).build()
                .build();
    }

    @Override
    public ActionResult recharge() {

        if(isDone()) {
            return ActionResult.SUCCESS;
        }

        var options = Dialog.getOptions();

        if(Dialog.canContinue()) {

            Action.logPerform("CONTINUE_DIALOGUE");
            if(!Dialog.continueSpace()) {
                Action.logFail("CONTINUE_DIALOGUE");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!Dialog.isOpen()) {
                Reaction.REGULAR.sleep();

                if(isDone()) {
                    return ActionResult.SUCCESS;
                }

                return ActionResult.IN_PROGRESS;
            }

            Reaction.PREDICTABLE.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(options.size() > 0) {

            var response = options.size() - 1;

            Action.logPerform("CHOOSE_OPTION");
            if(!Dialog.chooseOption(response)) {
                Action.logFail("CHOOSE_OPTION");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.PREDICTABLE.sleep();
            return ActionResult.IN_PROGRESS;
        }

        var bob = Npcs.getNearest("Bob");

        if(bob == null) {
            log.warn("Unable to find bob");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        Action.logPerform("TALK_TO_BOB");
        if(!bob.interact("Repair")) {
            Action.logFail("TALK_TO_BOB");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        if(!Time.sleepUntil(Dialog::isOpen, () -> Players.getLocal().isMoving(), 1800)) {
            Action.logTimeout("TALK_TO_BOB");
            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.IN_PROGRESS;
    }

    private boolean isDone() {

        if(!needsRecharging()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean needsRecharging() {
        var brokenItemId = getItemIds().get(getItemIds().size() - 1);
        return Inventory.contains(brokenItemId) || Equipment.contains(brokenItemId);
    }
}
