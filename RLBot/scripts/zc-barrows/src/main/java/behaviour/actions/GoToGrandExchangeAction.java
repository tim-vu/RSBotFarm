package behaviour.actions;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.Teleportation;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class GoToGrandExchangeAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Going to the grand exchange";
    }

    @Override
    public void execute() {

        var ring = Inventory.getFirst(ItemIds.RING_OF_WEALTH);

        if(ring != null) {

            Action.logPerform("TELEPORT_TO_GRANDEXCHANGE");
            if (!Teleportation.useTeleport(ItemIds.RING_OF_WEALTH, "Grand Exchange", Areas.GRAND_EXCHANGE)) {
                Action.logFail("TELEPORT_TO_GRANDEXCHANGE");
            }

            Reaction.REGULAR.sleep();
            return;
        }

        Movement.walkTo(Areas.GRAND_EXCHANGE);
    }
}
