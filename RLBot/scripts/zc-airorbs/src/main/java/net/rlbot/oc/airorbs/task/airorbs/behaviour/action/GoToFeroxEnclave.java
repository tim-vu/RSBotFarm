package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.DangerousPlayers;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class GoToFeroxEnclave extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to ferox enclave";
    }

    @Override
    public void execute() {

        var isTeleblocked = DangerousPlayers.isTeleblocked(getBlackboard());

        if(!isTeleblocked && ItemIds.RING_OF_DUELING.contains(EquipmentSlot.RING.getItemId())){

            Action.logPerform("TELEPORT_TO_FEROX_ENCLAVE");
            if(!EquipmentSlot.RING.interact("Ferox Enclave") || !Time.sleepUntil(GoToFeroxEnclave::isAtFeroxEnclave, 5000))
            {
                Action.logFail("TELEPORT_TO_FEROX_ENCLAVE");
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        Movement.walkTo(Constants.FEROX_ENCLAVE);
    }

    private static boolean isAtFeroxEnclave(){
        return Constants.FEROX_ENCLAVE.contains(Players.getLocal());
    }
}
