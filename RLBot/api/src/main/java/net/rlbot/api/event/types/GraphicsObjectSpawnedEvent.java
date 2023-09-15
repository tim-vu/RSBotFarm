package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.scene.GraphicsObject;
import net.rlbot.api.event.Event;

@AllArgsConstructor
public class GraphicsObjectSpawnedEvent implements Event {

    @Getter
    private final GraphicsObject graphicsObject;

}
