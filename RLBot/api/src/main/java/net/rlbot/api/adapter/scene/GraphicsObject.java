package net.rlbot.api.adapter.scene;

import net.rlbot.api.Game;
import net.rlbot.api.adapter.common.Identifiable;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;
import net.rlbot.internal.ApiContext;
import net.runelite.api.coords.WorldPoint;

import java.util.Objects;

public class GraphicsObject implements Identifiable, Positionable {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private final net.runelite.api.GraphicsObject graphicsObject;
    private final Position position;

    public GraphicsObject(net.runelite.api.GraphicsObject graphicsObject) {
        this.graphicsObject = graphicsObject;
        var location = graphicsObject.getLocation();
        this.position = Position.fromLocal(location.getX(), location.getY(), Game.getPlane());
    }

    @Override
    public int getId() {
        return graphicsObject.getId();
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getPlane() {
        return position.getPlane();
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphicsObject that = (GraphicsObject) o;
        return Objects.equals(graphicsObject, that.graphicsObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphicsObject);
    }
}
