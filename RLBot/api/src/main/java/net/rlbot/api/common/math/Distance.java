package net.rlbot.api.common.math;

import lombok.AllArgsConstructor;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;

import java.util.function.BiFunction;

@AllArgsConstructor
public enum Distance
{
    EUCLIDEAN((f, t) -> Math.hypot(f.getX() - t.getX(), f.getY() - t.getY())),
    CHEBYSHEV((f, t) -> (double)Math.max(Math.abs(f.getX() - t.getX()), Math.abs(f.getY() - t.getY())));


    public double evaluate(Positionable from, Positionable to) {
        return this.evaluate.apply(from.getPosition(), to.getPosition());
    }

    public double evaluate(int x1, int y1, int x2, int y2) {
        return this.evaluate.apply(new Position(x1, y1), new Position(x2, y2));
    }

    private final BiFunction<Position, Position, Double> evaluate;

}
