package net.rlbot.api.adapter.scene;

import lombok.NonNull;

public class WallObject extends SceneObject{

    private final net.runelite.api.WallObject wallObject;

    protected WallObject(@NonNull net.runelite.api.WallObject wallObject) {
        super(wallObject);
        this.wallObject = wallObject;
    }

    public int getOrientationA() {
        return this.wallObject.getOrientationA();
    }

}
