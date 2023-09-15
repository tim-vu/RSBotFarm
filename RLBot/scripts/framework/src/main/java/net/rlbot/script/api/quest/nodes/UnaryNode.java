package net.rlbot.script.api.quest.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;

import java.util.function.BooleanSupplier;

/**
 * The base class of a quest node which has one successor node
 */
@Slf4j
public abstract class UnaryNode implements QuestNode {

    private final String status;

    private QuestNode next;

    public UnaryNode(String status) {

        this.status = status;
    }

    public QuestNode getNext() {

        return next;
    }

    @Override
    public String getStatus() {

        return status;
    }

    protected abstract ActionResult doExecute();

    /**
     * Executes this node and returns the next node which should be executed
     *
     * @return The next node to execute
     */
    @Override
    public QuestNode execute() {

        if (getNext() == null) {
            throw new IllegalStateException("Node next has not been initialized");
        }

        var result = doExecute();

        //execute the step
        if (result == ActionResult.SUCCESS) {
            //this step of the quest has been successfully completed, return the successor
            return getNext();
        }

        if(result == ActionResult.FAILURE) {
            log.trace("UnaryNode {} failed", getStatus());
        }

        //this step of the quest failed for some reason, returning self
        return this;
    }

    /**
     * Sets the successor of this node to the given node
     *
     * @param node The successor
     * @return The new successor of this node
     */
    public UnaryNode next(UnaryNode node) {

        this.next = node;
        return node;
    }

    /**
     * Sets the successor of this node to the given node
     *
     * @param node The successor
     * @return The new successor of this node
     */
    public QuestNode next(QuestNode node) {

        this.next = node;
        return node;
    }

    public static UnaryNode sequence(UnaryNode... nodes) {
        return sequence(() -> false, nodes);
    }

    public static UnaryNode sequence(BooleanSupplier precondition, UnaryNode... nodes){

        return new UnaryNode(null) {

            private int index = 0;

            @Override
            public String getStatus() {
                return nodes[index].getStatus();
            }

            @Override
            protected ActionResult doExecute() {

                if(precondition.getAsBoolean()) {
                    return ActionResult.SUCCESS;
                }

                var result = nodes[index].doExecute();

                if(result == ActionResult.FAILURE) {
                    index = 0;
                    return ActionResult.FAILURE;
                }

                if(result == ActionResult.IN_PROGRESS) {
                    return ActionResult.IN_PROGRESS;
                }

                if(index == nodes.length - 1) {
                    index = 0;
                    return ActionResult.SUCCESS;
                }

                index++;
                return ActionResult.IN_PROGRESS;
            }
        };
    }

    public UnaryNode selector(UnaryNode... nodes) {
        return selector(() -> false, nodes);
    }

    public UnaryNode selector(BooleanSupplier precondition, UnaryNode... nodes) {

        return new UnaryNode(null) {

            private int index = 0;

            @Override
            public String getStatus() {
                return nodes[index].getStatus();
            }

            @Override
            protected ActionResult doExecute() {

                if(precondition.getAsBoolean()) {
                    return ActionResult.SUCCESS;
                }

                var result = nodes[index].doExecute();

                if(result == ActionResult.SUCCESS) {
                    this.index = 0;
                    return ActionResult.SUCCESS;
                }

                if(result == ActionResult.IN_PROGRESS) {
                    return ActionResult.IN_PROGRESS;
                }

                if(this.index == nodes.length - 1) {
                    this.index = 0;
                    return ActionResult.FAILURE;
                }

                this.index++;
                return ActionResult.IN_PROGRESS;
            }
        };

    }
}
