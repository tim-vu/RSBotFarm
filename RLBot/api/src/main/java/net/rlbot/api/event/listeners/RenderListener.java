package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.RenderEvent;

public interface RenderListener extends EventListener {

    void notify(RenderEvent event);

}
