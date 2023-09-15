package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.AnimationChangedEvent;

public interface AnimationChangedListener extends EventListener {

    void notify(AnimationChangedEvent event);

}
