package net.rlbot.script.api.tree.decisiontree;

public class AnonLeafNode extends LeafNodeBase {

    private final String status;

    private final Action action;

    public AnonLeafNode(Action action, String status) {

        this.action = action;
        this.status = status;
    }

    public AnonLeafNode(Runnable action, String status) {

        this.action = (context) -> action.run();
        this.status = status;
    }

    @Override
    public String getStatus() {

        return status;
    }

    @Override
    public void execute() {
        action.execute(getBlackboard());
    }

}
