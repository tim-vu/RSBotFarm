package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;


import net.rlbot.api.movement.Movement;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class GoToGrandExchangeAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to the grand exchange";
    }

    @Override
    public void execute() {
        Movement.walkTo(Constants.GRAND_EXCHANGE);
    }
}
