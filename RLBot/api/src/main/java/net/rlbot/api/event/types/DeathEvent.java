package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.event.Event;

@AllArgsConstructor
public class DeathEvent implements Event {

    @Getter
    private final Actor actor;

}
