package net.rlbot.script.api.tree.behaviourtree.condition;

import lombok.Getter;
import net.rlbot.script.api.tree.behaviourtree.ParentNode;
import net.rlbot.script.api.tree.behaviourtree.Result;

public class AndNode implements ConditionNode, ParentNode {

    @Getter
    private final ConditionNode[] children;

    public AndNode(ConditionNode... children) {
        this.children = children;
    }

    @Override
    public Result tick() {

        for(var child : this.children) {

            if(child.tick() == Result.SUCCESS) {
                continue;
            }

            return Result.FAILURE;
        }

        return Result.SUCCESS;
    }
}
