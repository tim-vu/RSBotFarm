package tasks.agility.behaviour.course;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;

import java.util.Arrays;
import java.util.List;

class ObstacleLists {

    static List<Obstacle> GNOME_STRONGHOLD = Arrays.asList(
            new Obstacle(Area.rectangular(2471, 3435, 2477, 3440, 0), "Log balance"),
            new Obstacle(Area.rectangular(2471, 3430, 2477, 3426, 0), "Obstacle net"),
            new Obstacle(Area.rectangular(2471, 3424, 2476, 3422, 1), "Tree branch"),
            new Obstacle(Area.rectangular(2472, 3421, 2475, 3418, 2), "Balancing rope"),
            new Obstacle(Area.rectangular(2483, 3420, 2488, 3419, 2), "Tree branch"),
            new Obstacle(Area.rectangular(2483, 3425, 2488, 3417, 0), "Obstacle net"),
            new Obstacle(Area.rectangular(2483, 3431, 2488, 3427, 0), "Obstacle pipe")
    );

    static List<Obstacle> DRAYNOR_VILLAGE = Arrays.asList(
            new Obstacle(Area.rectangular(3103, 3281, 3109, 3277, 0), "Rough wall"),
            new Obstacle(Area.rectangular(3097, 3281, 3102, 3277, 3), "Tightrope"),
            new Obstacle(Area.polygonal(
                    new Position(3091, 3279, 3),
                    new Position(3094, 3276, 3),
                    new Position(3089, 3271, 3),
                    new Position(3086, 3274, 3)), "Tightrope"),
            new Obstacle(Area.rectangular(3089, 3267, 3094, 3265, 3), "Narrow wall"),
            new Obstacle(Area.rectangular(3088, 3261, 3088, 3257, 3), "Wall"),
            new Obstacle(Area.rectangular(3087, 3255, 3094, 3255, 3), "Gap"),
            new Obstacle(Area.rectangular(3096, 3261, 3101, 3256, 3), "Crate")
    );

    static List<Obstacle> AL_KHARID = Arrays.asList(
            new Obstacle(Area.rectangular(3270, 3199, 3277, 3195, 0), "Rough wall"),
            new Obstacle(Area.rectangular(3272, 3192, 3276, 3182, 3), "Tightrope"),
            new Obstacle(Area.rectangular(3265, 3172, 3272, 3161, 3), "Cable"),
            new Obstacle(Area.rectangular(3203, 3176, 3283, 3160, 3), "Zip line"),
            new Obstacle(Area.rectangular(3314, 3165, 3318, 3160, 1), "Tropical tree"),
            new Obstacle(Area.rectangular(3313, 3179, 3317, 3174, 2), "Roof top beams"),
            new Obstacle(Area.rectangular(3312, 3186, 3318, 3180, 3), "Tightrope"),
            new Obstacle(Area.polygonal(
                    new Position(3300, 3187, 3),
                    new Position(3305, 3187, 3),
                    new Position(3307, 3189, 3),
                    new Position(3301, 3195, 3),
                    new Position(3297, 3191, 3)),
                    "Gap")
    );

    static List<Obstacle> VARROCK = Arrays.asList(
            new Obstacle(Area.rectangular(3221, 3418, 3226, 3411, 0), "Rough wall"),
            new Obstacle(Area.rectangular(3214, 3419, 3219, 3409, 3), "Clothes line"),
            new Obstacle(Area.rectangular(3201, 3419, 3208, 3413, 3), "Gap"),
            new Obstacle(Area.rectangular(3198, 3416, 3192, 3416, 1), "Wall"),
            new Obstacle(Area.rectangular(3192, 3406, 3198, 3402, 3), "Gap"),
            new Obstacle(Area.polygonal(
                    new Position(3182, 3399, 3),
                    new Position(3202, 3399, 3),
                    new Position(3202, 3404, 3),
                    new Position(3209, 3404, 3),
                    new Position(3209, 3395, 3),
                    new Position(3190, 3382, 3),
                    new Position(3182, 3382, 3)),
                    new Position(3209, 3397, 3)
            ),
            new Obstacle(Area.rectangular(3218, 3404, 3232, 3393, 3), new Position(3233, 3402, 3)),
            new Obstacle(Area.rectangular(3236, 3409, 3240, 3403, 3), "Ledge"),
            new Obstacle(Area.rectangular(3236, 3416, 3240, 3410, 3), "Edge")
    );

    static List<Obstacle> FALADOR = Arrays.asList(
            new Obstacle(Area.rectangular(3033, 3341, 3038, 3337, 0), "Rough wall"),
            new Obstacle(Area.rectangular(3036, 3343, 3040, 3342, 3), "Tightrope"),
            new Obstacle(Area.rectangular(3044, 3349, 3051, 3341, 3), "Hand holds"),
            new Obstacle(Area.rectangular(3048, 3358, 3050, 3357, 3), "Gap"),
            new Obstacle(Area.rectangular(3045, 3367, 3048, 3358, 3), new Position(3044, 3361, 3)),
            new Obstacle(Area.rectangular(3034, 3364, 3041, 3361, 3), "Tightrope"),
            new Obstacle(Area.rectangular(3026, 3354, 3029, 3352, 3), new Position(3026, 3353, 3)),
            new Obstacle(Area.rectangular(3009, 3358, 3021, 3353, 3), "Gap"),
            new Obstacle(Area.rectangular(3016, 3349, 3022, 3343, 3), "Ledge"),
            new Obstacle(Area.rectangular(3011, 3346, 3014, 3344, 3), new Position(3011, 3343, 3)),
            new Obstacle(Area.rectangular(3009, 3342, 3013, 3335, 3), new Position(3012, 3334, 3)),
            new Obstacle(Area.rectangular(3012, 3334, 3017, 3332, 3), new Position(3018, 3332, 3)),
            new Obstacle(Area.rectangular(3019, 3335, 3024, 3332, 3), "Edge")
    );
}
