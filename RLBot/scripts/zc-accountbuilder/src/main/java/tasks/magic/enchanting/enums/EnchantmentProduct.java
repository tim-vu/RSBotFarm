package tasks.magic.enchanting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.script.api.data.ItemId;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true)
public enum EnchantmentProduct {


    AMULET_OF_MAGIC(EnchantingSpell.LVL_1_ENCHANT, ItemId.SAPPHIRE_AMULET, ItemId.AMULET_OF_MAGIC),
    AMULET_OF_DEFENCE(EnchantingSpell.LVL_2_ENCHANT, ItemId.EMERALD_AMULET, ItemId.AMULET_OF_DEFENCE),
    AMULET_OF_STRENGTH(EnchantingSpell.LVL_3_ENCHANT, ItemId.RUBY_AMULET, ItemId.AMULET_OF_STRENGTH),
    AMULET_OF_POWER(EnchantingSpell.LVL_4_ENCHANT, ItemId.DIAMOND_AMULET, ItemId.AMULET_OF_POWER);

    EnchantingSpell enchantmentSpell;

    int sourceItemId;

    int productItemId;
}
