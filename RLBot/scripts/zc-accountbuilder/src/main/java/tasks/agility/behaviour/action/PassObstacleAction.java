package tasks.agility.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Health;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.agility.behaviour.Keys;

import java.util.function.BooleanSupplier;

@Slf4j
public class PassObstacleAction extends LeafNodeBase {


    @Override
    public String getStatus() {
        return "Passing an obstacle";
    }

    @Override
    public void execute() {

        var course = getBlackboard().get(Keys.COURSE);

        var currentObstacle = course.getCurrentObstacle();

        if (currentObstacle == null) {
            log.warn("Player player is currently not on the course");
            Time.sleepTick();
            return;
        }

        var sceneObject = SceneObjects.getNearest(currentObstacle.getObstacleSelector());

        if (sceneObject == null) {
            log.warn("Unable to find the obstacle");
            Time.sleepTick();
            return;
        }

        if (sceneObject.distance() > 15) {
            Movement.walkTo(sceneObject.getPosition());
        }

        int currentHp = Health.getCurrent();

        if(!sceneObject.interact(Predicates.always()) || !Time.sleepUntil(() -> Players.getLocal().isMoving() || Players.getLocal().isAnimating(), 2000))
        {
            log.warn("Failed to interact with the obstacle");
            Time.sleepTick();
            return;
        }

        int obstacleIndex = course.getObstacles().indexOf(currentObstacle);

        BooleanSupplier waitingCondition;

        boolean isLastObstacle = obstacleIndex == course.getObstacles().size() - 1;

        if (isLastObstacle) {

            waitingCondition = () -> course.getEndingArea().contains(Players.getLocal().getPosition());

        } else {

            var next = course.getObstacles().get(obstacleIndex + 1);
            waitingCondition = () -> {
                Player self = Players.getLocal();
                return next.getStartingArea().contains(self.getPosition());
            };
        }

        Time.sleep(2000);

        var successful = Time.sleepUntil(() -> {
            Player self = Players.getLocal();
            return waitingCondition.getAsBoolean() && ((!self.isAnimating() && !self.isMoving()) || Health.getCurrent() < currentHp);
        }, () -> {
            Player self = Players.getLocal();
            return self.isMoving() || self.isAnimating();
        }, 5000);

        if (!successful) {
            log.warn("Unable to complete the obstacle");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
