package tasks.magic.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.magic.Rune;
import net.rlbot.script.api.data.ItemId;

@AllArgsConstructor
public enum ElementalStaff {

    AIR(ItemId.STAFF_OF_AIR, Rune.AIR),
    WATER(ItemId.STAFF_OF_WATER, Rune.WATER),
    EARTH(ItemId.STAFF_OF_EARTH, Rune.EARTH),
    FIRE(ItemId.STAFF_OF_FIRE, Rune.FIRE);

    @Getter
    private final int itemId;

    @Getter
    private final Rune rune;
}
