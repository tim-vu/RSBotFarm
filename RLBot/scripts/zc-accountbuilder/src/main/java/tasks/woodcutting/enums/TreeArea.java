package tasks.woodcutting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;

//TODO: Add remaining locations
@AllArgsConstructor
public enum TreeArea {

    LUMBRIDGE_CASTLE_REGULAR(Tree.REGULAR, Area.rectangular(3159, 3234, 3199, 3207)),
    VARROCK_WEST_REGULAR(Tree.REGULAR, Area.rectangular(3156, 3424, 3172, 3374)),
    GRAND_EXCHANGE_REGULAR(Tree.REGULAR, Area.polygonal(
            new Position(3152, 3467, 0),
            new Position(3162, 3460, 0),
            new Position(3166, 3460, 0),
            new Position(3173, 3456, 0),
            new Position(3173, 3449, 0),
            new Position(3151, 3449, 0),
            new Position(3150, 3456, 0),
            new Position(3144, 3457, 0),
            new Position(3144, 3460, 0),
            new Position(3147, 3463, 0)
    )),
    VARROCK_WEST_OAK(Tree.OAK, Area.rectangular(3158, 3423, 3172, 3410)),
    CHAMPIONS_GUILD_OAK(Tree.OAK, Area.polygonal(
            new Position(3202, 3376, 0),
            new Position(3208, 3376, 0),
            new Position(3215, 3366, 0),
            new Position(3215, 3358, 0),
            new Position(3201, 3358, 0),
            new Position(3192, 3368, 0)
    )),
    CHAMPIONS_GUILD_WILLOW(Tree.WILLOW, Area.rectangular(3201, 3361, 3216, 3354)),
    CRAFTING_GUILD_WILLOW(Tree.WILLOW, Area.polygonal(
            new Position(2909, 3307, 0),
            new Position(2915, 3307, 0),
            new Position(2924, 3299, 0),
            new Position(2924, 3293, 0),
            new Position(2916, 3293, 0),
            new Position(2909, 3303, 0)
    )),
    PORT_SARIM_WILLOW(Tree.WILLOW, Area.polygonal(
            new Position(3008, 3175, 0),
            new Position(3018, 3175, 0),
            new Position(3026, 3181, 0),
            new Position(3031, 3178, 0),
            new Position(3031, 3166, 0),
            new Position(3009, 3158, 0),
            new Position(2996, 3162, 0),
            new Position(2994, 3171, 0)
    )),
    LUMBRIDGE_WEST_FARM_WILLOW(Tree.WILLOW, Area.rectangular(3161, 3275, 3169, 3264)),
    CORSAIR_CAVE_MAPLE(Tree.MAPLE, Area.rectangular(2476, 2901, 2485, 2894));

    @Getter
    private final Tree tree;

    @Getter
    private final Area area;

}
