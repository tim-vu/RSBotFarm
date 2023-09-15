package behaviour.actions;

import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tunnels.Tunnels;

public class GoToChestRoomAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Going to the chest room";
    }

    @Override
    public void execute() {
        Movement.walkTo(Tunnels.CHEST_ROOM);
    }
}
