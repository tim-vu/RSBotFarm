package net.rlbot.api.movement.position;

import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonalArea extends Area {

    private final Polygon polygon;
    private final int plane;

    private final Position center;

    public PolygonalArea(Positionable[] positionables) {

        if(positionables.length == 0) {
            throw new IllegalArgumentException("There must be at least 3 positionables");
        }

        if(Arrays.stream(positionables).map(Positionable::getPlane).distinct().count() != 1) {
            throw new IllegalArgumentException("Positionables must be on the same plane");
        }

        this.polygon = new Polygon();

        for(Positionable positionable : positionables) {
            polygon.addPoint(positionable.getX(), positionable.getY());
        }

        this.plane = positionables[0].getPlane();
        this.center = getCenter(positionables, this.plane);
    }

    private static Position getCenter(Positionable[] positionables, int plane) {
        var totalX = 0;
        var totalY = 0;

        for(var positionable : positionables) {
            var position = positionable.getPosition();
            totalX += position.getX();
            totalY += position.getY();
        }

        return new Position(totalX / positionables.length, totalY / positionables.length, plane);
    }

    @Override
    public boolean contains(Positionable positionable) {
        var position = positionable.getPosition();
        var x = position.getX() + 0.5d;
        var y = position.getY() + 0.5d;
        return polygon.contains(x, y) && this.plane == position.getPlane();
    }

    @Override
    public Position getCenter() {
        return this.center;
    }

    @Override
    public List<Position> getPositions() {

        var bounds = polygon.getBounds2D();

        var minX = (int)bounds.getMinX();
        var minY = (int)bounds.getMinY();
        var maxX = (int)Math.ceil(bounds.getMaxX());
        var maxY = (int)Math.ceil(bounds.getMaxY());

        var result = new ArrayList<Position>();

        for(var x = minX; x <= maxX; x++) {
            for(var y = minY; y <= maxY; y++) {

                if(!polygon.contains(x, y)) {
                    continue;
                }

                result.add(new Position(x, y, plane));
            }
        }

        return result;
    }

    public int getX() {
        return this.center.getX();
    }

    public int getY() {
        return this.getCenter().getY();
    }

    public int getPlane() {
        return this.center.getPlane();
    }

    @Override
    public Position getPosition() {
        return this.center;
    }

}
