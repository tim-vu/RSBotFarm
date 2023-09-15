package net.rlbot.api.movement.pathfinder.model;

import lombok.Value;
import net.rlbot.api.movement.Position;

import java.util.function.BooleanSupplier;

@Value
public class Teleport
{
	Position destination;
	BooleanSupplier handler;
}
