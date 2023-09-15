package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;


import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.BankLocation;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class GoToBankAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Going to the bank";
    }

    @Override
    public void execute() {

        if(BankLocation.getNearest().getArea().distance() > 30 && !Constants.FEROX_ENCLAVE.contains(Players.getLocal()) && ItemIds.RING_OF_DUELING.contains(EquipmentSlot.RING.getItemId())){

            Reaction.PREDICTABLE.sleep();

            Action.logPerform("TELEPORT_FEROX_ENCLAVE");
            if(!EquipmentSlot.RING.interact("Ferox Enclave") || !Time.sleepUntil(() -> Constants.FEROX_ENCLAVE.contains(Players.getLocal()), 5000))
            {
                Action.logFail("TELEPORT_FEROX_ENCLAVE");
                return;
            }

            getBlackboard().get(Keys.STATISTICS).incrementDuelingDosesUsed();
            return;
        }

        Movement.walkTo(BankLocation.getNearest().getArea());
    }
}
