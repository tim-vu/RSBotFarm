package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.DeathEvent;

public interface DeathListener extends EventListener {

    void notify(DeathEvent event);

}
