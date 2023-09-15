package net.rlbot.script.api.tree.behaviourtree.condition;

import net.rlbot.script.api.tree.behaviourtree.BTNode;
import net.rlbot.script.api.tree.behaviourtree.ParentNode;
import net.rlbot.script.api.tree.behaviourtree.Result;

public class NotNode implements ConditionNode, ParentNode {

    @Override
    public BTNode[] getChildren() {
        return children;
    }

    private final ConditionNode condition;
    private final BTNode[] children;

    public NotNode(ConditionNode condition) {
        this.condition = condition;
        this.children = new BTNode[] { condition };
    }

    @Override
    public Result tick() {

        var result = this.condition.tick();

        if(result == Result.SUCCESS) {
            return Result.FAILURE;
        }

        return Result.SUCCESS;
    }
}
