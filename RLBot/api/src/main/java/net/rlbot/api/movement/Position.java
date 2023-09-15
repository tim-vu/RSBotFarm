package net.rlbot.api.movement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Position implements Positionable {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    @Getter
    private final int x;

    @Getter
    private final int y;

    @Getter
    private final int plane;

    public Position(int x, int y) {
        this(x, y, 0);
    }

    public List<Position> pathTo(Position other) {

        if (plane != other.getPlane())
        {
            return null;
        }

        LocalPoint sourceLp = LocalPoint.fromWorld(API_CONTEXT.getClient(), x, y);
        LocalPoint targetLp = LocalPoint.fromWorld(API_CONTEXT.getClient(), other.getX(), other.getY());

        if (sourceLp == null || targetLp == null)
        {
            return null;
        }

        int thisX = sourceLp.getSceneX();
        int thisY = sourceLp.getSceneY();
        int otherX = targetLp.getSceneX();
        int otherY = targetLp.getSceneY();

        var tiles = API_CONTEXT.getClient().getScene().getTiles();
        var sourceTile = new net.rlbot.api.adapter.scene.Tile(tiles[plane][thisX][thisY]);

        var targetTile = new net.rlbot.api.adapter.scene.Tile(tiles[plane][otherX][otherY]);
        var checkpointTiles = sourceTile.pathTo(targetTile);
        if (checkpointTiles == null)
        {
            return null;
        }
        var checkpointWPs = new ArrayList<Position>();
        for (var checkpointTile : checkpointTiles)
        {
            if (checkpointTile == null)
            {
                break;
            }
            checkpointWPs.add(checkpointTile.getPosition());
        }
        return checkpointWPs;
    }

    public Position dx(int dx) {
        return new Position(x + dx, y, plane);
    }

    public Position dy(int dy) {
        return new Position(x, y + dy, plane);
    }

    public Position d(int x, int y) {
        return new Position(this.x + x, this.y + y, this.plane);
    }

    public static Position fromLocal(int x, int y, int plane) {
        return new Position(
                (x >>> Perspective.LOCAL_COORD_BITS) + API_CONTEXT.getClient().getBaseX(),
                (y >>> Perspective.LOCAL_COORD_BITS) + API_CONTEXT.getClient().getBaseY(),
                plane
        );
    }

    @Override
    @JsonBackReference
    public Position getPosition() {
        return this;
    }

    public void outline(Graphics2D graphics, Color color)
    {
        LocalPoint localPoint = LocalPoint.fromWorld(API_CONTEXT.getClient(), new WorldPoint(x, y, plane));
        if (localPoint == null)
        {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(API_CONTEXT.getClient(), localPoint);
        if (poly == null)
        {
            return;
        }

        graphics.setColor(color);
        final Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(poly);
        graphics.setColor(new Color(0, 0, 0, 50));
        graphics.fill(poly);
        graphics.setStroke(originalStroke);
    }

}
