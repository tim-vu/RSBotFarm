package net.rlbot.api.movement.pathfinder;

import net.rlbot.api.movement.Direction;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.scene.Tiles;

public class LocalCollisionMap implements CollisionMap
{
	private final boolean blockDoors;

	public LocalCollisionMap(boolean blockDoors)
	{
		this.blockDoors = blockDoors;
	}

	@Override
	public boolean n(int x, int y, int z)
	{
		Position current = new Position(x, y, z);
		if (Reachable.isObstacle(current))
		{
			return false;
		}

		var currentTile = Tiles.getAt(current);
		var destinationTile = Tiles.getAt(current.dy(1));

		if (currentTile != null
				&& destinationTile != null
				&& (Reachable.isDoored(currentTile, destinationTile) || Reachable.isDoored(destinationTile, currentTile))
				&& !blockDoors
		)
		{
			return !Reachable.isObstacle(destinationTile.getPosition());
		}

		return Reachable.canWalk(Direction.NORTH, Reachable.getCollisionFlag(current), Reachable.getCollisionFlag(current.dy(1)));
	}

	@Override
	public boolean e(int x, int y, int z)
	{
		Position current = new Position(x, y, z);
		if (Reachable.isObstacle(current))
		{
			return false;
		}

		var currentTile = Tiles.getAt(current);
		var destinationTile = Tiles.getAt(current.dx(1));

		if (currentTile != null
				&& destinationTile != null
				&& (Reachable.isDoored(currentTile, destinationTile) || Reachable.isDoored(destinationTile, currentTile))
				&& !blockDoors
		)
		{
			return !Reachable.isObstacle(destinationTile.getPosition());
		}

		return Reachable.canWalk(Direction.EAST, Reachable.getCollisionFlag(current), Reachable.getCollisionFlag(current.dx(1)));
	}
}
