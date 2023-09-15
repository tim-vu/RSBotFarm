package net.rlbot.script.api.tree.behaviourtree.decorator;

import lombok.Getter;
import net.rlbot.script.api.tree.behaviourtree.condition.ConditionNode;
import net.rlbot.script.api.tree.behaviourtree.BTNode;
import net.rlbot.script.api.tree.behaviourtree.Result;

public class RepeatWhile extends DecoratorBase {

    @Getter
    private final ConditionNode condition;

    public RepeatWhile(ConditionNode condition, BTNode child) {
        super(child);
        this.condition = condition;
    }

    @Override
    public Result tick() {

        if(condition.tick() != Result.SUCCESS) {
            return Result.SUCCESS;
        }

        var result = getChild().tick();

        if(result == Result.FAILURE) {
            return Result.FAILURE;
        }

        return Result.IN_PROGRESS;
    }
}
