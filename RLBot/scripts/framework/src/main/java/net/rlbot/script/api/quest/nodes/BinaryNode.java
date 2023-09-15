package net.rlbot.script.api.quest.nodes;

/**
 * The base class of a quest node which has 2 successors
 */
public abstract class BinaryNode implements QuestNode {

    private QuestNode leftNode;

    private QuestNode rightNode;

    private final String status;

    public BinaryNode(String status) {

        this.status = status;
    }

    private QuestNode getLeftNode() {

        return leftNode;
    }

    private void setLeftNode(QuestNode node) {

        this.leftNode = node;
    }

    private QuestNode getRightNode() {

        return rightNode;
    }

    private void setRightNode(QuestNode node) {

        this.rightNode = node;
    }

    @Override
    public String getStatus() {

        return status;
    }

    protected abstract Result doExecute();

    /**
     * Executes this node and returns the next node which should be executed
     *
     * @return The next node to execute
     */
    @Override
    public QuestNode execute() {

        //execute the step
        Result result = doExecute();

        if (result == Result.LEFT)
            return getLeftNode();

        if (result == Result.RIGHT)
            return getRightNode();

        //this step of the quest failed for some reason, returning self
        return this;
    }

    public QuestNode nextLeft(QuestNode questNode) {

        setLeftNode(questNode);

        return questNode;
    }

    public QuestNode nextRight(QuestNode questNode) {

        setRightNode(questNode);

        return questNode;
    }

    public enum Result {
        LEFT,
        RIGHT,
        FAILURE
    }

}
