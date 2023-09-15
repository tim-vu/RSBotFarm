package net.rlbot.script.api.tree.behaviourtree.condition;

import net.rlbot.script.api.tree.behaviourtree.BlackboardNode;
import net.rlbot.script.api.tree.behaviourtree.Result;

public abstract class ConditionNodeBase extends BlackboardNode implements ConditionNode {

    protected abstract boolean evaluate();

    @Override
    public Result tick() {
        return evaluate() ? Result.SUCCESS : Result.FAILURE;
    }
}
