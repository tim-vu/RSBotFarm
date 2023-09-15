package net.rlbot.api.adapter.scene;

import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;
import net.runelite.api.CollisionData;
import net.runelite.api.TileItem;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Positionable {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private final net.runelite.api.Tile tile;

    public Tile(net.runelite.api.Tile tile) {
        this.tile = tile;
    }

    public Point getSceneLocation() {
        var sceneLocation = this.tile.getSceneLocation();
        return new Point(sceneLocation.getX(), sceneLocation.getY());
    }

    public SceneObject[] getSceneObjects() {

        var result = new ArrayList<SceneObject>();

        for (net.runelite.api.GameObject gameObject : tile.getGameObjects()) {

            if(gameObject == null)
            {
                continue;
            }

            result.add(new GameObject(gameObject));
        }

        var wallObject = tile.getWallObject();

        if(wallObject != null) {
            result.add(new WallObject(wallObject));
        }

        var groundObject = tile.getGroundObject();

        if(groundObject != null) {
            result.add(new GroundObject(groundObject));
        }

        var decorativeObject = tile.getDecorativeObject();

        if(decorativeObject != null) {
            result.add(new DecorativeObject(decorativeObject));
        }

        return result.toArray(new SceneObject[0]);
    }

    @Nullable
    public WallObject getWallObject() {

        var wall = this.tile.getWallObject();

        if(wall == null) {
            return null;
        }

        return new WallObject(wall);
    }

    public Pickable[] getPickables() {

        var groundItems = tile.getGroundItems();

        if(groundItems == null) {
            return new Pickable[0];
        }

        var result = new Pickable[groundItems.size()];

        for(var i = 0; i < groundItems.size(); i++){
            result[i] = new Pickable(getPosition(), groundItems.get(i));
        }

        return result;
    }

    public List<Tile> pathTo(Tile other) {
        int z = this.tile.getPlane();
        if (z != other.getPlane())
        {
            return null;
        }

        CollisionData[] collisionData = API_CONTEXT.getClient().getCollisionMaps();
        if (collisionData == null)
        {
            return null;
        }

        int[][] directions = new int[128][128];
        int[][] distances = new int[128][128];
        int[] bufferX = new int[4096];
        int[] bufferY = new int[4096];

        // Initialise directions and distances
        for (int i = 0; i < 128; ++i)
        {
            for (int j = 0; j < 128; ++j)
            {
                directions[i][j] = 0;
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        var p1 = this.getSceneLocation();
        var p2 = other.getSceneLocation();

        int middleX = p1.x;
        int middleY = p1.y;
        int currentX = middleX;
        int currentY = middleY;
        int offsetX = 64;
        int offsetY = 64;
        // Initialise directions and distances for starting tile
        directions[offsetX][offsetY] = 99;
        distances[offsetX][offsetY] = 0;
        int index1 = 0;
        bufferX[0] = currentX;
        int index2 = 1;
        bufferY[0] = currentY;
        int[][] collisionDataFlags = collisionData[z].getFlags();

        boolean isReachable = false;

        while (index1 != index2)
        {
            currentX = bufferX[index1];
            currentY = bufferY[index1];
            index1 = index1 + 1 & 4095;
            // currentX is for the local coordinate while currentMapX is for the index in the directions and distances arrays
            int currentMapX = currentX - middleX + offsetX;
            int currentMapY = currentY - middleY + offsetY;
            if ((currentX == p2.getX()) && (currentY == p2.getY()))
            {
                isReachable = true;
                break;
            }

            int currentDistance = distances[currentMapX][currentMapY] + 1;
            if (currentMapX > 0 && directions[currentMapX - 1][currentMapY] == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0)
            {
                // Able to move 1 tile west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY] = 2;
                distances[currentMapX - 1][currentMapY] = currentDistance;
            }

            if (currentMapX < 127 && directions[currentMapX + 1][currentMapY] == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0)
            {
                // Able to move 1 tile east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY] = 8;
                distances[currentMapX + 1][currentMapY] = currentDistance;
            }

            if (currentMapY > 0 && directions[currentMapX][currentMapY - 1] == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
            {
                // Able to move 1 tile south
                bufferX[index2] = currentX;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX][currentMapY - 1] = 1;
                distances[currentMapX][currentMapY - 1] = currentDistance;
            }

            if (currentMapY < 127 && directions[currentMapX][currentMapY + 1] == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
            {
                // Able to move 1 tile north
                bufferX[index2] = currentX;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX][currentMapY + 1] = 4;
                distances[currentMapX][currentMapY + 1] = currentDistance;
            }

            if (currentMapX > 0 && currentMapY > 0 && directions[currentMapX - 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX - 1][currentY - 1] & 19136782) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
            {
                // Able to move 1 tile south-west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY - 1] = 3;
                distances[currentMapX - 1][currentMapY - 1] = currentDistance;
            }

            if (currentMapX < 127 && currentMapY > 0 && directions[currentMapX + 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX + 1][currentY - 1] & 19136899) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
            {
                // Able to move 1 tile north-west
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY - 1] = 9;
                distances[currentMapX + 1][currentMapY - 1] = currentDistance;
            }

            if (currentMapX > 0 && currentMapY < 127 && directions[currentMapX - 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX - 1][currentY + 1] & 19136824) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
            {
                // Able to move 1 tile south-east
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY + 1] = 6;
                distances[currentMapX - 1][currentMapY + 1] = currentDistance;
            }

            if (currentMapX < 127 && currentMapY < 127 && directions[currentMapX + 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX + 1][currentY + 1] & 19136992) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
            {
                // Able to move 1 tile north-east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY + 1] = 12;
                distances[currentMapX + 1][currentMapY + 1] = currentDistance;
            }
        }
        if (!isReachable)
        {
            // Try find a different reachable tile in the 21x21 area around the target tile, as close as possible to the target tile
            int upperboundDistance = Integer.MAX_VALUE;
            int pathLength = Integer.MAX_VALUE;
            int checkRange = 10;
            int approxDestinationX = p2.x;
            int approxDestinationY = p2.x;
            for (int i = approxDestinationX - checkRange; i <= checkRange + approxDestinationX; ++i)
            {
                for (int j = approxDestinationY - checkRange; j <= checkRange + approxDestinationY; ++j)
                {
                    int currentMapX = i - middleX + offsetX;
                    int currentMapY = j - middleY + offsetY;
                    if (currentMapX >= 0 && currentMapY >= 0 && currentMapX < 128 && currentMapY < 128 && distances[currentMapX][currentMapY] < 100)
                    {
                        int deltaX = 0;
                        if (i < approxDestinationX)
                        {
                            deltaX = approxDestinationX - i;
                        }
                        else if (i > approxDestinationX)
                        {
                            deltaX = i - (approxDestinationX);
                        }

                        int deltaY = 0;
                        if (j < approxDestinationY)
                        {
                            deltaY = approxDestinationY - j;
                        }
                        else if (j > approxDestinationY)
                        {
                            deltaY = j - (approxDestinationY);
                        }

                        int distanceSquared = deltaX * deltaX + deltaY * deltaY;
                        if (distanceSquared < upperboundDistance || distanceSquared == upperboundDistance && distances[currentMapX][currentMapY] < pathLength)
                        {
                            upperboundDistance = distanceSquared;
                            pathLength = distances[currentMapX][currentMapY];
                            currentX = i;
                            currentY = j;
                        }
                    }
                }
            }
            if (upperboundDistance == Integer.MAX_VALUE)
            {
                // No path found
                return null;
            }
        }

        // Getting path from directions and distances
        bufferX[0] = currentX;
        bufferY[0] = currentY;
        int index = 1;
        int directionNew;
        int directionOld;
        for (directionNew = directionOld = directions[currentX - middleX + offsetX][currentY - middleY + offsetY]; p1.getX() != currentX || p1.getY() != currentY; directionNew = directions[currentX - middleX + offsetX][currentY - middleY + offsetY])
        {
            if (directionNew != directionOld)
            {
                // "Corner" of the path --> new checkpoint tile
                directionOld = directionNew;
                bufferX[index] = currentX;
                bufferY[index++] = currentY;
            }

            if ((directionNew & 2) != 0)
            {
                ++currentX;
            }
            else if ((directionNew & 8) != 0)
            {
                --currentX;
            }

            if ((directionNew & 1) != 0)
            {
                ++currentY;
            }
            else if ((directionNew & 4) != 0)
            {
                --currentY;
            }
        }

        int checkpointTileNumber = 1;
        net.runelite.api.Tile[][][] tiles = API_CONTEXT.getClient().getScene().getTiles();
        var checkpointTiles = new ArrayList<Tile>();
        while (index-- > 0)
        {
            checkpointTiles.add(new Tile(tiles[this.getPlane()][bufferX[index]][bufferY[index]]));
            if (checkpointTileNumber == 25)
            {
                // Pathfinding only supports up to the 25 first checkpoint tiles
                break;
            }
            checkpointTileNumber++;
        }
        return checkpointTiles;
    }

    public int getX() {
        return this.tile.getWorldLocation().getX();
    }

    public int getY() {
        return this.tile.getWorldLocation().getY();
    }

    public int getPlane() {
        return this.tile.getWorldLocation().getPlane();
    }

    @Override
    public Position getPosition() {
        var position = tile.getWorldLocation();
        return new Position(position.getX(), position.getY(), position.getPlane());
    }

}
