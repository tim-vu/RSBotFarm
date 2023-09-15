package net.rlbot.api.adapter.scene;

import lombok.NonNull;
import net.rlbot.api.movement.position.Area;
import net.runelite.api.coords.WorldPoint;

public class GameObject extends SceneObject {

    private final net.runelite.api.GameObject gameObject;

    protected GameObject(@NonNull net.runelite.api.GameObject gameObject) {
        super(gameObject);
        this.gameObject = gameObject;
    }

    public int getOrientation() {
        return this.gameObject.getOrientation();
    }

    public Area getArea() {

        var sceneMin = gameObject.getSceneMinLocation();
        var sceneMax = gameObject.getSceneMaxLocation();

        var worldMin = WorldPoint.fromScene(API_CONTEXT.getClient(), sceneMin.getX(), sceneMin.getY(), API_CONTEXT.getClient().getPlane());
        var worldMax = WorldPoint.fromScene(API_CONTEXT.getClient(), sceneMax.getX(), sceneMax.getY(), API_CONTEXT.getClient().getPlane());

        return Area.rectangular(worldMin.getX(), worldMax.getY(), worldMax.getX(), worldMin.getY());
    }

}
