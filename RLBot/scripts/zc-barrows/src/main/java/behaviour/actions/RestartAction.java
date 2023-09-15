package behaviour.actions;

import net.rlbot.api.common.Time;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.Teleportation;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class RestartAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Restarting";
    }

    @Override
    public void execute() {

        Action.logPerform("TELEPORT_TO_FEROX_ENCLAVE");
        if(!Teleportation.useTeleport(ItemIds.RING_OF_DUELING, "Ferox Enclave", Areas.FEROX_ENCLAVE)) {
            Action.logFail("TELEPORT_TO_FEROX_ENCLAVE");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
