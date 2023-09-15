package net.rlbot.script.api.task.common.basicactivitytask.behaviour.action;

import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class GoToTrainingArea extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Going to the training area";
    }

    @Override
    public void execute() {
        Movement.walkTo(getBlackboard().get(BasicActivityKeys.TRAINING_AREA));
    }
}
