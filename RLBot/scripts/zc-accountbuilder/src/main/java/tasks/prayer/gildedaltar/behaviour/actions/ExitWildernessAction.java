package tasks.prayer.gildedaltar.behaviour.actions;

import net.rlbot.api.Game;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.Teleportation;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.prayer.gildedaltar.data.Constants;

public class ExitWildernessAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Exiting the wilderness";
    }

    @Override
    public void execute() {

        if(Game.getWildyLevel() > 30){
            Movement.walkTo(Constants.RUINS);
            return;
        }

        Teleportation.useTeleport(ItemIds.RING_OF_WEALTH, "Grand Exchange", Areas.GRAND_EXCHANGE);
    }
}
