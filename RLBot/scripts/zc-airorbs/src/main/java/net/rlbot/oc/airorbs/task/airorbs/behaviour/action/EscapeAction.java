package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.DangerousPlayers;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.component.WorldHopperEx;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class EscapeAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Hopping worlds";
    }

    @Override
    public void execute() {

        var isInCombat = DangerousPlayers.isInCombat(getBlackboard());

        if(!isInCombat){

            var worldRegion = getBlackboard().get(Keys.WORLD_REGION);

            Action.logPerform("HOP_WORLDS");
            if(!WorldHopperEx.randomHopInP2p(worldRegion == null ? Predicates.always() : w -> worldRegion.equals(w.getLocation())))
            {
                Action.logFail("HOP_WORLDS");
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var isTeleblocked = DangerousPlayers.isTeleblocked(getBlackboard());
        if (isTeleblocked || !ItemIds.RING_OF_DUELING.contains(EquipmentSlot.RING.getItemId())) {
            log.debug("No option to escape");
            return;
        }

        log.debug("Teleporting to Ferrox Enclave");
        Action.logPerform("TELEPORT_TO_FEROX_ENCLAVE");
        if(!EquipmentSlot.RING.interact("Ferox Enclave") || !Time.sleepUntil(EscapeAction::isAtFeroxEnclave, 5000))
        {
            Action.logFail("TELEPORT_TO_FEROX_ENCLAVE");
            return;
        }

        log.trace("Set isCharging to false as the player escaped");
        getBlackboard().put(Keys.IS_CHARGING, false);
    }

    private static boolean isAtFeroxEnclave(){
        return Constants.FEROX_ENCLAVE.contains(Players.getLocal());
    }
}
