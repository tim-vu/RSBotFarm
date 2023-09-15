package net.rlbot.api.adapter.scene;

import lombok.NonNull;

public class DecorativeObject extends SceneObject {

    private final net.runelite.api.DecorativeObject decorativeObject;

    protected DecorativeObject(@NonNull net.runelite.api.DecorativeObject object) {
        super(object);
        this.decorativeObject = object;
    }
}
