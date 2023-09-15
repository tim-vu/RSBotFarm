package tasks.agility.behaviour.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Pickables;
import net.rlbot.api.scene.Players;

import java.util.List;

@AllArgsConstructor
public enum Course {

    GNOME_STRONGHOLD(1, 10, ObstacleLists.GNOME_STRONGHOLD, Area.rectangular(2483, 3438, 2488, 3436, 0)),

    DRAYNOR_VILLAGE(10, 30, ObstacleLists.DRAYNOR_VILLAGE, Area.rectangular(3102, 3263, 3105, 3259, 0)),

    AL_KHARID(20, 30, ObstacleLists.AL_KHARID, Area.polygonal(new Position(3301, 3195, 0), new Position(3298, 3198, 0), new Position(3294, 3194, 0), new Position(3297, 3191, 0))),

    VARROCK(30, 50, ObstacleLists.VARROCK, Area.rectangular(3234, 3419, 3242, 3417)),

    FALADOR(50, 99, ObstacleLists.FALADOR, Area.rectangular(3028, 3330, 3031, 3337, 0));

    @Getter
    private final int minimumAgilityLevel;

    @Getter
    private final int stopLevel;

    @Getter
    private final List<Obstacle> obstacles;

    public Position getStartingPosition() {
        return getObstacles().get(0).getStartingArea().getCenter();
    }

    @Getter
    private final Area endingArea;

    public boolean isOnCourse() {
        return getCurrentObstacle() != null;
    }

    public Obstacle getCurrentObstacle() {

        Position currentPosition = Players.getLocal().getPosition();
        for (Obstacle obstacle : getObstacles()) {

            if (obstacle.getStartingArea().contains(currentPosition)) {
                return obstacle;
            }
        }

        return null;
    }

    public Pickable getMarkOfGrace() {

        var currentObstacle = getCurrentObstacle();

        if (currentObstacle == null)
        {
            return null;
        }

        return Pickables.getNearest(pickable -> pickable.getName().equals("Mark of grace") && currentObstacle.getStartingArea().contains(pickable.getPosition()));
    }
}
