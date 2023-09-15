package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.event.Event;

@AllArgsConstructor
@Value
public class AnimationChangedEvent implements Event {

    Actor actor;

}
