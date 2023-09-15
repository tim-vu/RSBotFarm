package net.rlbot.api.movement.pathfinder.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.model.requirement.Requirements;

import java.util.function.BooleanSupplier;

@Value
@AllArgsConstructor
public class Transport
{
    Position source;
    Position destination;
    int sourceRadius;
    int destinationRadius;
    BooleanSupplier handler;
    Requirements requirements;

    public Transport(Position source,
              Position destination,
              int sourceRadius,
              int destinationRadius,
              BooleanSupplier handler
    )
    {
        this.source = source;
        this.destination = destination;
        this.sourceRadius = sourceRadius;
        this.destinationRadius = destinationRadius;
        this.handler = handler;
        this.requirements = new Requirements();
    }
}
