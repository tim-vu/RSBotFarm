package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;


import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.widgets.WorldHopper;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class GoToMuleAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to the mule";
    }

    @Override
    public void execute() {

        var mulingRequest = getBlackboard().get(Keys.MULING_REQUEST);

        var position = new Position(mulingRequest.getPosition().getX(), mulingRequest.getPosition().getY(), mulingRequest.getPosition().getZ());

        if(position.distance() > 10) {
            Movement.walkTo(position);
            return;
        }

        Action.logPerform("HOP_WORLD");
        if(!WorldHopper.hopTo(mulingRequest.getWorld())) {
            Action.logFail("HOP_WORLD");
            return;
        }
    }
}
