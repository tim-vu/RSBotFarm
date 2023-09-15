package net.rlbot.api.movement.position;

import lombok.Getter;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;

import java.util.ArrayList;
import java.util.List;

public class RectangularArea extends Area {

    @Getter
    private final int x;

    @Getter
    private final int y;

    @Getter
    private final int width;

    @Getter
    private final int height;

    @Getter
    private final int plane;

    @Getter
    private final Position center;

    protected RectangularArea(Positionable northWest, Positionable southEast) {
        this(northWest.getX(),
                northWest.getY(),
                southEast.getX(),
                southEast.getY(),
                northWest.getPlane());

        if(northWest.getPlane() != southEast.getPlane()) {
            throw new IllegalArgumentException("Positionables must be on the same plane");
        }
    }

    protected RectangularArea(int northWestX, int northWestY, int southEastX, int southEastY, int plane) {
        this.x = northWestX;
        this.y = southEastY;
        this.width = southEastX - northWestX;
        this.height = northWestY - southEastY;
        this.plane = plane;
        this.center = new Position(x + width / 2, y + height / 2, plane);
    }

    @Override
    public boolean contains(Positionable positionable) {
        var location = positionable.getPosition();
        return location.getX() >= x &&
                location.getX() <= x + width &&
                location.getY() >= y &&
                location.getY() <= y + height &&
                this.plane == location.getPlane();
    }

    @Override
    public List<Position> getPositions() {

        var result = new ArrayList<Position>();

        for(var x = this.x; x <= this.x + this.width; x++) {
            for(var y = this.y; y <= this.y + height; y++) {
                result.add(new Position(x, y, plane));
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

        RectangularArea that = (RectangularArea) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (width != that.width) return false;
        if (height != that.height) return false;
        return plane == that.plane;
    }

    @Override
    public int hashCode() {

        int result = x;
        result = 31 * result + y;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + plane;
        return result;
    }

}
