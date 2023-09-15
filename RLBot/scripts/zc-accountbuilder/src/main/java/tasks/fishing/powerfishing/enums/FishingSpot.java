package tasks.fishing.powerfishing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;

import java.util.stream.Stream;

@AllArgsConstructor
public enum FishingSpot {

    BARBARIAN_VILLAGE(
            Area.polygonal(
                    new Position(3102, 3438, 0),
                    new Position(3107, 3438, 0),
                    new Position(3111, 3435, 0),
                    new Position(3111, 3432, 0),
                    new Position(3105, 3426, 0),
                    new Position(3105, 3424, 0),
                    new Position(3102, 3424, 0)
            ),
            FishingType.FLY_FISHING
    ),

    LUMBRIDGE_SWAMP(
            Area.polygonal(
                new Position(3242, 3159, 0),
                new Position(3247, 3157, 0),
                new Position(3247, 3151, 0),
                new Position(3244, 3147, 0),
                new Position(3242, 3140, 0),
                new Position(3238, 3140, 0),
                new Position(3233, 3146, 0),
                new Position(3235, 3159, 0)
            ),
            FishingType.NET_FISHING
    ),
    OTTO_GROTTO(
            Area.polygonal(
                new Position(2507, 3492, 0),
                new Position(2505, 3498, 0),
                new Position(2504, 3502, 0),
                new Position(2501, 3505, 0),
                new Position(2501, 3512, 0),
                new Position(2503, 3515, 0),
                new Position(2505, 3516, 0),
                new Position(2510, 3516, 0),
                new Position(2510, 3523, 0),
                new Position(2499, 3522, 0),
                new Position(2497, 3512, 0),
                new Position(2496, 3510, 0),
                new Position(2496, 3505, 0),
                new Position(2499, 3501, 0),
                new Position(2499, 3492, 0)
            ),
            FishingType.BARBARIAN_FISHING
    );


    @Getter
    private final Area area;

    @Getter
    private final FishingType fishingType;

    @Override
    public String toString(){
        return Stream.of(name().toLowerCase().split("_")).map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1)).reduce((w1, w2) -> w1 + " " + w2).get();
    }

}
