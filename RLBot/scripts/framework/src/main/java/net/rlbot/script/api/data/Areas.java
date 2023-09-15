package net.rlbot.script.api.data;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;

public class Areas {

    public static final Area GRAND_EXCHANGE = Area.surrounding(new Position(3164, 3489, 0), 10);

    public static final Area FEROX_ENCLAVE = Area.rectangular(3125, 3639, 3155, 3625);

    public static final Area FEROX_ENCLAVE_CHURCH = Area.polygonal(
            new Position(3131, 3640, 0),
            new Position(3131, 3639, 0),
            new Position(3132, 3638, 0),
            new Position(3132, 3635, 0),
            new Position(3131, 3634, 0),
            new Position(3131, 3633, 0),
            new Position(3127, 3633, 0),
            new Position(3127, 3634, 0),
            new Position(3125, 3634, 0),
            new Position(3125, 3638, 0),
            new Position(3127, 3638, 0),
            new Position(3127, 3640, 0)
    );}
