package net.rlbot.script.api.quests.misthalinmystery.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.scene.GameObject;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.event.listeners.GraphicsObjectSpawnedListener;
import net.rlbot.api.event.listeners.RenderListener;
import net.rlbot.api.event.types.GraphicsObjectSpawnedEvent;
import net.rlbot.api.event.types.RenderEvent;
import net.rlbot.api.movement.Direction;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

import java.awt.*;
import java.util.Map;

@Slf4j
public class FightBossNode extends UnaryNode implements GraphicsObjectSpawnedListener, RenderListener {

    private static final int INDICATOR_ID = 483;

    private static final Map<Position, Direction> WARDROBE_TO_DIRECTION = Map.of(
            new Position(51, 55, 0), Direction.WEST,
            new Position(46, 58, 0), Direction.SOUTH,
            new Position(43, 52, 0), Direction.EAST,
            new Position(48, 49, 0), Direction.NORTH
    );

    private Position lastWardrobe;
    private Position pushPosition;

    public FightBossNode() {
        super("Fighting the boss");
    }

    @Override
    protected ActionResult doExecute() {

        if(Quests.getStage(Quest.MISTHALIN_MYSTERY) == 115) {
            return ActionResult.SUCCESS;
        }

        if(lastWardrobe == null) {
            log.warn("No idea in which wardrobe the killer is");
            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        var mirror = Npcs.getNearest(n -> n.getName().contains("Mirror"));

        if(mirror == null) {
            log.warn("Unable to find mirror");
            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        var wardrobe = (GameObject)SceneObjects.getNearest(lastWardrobe, "Wardrobe");

        if(wardrobe == null) {
            log.warn("Unable to find wardrobe");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        var direction = getWardrobeDirection(wardrobe.getOrientation());

        var goalPosition = getGoalPosition(lastWardrobe, direction);

        if(isOnKnifePath(wardrobe, direction, mirror)) {


            if(mirror.getPosition().equals(goalPosition)) {
                log.debug("Mirror at goal position, waiting");
                Time.sleepTick();
                return ActionResult.IN_PROGRESS;
            }

            var pushLocation = getPushTowardsWardrobeLocation(mirror, direction);

            var currentPosition = Players.getLocal().getPosition();
            if(currentPosition.equals(pushLocation)) {

                if(!mirror.interact("Push")) {
                    log.warn("Failed to push the mirror");
                    Time.sleepTick();
                    return ActionResult.FAILURE;
                }

                Time.sleepTick();
                return ActionResult.IN_PROGRESS;
            }

            if(!Movement.walk(pushLocation) || !Time.sleepUntil(() -> Players.getLocal().distanceTo(pushLocation) < 1.5, () -> Players.getLocal().isMoving(), 1800)) {
                log.warn("Failed to walk to push location");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            return ActionResult.IN_PROGRESS;
        }

        pushPosition = getPushTowardsKnifePathLocation(wardrobe, mirror, direction);

        var currentPosition = Players.getLocal().getPosition();
        if(currentPosition.equals(pushPosition)) {

            var currentMirrorPosition = mirror.getPosition();
            if(!mirror.interact("Push")) {
                log.warn("Failed to push the mirror");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        if(!Movement.walk(pushPosition) || !Time.sleepUntil(() -> Players.getLocal().distanceTo(pushPosition) < 1.5, () -> Players.getLocal().isMoving(), 1800)) {
            log.warn("Failed to walk to push location");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        return ActionResult.IN_PROGRESS;
    }

    private static Direction getWardrobeDirection(int orientation) {

        if(orientation == 1024) {
            return Direction.WEST;
        }

        if(orientation == 0) {
            return Direction.EAST;
        }

        if(orientation == 512) {
            return Direction.SOUTH;
        }

        return Direction.NORTH;
    }

    private static Position getPushTowardsWardrobeLocation(Npc mirror, Direction direction) {
        return switch(direction) {
            case NORTH -> mirror.getPosition().dy(1);
            case SOUTH -> mirror.getPosition().dy(-1);
            case EAST -> mirror.getPosition().dx(1);
            case WEST -> mirror.getPosition().dx(-1);
        };
    }

    private static Position getPushTowardsKnifePathLocation(SceneObject wardrobe, Npc mirror, Direction direction) {
        return switch(direction) {
            case NORTH -> new Position(mirror.getX() + (int) Math.signum(mirror.getX() - wardrobe.getX()), mirror.getY(), mirror.getPlane());
            case EAST -> new Position(mirror.getX(), mirror.getY() + (int)Math.signum(mirror.getY() - wardrobe.getY()), mirror.getPlane());
            case SOUTH -> new Position(mirror.getX() + (int) Math.signum(mirror.getX() - wardrobe.getX()), mirror.getY(), mirror.getPlane());
            case WEST -> new Position(mirror.getX(), mirror.getY() + (int)Math.signum(mirror.getY() - wardrobe.getY()), mirror.getPlane());
        };
    }

    private static boolean isOnKnifePath(SceneObject wardrobe, Direction direction, Npc mirror) {
        return switch(direction) {
            case NORTH, SOUTH -> wardrobe.getX() == mirror.getX();
            case EAST, WEST -> wardrobe.getY() == mirror.getY();
        };
    }
    private static Position getGoalPosition(Position wardrobe, Direction direction) {
        return switch(direction) {
            case NORTH -> wardrobe.dy(3);
            case SOUTH -> wardrobe.dy(-3);
            case EAST -> wardrobe.dx(3);
            case WEST -> wardrobe.dx(-3);
        };
    }

    @Override
    public void notify(GraphicsObjectSpawnedEvent event) {

        if(event.getGraphicsObject().getId() != INDICATOR_ID) {
            return;
        }

        lastWardrobe = event.getGraphicsObject().getPosition();
        log.debug("Updating last wardrobe to {}", lastWardrobe);
    }

    @Override
    public void notify(RenderEvent event) {
        if(lastWardrobe != null) {
            lastWardrobe.outline((Graphics2D)event.getGraphics(), Color.GREEN);
        }

        if(pushPosition != null) {
            pushPosition.outline((Graphics2D)event.getGraphics(), Color.RED);
        }
    }
}
