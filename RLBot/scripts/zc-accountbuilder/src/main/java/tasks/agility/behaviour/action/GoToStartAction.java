package tasks.agility.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.agility.behaviour.Keys;

import java.util.function.BooleanSupplier;

@Slf4j
public class GoToStartAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Walking to the start";
    }

    @Override
    public void execute() {

        var course = getBlackboard().get(Keys.COURSE);

        var firstObstacle = course.getObstacles().get(0);

        var obstacle = SceneObjects.getNearest(firstObstacle.getStartingArea(), firstObstacle.getObstacleSelector());

        if (obstacle != null && obstacle.distance() < 7) {

            if(!obstacle.interact(Predicates.always())) {
                log.warn("Failed to interact with the first obstacle");
                Time.sleepTick();
                return;
            }

            var next = course.getObstacles().get(1);

            BooleanSupplier waitingCondition = () -> {
                Player self = Players.getLocal();
                return ((next.getStartingArea().contains(self.getPosition()) && !self.isAnimating()) && !self.isMoving());
            };

            if(!Time.sleepUntil(waitingCondition, () -> {
                Player self = Players.getLocal();
                return self.isMoving() || self.isAnimating();
            }, 5000)) {
                log.warn("Failed to complete the first obstacle");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();

            return;
        }

        Movement.walkTo(firstObstacle.getStartingArea());
    }
}