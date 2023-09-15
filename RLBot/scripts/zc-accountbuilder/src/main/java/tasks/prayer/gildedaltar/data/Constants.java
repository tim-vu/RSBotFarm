package tasks.prayer.gildedaltar.data;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;

public class Constants {

    public static final Area LAVA_MAZE = Area.rectangular(3003, 3859, 3055, 3809);

    public static final Area CHAOS_TEMPLE_INSIDE = Area.polygonal(
            new Position(2949, 3825, 0),
            new Position(2953, 3825, 0),
            new Position(2954, 3823, 0),
            new Position(2958, 3823, 0),
            new Position(2958, 3819, 0),
            new Position(2954, 3819, 0),
            new Position(2953, 3817, 0),
            new Position(2949, 3817, 0),
            new Position(2947, 3819, 0),
            new Position(2947, 3823, 0)
    );

    public static final Area GILDED_ALTAR = Area.rectangular(2947, 3828, 2961, 3813);

    public static final Position RUINS = new Position(2951, 3713, 0);

}
