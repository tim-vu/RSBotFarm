package net.rlbot.script.api.task.common.basicactivitytask.behaviour.action;

import net.rlbot.api.common.BankLocation;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class GoToBankAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to the bank";
    }

    @Override
    public void execute() {
        Movement.walkTo(BankLocation.getNearest().getArea());
    }
}