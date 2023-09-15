package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.GraphicChangedEvent;

public interface GraphicChangedListener extends EventListener {

    void notify(GraphicChangedEvent event);

}
