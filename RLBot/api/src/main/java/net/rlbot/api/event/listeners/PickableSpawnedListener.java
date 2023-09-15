package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.PickableSpawnedEvent;

public interface PickableSpawnedListener extends EventListener {

    void notify(PickableSpawnedEvent event);

}
