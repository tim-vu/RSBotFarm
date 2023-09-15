package net.rlbot.script.api.task.common.basicactivitytask.behaviour.action;

import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class GoToGrandExchange extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to the grand exchange";
    }

    @Override
    public void execute() {
        Movement.walkTo(Areas.GRAND_EXCHANGE);
    }
}
