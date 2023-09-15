package net.rlbot.api.movement.pathfinder.model.requirement;

import java.util.function.BiFunction;

public enum Comparison implements BiFunction<Integer, Integer, Boolean>
{
    LESS_THAN,
    LESS_THAN_EQUAL,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    EQUAL,
    ;

    @Override
    public Boolean apply(Integer value, Integer expected)
    {
        return switch (this) {
            case LESS_THAN -> value < expected;
            case LESS_THAN_EQUAL -> value <= expected;
            case GREATER_THAN -> value > expected;
            case GREATER_THAN_EQUAL -> value >= expected;
            case EQUAL -> value.equals(expected);
        };
    }
}
