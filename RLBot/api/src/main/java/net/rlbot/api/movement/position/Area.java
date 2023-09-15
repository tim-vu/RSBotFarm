package net.rlbot.api.movement.position;

import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;

import java.util.List;

public abstract class Area implements Positionable {

    public static RectangularArea rectangular(Positionable northWest, Positionable southEast) {
        return new RectangularArea(northWest, southEast);
    }

    public static RectangularArea rectangular(int northWestX, int northWestY, int southEastX, int southEastY, int plane) {
        return new RectangularArea(northWestX, northWestY, southEastX, southEastY, plane);
    }

    public static RectangularArea rectangular(int northWestX, int northWestY, int southEastX, int southEastY) {
        return rectangular(northWestX, northWestY, southEastX, southEastY, 0);
    }

    //TODO: Remove factory
    public static RectangularArea rectangular2(int x, int y, int width, int height, int plane) {
        return rectangular(x, y + height, x + width, y, plane);
    }

    public static CircularArea surrounding(Positionable center, int radius) {
        return new CircularArea(center.getPosition(), radius);
    }

    public static PolygonalArea polygonal(Positionable... positionables) {
        return new PolygonalArea(positionables);
    }

    public static RectangularArea singular(Positionable positionable) {
        return new RectangularArea(positionable, positionable);
    }

    public abstract boolean contains(Positionable positionable);

    public abstract Position getCenter();

    public abstract List<Position> getPositions();

}
