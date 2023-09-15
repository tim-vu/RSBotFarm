package net.rlbot.api.adapter.scene;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.Interactable;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.movement.Position;
import net.runelite.api.NPC;

@Slf4j
public abstract class Actor implements SceneEntity {

    protected static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private final net.runelite.api.Actor actor;

    public Actor(@NonNull net.runelite.api.Actor actor) {
        this.actor = actor;
    }

    public abstract int getId();

    public Interactable getInteracting() {
        var interacting = this.actor.getInteracting();

        if(interacting == null) {
            return null;
        }

        if(interacting instanceof NPC npc) {
            return new Npc(npc);
        }

        if(interacting instanceof net.runelite.api.Player player) {
            return new Player(player);
        }

        return null;
    }

    public int getX() {
        return actor.getWorldLocation().getX();
    }

    public int getY() {
        return actor.getWorldLocation().getY();
    }

    public int getPlane() {
        return actor.getWorldLocation().getPlane();
    }

    public Position getPosition() {
        var position = actor.getWorldLocation();
        return new Position(position.getX(), position.getY(), position.getPlane());
    }

    public String getOverheadText() {
        return actor.getOverheadText();
    }

    public abstract String getName();

    public abstract int getCombatLevel();

    public int getOrientation() {
        return (int) (270 - (actor.getOrientation() & 0x3fff) / 45.51) % 360;
    }

    public boolean isHealthBarVisible() {
        return getHealthScale() != -1;
    }

    public int getHealthPercent() {
        int healthRatio = this.actor.getHealthRatio();
        var healthScale = this.actor.getHealthScale();

        if (healthRatio == -1 || healthScale == -1)
        {
            return 100;
        }

        return (int)Math.round((healthRatio / (double)healthScale) * 100);
    }

    public int getHealthScale() {
        return this.actor.getHealthScale();
    }

    public Actor getTarget() {

        var target = this.actor.getInteracting();

        if(target instanceof NPC npc) {
            return new Npc(npc);
        }

        if(target instanceof net.runelite.api.Player player) {
            return new Player(player);
        }

        return null;
    }

    public int getAnimation() {
        return this.actor.getAnimation();
    }

    public int getAnimationFrame() {
        return this.actor.getAnimationFrame();
    }

    public boolean isAnimating() {
        return this.actor.getAnimation() != -1;
    }

    public boolean isAlive() {
        return !this.actor.isDead();
    }

    public boolean isMoving() {

        var poseAnimation = this.actor.getPoseAnimation();

        return poseAnimation == this.actor.getWalkAnimation() ||
                poseAnimation == this.actor.getRunAnimation() ||
                poseAnimation == this.actor.getWalkRotate180() ||
                poseAnimation == this.actor.getWalkRotateLeft() ||
                poseAnimation == this.actor.getWalkRotateRight();
    }

    public int getGraphic() {
        return this.actor.getGraphic();
    }

    public Direction getDirectionFacing() {

        int angle = this.actor.getOrientation();
        int round = angle >>> 9;
        int up = angle & 128;

        if (up != 0) {
            // round up
            ++round;
        }

        return switch (round & 7) {
            case 0 -> Direction.S;
            case 1 -> Direction.SW;
            case 2 -> Direction.W;
            case 3 -> Direction.NW;
            case 4 -> Direction.N;
            case 5 -> Direction.NE;
            case 6 -> Direction.E;
            case 7 -> Direction.SE;
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public int hashCode() {

        return this.actor.hashCode();
    }

    public enum Direction {
        N, S, E, W, NE, NW, SE, SW;
    }
}
