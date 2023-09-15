package behaviour.actions;

import data.Areas;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.Teleportation;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class TeleportToBarrowsAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Teleporting to barrows";
    }

    @Override
    public void execute() {

        Action.logPerform("TELEPORT_TO_BARROWS");
        if(!Teleportation.useTablet(ItemId.BARROWS_TELEPORT, Areas.BARROWS)) {
            Action.logFail("TELEPORT_TO_BARROWS");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
