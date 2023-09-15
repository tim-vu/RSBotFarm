package net.rlbot.script.api.task.stopcondition;

import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface StopCondition extends BooleanSupplier {

    default StopCondition and(StopCondition stopCondition) {
        return () -> this.getAsBoolean() && stopCondition.getAsBoolean();
    }

    default StopCondition or(StopCondition stopCondition) {
        return () -> this.getAsBoolean() || stopCondition.getAsBoolean();
    }
}
