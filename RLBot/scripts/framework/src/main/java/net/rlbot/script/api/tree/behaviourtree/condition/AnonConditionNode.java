package net.rlbot.script.api.tree.behaviourtree.condition;

import net.rlbot.script.api.tree.Blackboard;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class AnonConditionNode extends ConditionNodeBase {

    private final Function<Blackboard, Boolean> condition;

    public AnonConditionNode(Function<Blackboard, Boolean> condition) {
        this.condition = condition;
    }

    public AnonConditionNode(BooleanSupplier condition) {
        this.condition = b -> condition.getAsBoolean();
    }

    @Override
    protected boolean evaluate() {
        return condition.apply(getBlackboard());
    }
}
