package net.rlbot.api.movement;


import lombok.EqualsAndHashCode;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.scene.GameObject;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.adapter.scene.WallObject;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.Tiles;
import net.runelite.api.CollisionData;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;

import java.util.*;

public class Reachable
{
    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final int MAX_ATTEMPTED_TILES = 64 * 64;

    public static boolean check(int flag, int checkFlag)
    {
        return (flag & checkFlag) != 0;
    }

    public static boolean isObstacle(int endFlag)
    {
        return check(endFlag, 0x100 | 0x20000 | 0x200000 | 0x1000000);
    }

    public static boolean isObstacle(Position worldPoint)
    {
        return isObstacle(getCollisionFlag(worldPoint));
    }

    public static int getCollisionFlag(Position position)
    {
        CollisionData[] collisionMaps = API_CONTEXT.getClient().getCollisionMaps();
        if (collisionMaps == null)
        {
            return 0xFFFFFF;
        }

        CollisionData collisionData = collisionMaps[Game.getPlane()];
        if (collisionData == null)
        {
            return 0xFFFFFF;
        }

        LocalPoint localPoint = LocalPoint.fromWorld(API_CONTEXT.getClient(), position.getX(), position.getY());
        if (localPoint == null ||
                localPoint.getSceneX() < 0 ||
                localPoint.getSceneY() < 0 ||
                localPoint.getSceneX() >= 104 ||
                localPoint.getSceneY() >= 104)
        {
            return 0xFFFFFF;
        }

        return collisionData.getFlags()[localPoint.getSceneX()][localPoint.getSceneY()];
    }

    public static boolean isWalled(Direction direction, int startFlag)
    {
        return switch (direction) {
            case NORTH -> check(startFlag, 0x2);
            case SOUTH -> check(startFlag, 0x20);
            case WEST -> check(startFlag, 0x80);
            case EAST -> check(startFlag, 0x8);
            default -> throw new IllegalArgumentException();
        };
    }

    public static boolean isWalled(Position source, Position destination)
    {
        var tileA = Tiles.getAt(source);
        var tileB = Tiles.getAt(source);

        if(tileA == null || tileB == null) {
            return false;
        }

        return isWalled(tileA, tileB);
    }

    public static boolean isWalled(Tile source, Tile destination)
    {
        var wall = source.getWallObject();

        if (wall == null)
        {
            return false;
        }

        var a = source.getPosition();
        var b = destination.getPosition();

        return switch (wall.getOrientationA()) {
            case 1 -> a.dx(-1).equals(b) || a.dx(-1).dy(1).equals(b) || a.dx(-1).dy(-1).equals(b);
            case 2 -> a.dy(1).equals(b) || a.dx(-1).dy(1).equals(b) || a.dx(1).dy(1).equals(b);
            case 4 -> a.dx(1).equals(b) || a.dx(1).dy(1).equals(b) || a.dx(1).dy(-1).equals(b);
            case 8 -> a.dy(-1).equals(b) || a.dx(-1).dy(-1).equals(b) || a.dx(-1).dy(1).equals(b);
            default -> false;
        };
    }

    public static boolean hasDoor(Position source, Direction direction)
    {
        Tile tile = Tiles.getAt(source);

        if (tile == null)
        {
            return false;
        }

        return hasDoor(tile, direction);
    }

    public static boolean hasDoor(Tile source, Direction direction)
    {
        var wall = source.getWallObject();

        if (wall == null)
        {
            return false;
        }

        return isWalled(direction, getCollisionFlag(source.getPosition())) && wall.hasAction("Open");
    }

    public static boolean isDoored(Tile source, Tile destination)
    {
        var wall = source.getWallObject();

        if (wall == null)
        {
            return false;
        }

        return isWalled(source, destination) && wall.hasAction("Open");
    }

    public static boolean isDoored(Position source, Position destination) {
        var tileA = Tiles.getAt(source);
        var tileB = Tiles.getAt(destination);

        if(tileA == null || tileB == null) {
            return false;
        }

        var wall = tileA.getWallObject();

        if(wall == null) {
            return false;
        }

        return isWalled(tileA, tileB) && wall.hasAction("Open");
    }

    public static boolean isGameObjectDoor(Position position) {

        var door = SceneObjects.query()
                .locations(position)
                .nameContains("door")
                .actions("Open")
                .results()
                .first();

        return door != null;
    }

    public static boolean canWalk(Direction direction, int startFlag, int endFlag)
    {
        if (isObstacle(endFlag))
        {
            return false;
        }

        return !isWalled(direction, startFlag);
    }

    public static Position getNeighbour(Direction direction, Position source)
    {

        return switch (direction) {
            case NORTH -> source.dy(1);
            case SOUTH -> source.dy(-1);
            case WEST -> source.dx(-1);
            case EAST -> source.dx(1);
            default -> throw new IllegalArgumentException();
        };
    }

    public static List<Position> getNeighbours(Positionable destination, Positionable current)
    {
        List<Position> out = new ArrayList<>();
        Position dest = current instanceof Position ? (Position) current : current.getPosition();

        for (Direction dir : Direction.values())
        {
            Position neighbour = getNeighbour(dir, dest);

            if (!isInScene(neighbour))
            {
                continue;
            }

            boolean containsPoint;
            if (destination instanceof GameObject gameObject)
            {
                containsPoint = gameObject.getArea().contains(neighbour);
            }
            else
            {
                containsPoint = destination.getPosition().equals(neighbour);
            }

            if (containsPoint && (!isWalled(dir, getCollisionFlag(dest)) || destination instanceof WallObject))
            {
                out.add(neighbour);
                continue;
            }

            if (!canWalk(dir, getCollisionFlag(dest), getCollisionFlag(neighbour)))
            {
                continue;
            }

            out.add(neighbour);
        }

        return out;
    }

    public static boolean isReachable(Positionable positionable) {
        return getCost(positionable) != Integer.MAX_VALUE;
    }

    public static boolean isReachable(Position position) {
        return getCost(position) != Integer.MAX_VALUE;
    }

    public static int getCost(Positionable destination) {
        var player = Players.getLocal();

        if (!isInScene(destination.getPosition()))
        {
            return Integer.MAX_VALUE;
        }

        if(player.getPlane() != destination.getPlane()) {
            return Integer.MAX_VALUE;
        }

        var visited = new HashSet<Position>();
        var queue = new PriorityQueue<Node>(Comparator.comparing(node -> node.total));

        var position = player.getPosition();

        if (position.getPlane() != destination.getPlane())
        {
            return Integer.MAX_VALUE;
        }

        queue.add(new Node(position, 0, 0));

        while (!queue.isEmpty())
        {
            var current = queue.poll();

            visited.add(current.position);

            if (current.position.equals(destination))
            {
                return current.cost;
            }

            for(var neighbour : getNeighbours(destination, current.position)) {

                if(visited.contains(neighbour)) {
                    continue;
                }

                var cost = (int)Distance.CHEBYSHEV.evaluate(current.position, neighbour);
                var node = new Node(neighbour, current.cost + cost, getHeuristic(neighbour, destination.getPosition()));

                if(queue.contains(node)) {
                    continue;
                }

                queue.add(node);
            }
        }

        return Integer.MAX_VALUE;
    }

    private static int getHeuristic(Position current, Position destination) {
        return (int)Distance.CHEBYSHEV.evaluate(current, destination);
    }

    private static boolean isInScene(Position position) {
        int baseX = API_CONTEXT.getClient().getBaseX();
        int baseY = API_CONTEXT.getClient().getBaseY();

        int maxX = baseX + Perspective.SCENE_SIZE;
        int maxY = baseY + Perspective.SCENE_SIZE;

        return position.getX() >= baseX && position.getX() < maxX && position.getY() >= baseY && position.getY() < maxY;
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Node {

        @EqualsAndHashCode.Include
        Position position;

        Node parent;

        int cost;

        int heuristic;

        int total;

        public Node(Position position, int cost, int heuristic) {
            this.position = position;
            this.cost = cost;
            this.heuristic = heuristic;
            this.total = cost + heuristic;
        }
    }
}

