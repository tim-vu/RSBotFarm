package tasks.cooking.rangecooking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.movement.position.Area;

@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum Range {

    EDGEVILLE("Stove", Area.rectangular(3077, 3496, 3081, 3489)),
    AL_KHARID("Range", Area.rectangular(3271, 3183, 3275, 3179));

    @Getter
    private String name;

    @Getter
    private Area area;

}
