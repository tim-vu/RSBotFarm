package net.rlbot.api.movement.pathfinder.model.dto;

import lombok.Value;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.TransportLoader;
import net.rlbot.api.movement.pathfinder.model.Transport;
import net.rlbot.api.movement.pathfinder.model.requirement.Requirements;

@Value
public class TransportDto
{
    Position source;
    Position destination;
    String action;
    Integer objectId;
    Requirements requirements;

    public Transport toTransport()
    {
        return TransportLoader.objectTransport(source, destination, objectId, action, requirements);
    }
}
