package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static net.runelite.api.HitsplatID.*;

@AllArgsConstructor
public class Hitsplat {


    @Getter
    private final int hitsplatType;

    @Getter
    private final int amount;

    @Getter
    private final int disappearsOnGameCycle;

    public boolean isMine()
    {
        return switch (hitsplatType) {
            case BLOCK_ME,
                    DAMAGE_ME,
                    DAMAGE_ME_CYAN,
                    DAMAGE_ME_YELLOW,
                    DAMAGE_ME_ORANGE,
                    DAMAGE_ME_WHITE,
                    DAMAGE_MAX_ME,
                    DAMAGE_MAX_ME_CYAN,
                    DAMAGE_MAX_ME_ORANGE,
                    DAMAGE_MAX_ME_YELLOW,
                    DAMAGE_MAX_ME_WHITE ->
                    true;
            default -> false;
        };
    }

    public boolean isOthers()
    {
        return switch (hitsplatType) {
            case BLOCK_OTHER,
                    DAMAGE_OTHER,
                    DAMAGE_OTHER_CYAN,
                    DAMAGE_OTHER_YELLOW,
                    DAMAGE_OTHER_ORANGE,
                    DAMAGE_OTHER_WHITE ->
                    true;
            default -> false;
        };
    }

}
