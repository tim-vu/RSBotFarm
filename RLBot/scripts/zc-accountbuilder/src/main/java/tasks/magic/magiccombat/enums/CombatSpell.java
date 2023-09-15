package tasks.magic.magiccombat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.magic.SpellBook;
import tasks.magic.common.ElementalStaff;

@AllArgsConstructor
public enum CombatSpell {

    WIND_STRIKE(SpellBook.Standard.WIND_STRIKE, ElementalStaff.AIR, 5.5),
    WATER_STRIKE(SpellBook.Standard.WATER_STRIKE, ElementalStaff.WATER, 7.5),
    EARTH_STRIKE(SpellBook.Standard.EARTH_STRIKE, ElementalStaff.EARTH, 9.5),
    FIRE_STRIKE(SpellBook.Standard.FIRE_STRIKE, ElementalStaff.FIRE, 11.5),
    WIND_BOLT(SpellBook.Standard.WIND_BOLT, ElementalStaff.AIR, 13.5),
    WATER_BOLT(SpellBook.Standard.WATER_BOLT, ElementalStaff.WATER, 16.5),
    EARTH_BOLT(SpellBook.Standard.EARTH_BOLT, ElementalStaff.EARTH, 19.5),
    FIRE_BOLT(SpellBook.Standard.FIRE_BOLT, ElementalStaff.FIRE, 22.5),
    WIND_BLAST(SpellBook.Standard.WIND_BLAST, ElementalStaff.AIR, 25.5),
    WATER_BLAST(SpellBook.Standard.WATER_BLAST, ElementalStaff.WATER, 28.5),
    EARTH_BLAST(SpellBook.Standard.EARTH_BLAST, ElementalStaff.EARTH, 31.5),
    FIRE_BLAST(SpellBook.Standard.FIRE_BLAST, ElementalStaff.FIRE, 34.5);

    @Getter
    private final Spell spell;

    @Getter
    private final ElementalStaff elementalStaff;

    @Getter
    private final double xp;
}
