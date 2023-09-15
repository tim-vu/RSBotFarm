package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.GraphicsObjectSpawnedEvent;

public interface GraphicsObjectSpawnedListener extends EventListener {

    void notify(GraphicsObjectSpawnedEvent event);

}
