package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.event.Event;

@AllArgsConstructor
@Value
public class PickableSpawnedEvent implements Event {

    @Getter
    Pickable pickable;

}
