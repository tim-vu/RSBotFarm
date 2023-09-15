package net.rlbot.script.api.tree.behaviourtree.decorator;

import lombok.Getter;
import net.rlbot.script.api.tree.behaviourtree.BTNode;
import net.rlbot.script.api.tree.behaviourtree.CompositeNode;
import net.rlbot.script.api.tree.behaviourtree.Result;
import net.rlbot.script.api.tree.behaviourtree.condition.ConditionNode;

public class RepeatUntil extends DecoratorBase {

    @Getter
    private final ConditionNode condition;

    public RepeatUntil(ConditionNode condition, BTNode child) {
        super(child);
        this.condition = condition;
    }

    @Override
    public Result tick() {

        var result = getChild().tick();

        if(result == Result.FAILURE) {
            return Result.FAILURE;
        }

        if(condition.tick() == Result.SUCCESS) {

            if(getChild() instanceof CompositeNode compositeNode) {
                compositeNode.reset();
            }

            return Result.SUCCESS;
        }

        return Result.IN_PROGRESS;
    }
}
