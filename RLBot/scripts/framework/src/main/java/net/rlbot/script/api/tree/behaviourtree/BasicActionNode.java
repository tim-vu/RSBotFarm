package net.rlbot.script.api.tree.behaviourtree;

public abstract class BasicActionNode extends BlackboardNode implements ActionNode {

    protected abstract boolean execute();

    @Override
    public Result tick() {
        return execute() ? Result.SUCCESS : Result.FAILURE;
    }
}
