package net.rlbot.api.adapter.common;


import net.rlbot.api.movement.Position;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.scene.Players;

public interface Positionable {

    int getX();

    int getY();

    int getPlane();

    Position getPosition();

    default double distanceTo(Positionable to, Distance distance) {
        return distance.evaluate(this, to);
    }

    default double distanceTo(Positionable to) {
        return Distance.EUCLIDEAN.evaluate(this, to);
    }

    default double distance(Distance distance) {
        return distanceTo(Players.getLocal(), distance);
    }

    default double distance() {
        return distance(Distance.EUCLIDEAN);
    }
}
