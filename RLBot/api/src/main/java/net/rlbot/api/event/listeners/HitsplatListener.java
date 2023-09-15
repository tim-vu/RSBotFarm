package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.HitsplatEvent;

public interface HitsplatListener extends EventListener {

    void notify(HitsplatEvent event);

}
