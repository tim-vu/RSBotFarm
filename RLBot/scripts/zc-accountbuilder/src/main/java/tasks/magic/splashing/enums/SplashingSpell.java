package tasks.magic.splashing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.magic.SpellBook;

@AllArgsConstructor
public enum SplashingSpell {

    WIND_STRIKE(SpellBook.Standard.WIND_STRIKE),
    CONFUSE(SpellBook.Standard.CONFUSE),
    WEAKEN(SpellBook.Standard.WEAKEN),
    CURSE(SpellBook.Standard.CURSE);

    @Getter
    private final Spell spell;

}
