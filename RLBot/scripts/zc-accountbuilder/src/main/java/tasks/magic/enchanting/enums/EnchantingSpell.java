package tasks.magic.enchanting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.magic.SpellBook;
import tasks.magic.common.ElementalStaff;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true)
public enum EnchantingSpell {

    LVL_1_ENCHANT(SpellBook.Standard.LVL_1_ENCHANT, ElementalStaff.WATER),
    LVL_2_ENCHANT(SpellBook.Standard.LVL_2_ENCHANT, ElementalStaff.AIR),
    LVL_3_ENCHANT(SpellBook.Standard.LVL_3_ENCHANT, ElementalStaff.FIRE),
    LVL_4_ENCHANT(SpellBook.Standard.LVL_4_ENCHANT, ElementalStaff.EARTH);

    Spell spell;

    ElementalStaff elementalStaff;
}
