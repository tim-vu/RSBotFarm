package net.rlbot.script.api.tree.behaviourtree.condition;

import net.rlbot.script.api.tree.behaviourtree.BTNode;
import net.rlbot.script.api.tree.behaviourtree.BlackboardNode;
import net.rlbot.script.api.tree.behaviourtree.ParentNode;
import net.rlbot.script.api.tree.behaviourtree.Result;

public class OrNode extends BlackboardNode implements ConditionNode, ParentNode {

    @Override
    public BTNode[] getChildren() {
        return children;
    }

    private final ConditionNode[] children;

    protected OrNode(ConditionNode... children) {
        this.children = children;
    }

    @Override
    public Result tick() {
        for(var child : this.children) {
            var result = child.tick();

            if(result == Result.SUCCESS) {
                return Result.SUCCESS;
            }
        }

        return Result.FAILURE;
    }
}
