package net.rlbot.script.api.tree.behaviourtree.decorator;

import net.rlbot.script.api.tree.behaviourtree.BTNode;
import net.rlbot.script.api.tree.behaviourtree.Result;

public class AlwaysSucceed extends DecoratorBase {

    public AlwaysSucceed(BTNode child) {
        super(child);
    }

    @Override
    public Result tick() {
        var result = getChild().tick();

        if(result == Result.IN_PROGRESS) {
            return Result.IN_PROGRESS;
        }

        return Result.SUCCESS;
    }
}
