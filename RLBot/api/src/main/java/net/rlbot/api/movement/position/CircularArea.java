package net.rlbot.api.movement.position;

import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class CircularArea extends Area {

    private final Position center;

    private final int radius;

    private final Ellipse2D.Double circle;

    public CircularArea(Position center, int radius) {
        this.center = center;
        this.radius = radius;
        this.circle = new Ellipse2D.Double(center.getX() - radius / 2d, center.getY() - radius / 2d, radius, radius);
    }

    @Override
    public boolean contains(Positionable positionable) {
        var position = positionable.getPosition();
        return circle.contains(position.getX(), position.getY());
    }

    @Override
    public Position getCenter() {
        return center;
    }

    @Override
    public List<Position> getPositions() {

        var result = new ArrayList<Position>();

        for(var x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for(var y = center.getY() - radius; y <= center.getY() + radius; y++) {

                if(!circle.contains(x, y)) {
                    continue;
                }

                result.add(new Position(x, y, center.getPlane()));
            }
        }

        return result;
    }

    public int getX() {
        return this.center.getX();
    }

    public int getY() {
        return this.center.getY();
    }

    public int getPlane() {
        return this.center.getPlane();
    }

    @Override
    public Position getPosition() {
        return center;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CircularArea that = (CircularArea) o;

        if (radius != that.radius) return false;
        return center.equals(that.center);
    }

    @Override
    public int hashCode() {

        int result = center.hashCode();
        result = 31 * result + radius;
        return result;
    }

}
