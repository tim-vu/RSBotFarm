package net.rlbot.script.api.quest.nodes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Represents a concrete quest step which consists of an action, a precondition, a condition and a time. This node always has one successor.
 * This class can be used to build {@link UnaryNode}s which consist of simple, common actions. The precondition is checked before the action is executed and if true the node will be skipped.
 * After the action is executed it will wait for the condition to become true. If that happens within the given timeout this node is completed successfully.
 */
@Slf4j
public class AnonUnaryNode extends UnaryNode {

    private static final int DEFAULT_TIMEOUT = 3000;

    @Getter
    private final BooleanSupplier action;

    @Getter
    private final BooleanSupplier condition;

    @Getter
    private final BooleanSupplier reset;

    @Getter
    private final BooleanSupplier precondition;

    @Getter
    private final int timeout;

    @Getter
    private final int wait;

    private AnonUnaryNode(Builder builder) {

        super(builder.status);

        if (builder.action == null) {
            throw new NullPointerException("action cannot be null");
        }

        if (builder.condition == null) {
            throw new NullPointerException("condition cannot be null");
        }

        if (builder.timeout < 0) {
            throw new IllegalArgumentException("timeout must be greater than zero");
        }

        if (builder.precondition == null) {
            throw new NullPointerException("precondition must not be null");
        }

        this.action = builder.action;
        this.condition = builder.condition;
        this.reset = builder.reset;
        this.precondition = builder.precondition;
        this.timeout = builder.timeout;
        this.wait = builder.wait;
    }

    /**
     * Creates a new builder with the given action
     *
     * @param action The action
     * @return The new builder
     */
    public static Builder builder(BooleanSupplier action) {

        return new Builder(action);
    }

    @Override
    protected ActionResult doExecute() {

        //check if the precondition is already true
        if (getPrecondition().getAsBoolean()) {
            return ActionResult.SUCCESS;
        }

        //execute the action
        boolean result = getAction().getAsBoolean();

        if (!result) {
            log.warn("Failed to execute action");
            return ActionResult.FAILURE;
        }

        //wait for the condition to become true
        if (!Time.sleepUntil(getCondition(), getReset(), getTimeout())) {
            log.warn("Condition timed out");
            return ActionResult.FAILURE;
        }

        Time.sleep(getWait());

        //this step executed succesfully
        return ActionResult.SUCCESS;
    }

    public static UnaryNode sequence(AnonUnaryNode... nodes)
    {
        return UnaryNode.sequence(nodes[nodes.length - 1].getPrecondition(), nodes);
    }
    public static class Builder {

        private int timeout = DEFAULT_TIMEOUT;

        private final BooleanSupplier action;

        private BooleanSupplier reset = () -> false;

        private BooleanSupplier condition = () -> true;

        private boolean usePrecondition = false;

        private BooleanSupplier precondition = null;

        private String status;

        private int wait;

        public Builder(BooleanSupplier action) {
            this.action = action;
        }

        public Builder reset(BooleanSupplier reset) {
            this.reset = reset;
            return this;
        }

        public Builder condition(BooleanSupplier condition) {

            this.condition = condition;
            return this;
        }

        public Builder timeout(int timeout) {

            this.timeout = timeout;
            return this;
        }

        public Builder status(String status) {

            this.status = status;
            return this;
        }

        public Builder usePrecondition() {

            this.usePrecondition = true;
            return this;
        }

        public Builder usePrecondition(BooleanSupplier precondition) {

            this.precondition = precondition;
            return this;
        }

        public Builder wait(int delay) {

            this.wait = delay;
            return this;
        }

        public AnonUnaryNode build() {

            if (usePrecondition && precondition == null)
                precondition = condition;
            else if (!usePrecondition && precondition == null)
                precondition = () -> false;

            return new AnonUnaryNode(this);
        }

    }

}
