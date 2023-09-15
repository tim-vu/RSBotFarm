package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.DangerousPlayers;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class StartChargingAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to edgeville";
    }

    @Override
    public void execute() {

        var isTeleblocked = DangerousPlayers.isTeleblocked(getBlackboard());

        if(!Constants.EDGEVILLE.contains(Players.getLocal()) &&
                ItemIds.AMULET_OF_GLORY.contains(EquipmentSlot.AMULET.getItemId()) &&
                !isTeleblocked){

            Action.logPerform("TELEPORT_TO_EDGEVILLE");
            if(!EquipmentSlot.AMULET.interact("Edgeville") || !Time.sleepUntil(() -> Constants.EDGEVILLE.contains(Players.getLocal()) && !Game.isLoadingRegion(), 6000))
            {
                Action.logFail("TELEPORT_TO_EDGEVILLE");
                return;
            }

            getBlackboard().get(Keys.STATISTICS).incrementGloryDosesUsed();
            getBlackboard().put(Keys.IS_CHARGING, true);
            return;
        }

        getBlackboard().put(Keys.IS_CHARGING, true);
    }
}
