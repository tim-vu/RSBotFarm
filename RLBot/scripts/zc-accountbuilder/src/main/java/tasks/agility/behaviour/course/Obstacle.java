package tasks.agility.behaviour.course;


import lombok.Getter;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;

import java.util.function.Predicate;

public class Obstacle {

    @Getter
    private final Area startingArea;

    @Getter
    private final Predicate<SceneObject> obstacleSelector;

    Obstacle(Area startingArea, Position obstaclePosition) {
        this.startingArea = startingArea;
        this.obstacleSelector = (sceneObject -> sceneObject.getPosition().equals(obstaclePosition));
    }

    Obstacle(Area startingArea, String obstacleName) {
        this.startingArea = startingArea;
        this.obstacleSelector = sceneObject -> sceneObject.getName().equals(obstacleName);
    }
}
