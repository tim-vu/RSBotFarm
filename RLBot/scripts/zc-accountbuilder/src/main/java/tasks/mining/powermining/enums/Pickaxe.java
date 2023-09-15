package tasks.mining.powermining.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.script.api.data.ItemId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@AllArgsConstructor
public enum Pickaxe {

    BRONZE(1, 1, ItemId.BRONZE_PICKAXE),
    IRON(1, 1, ItemId.IRON_PICKAXE),
    STEEL(6, 5, ItemId.STEEL_PICKAXE),
    MITHRIL(21, 20, ItemId.MITHRIL_PICKAXE),
    ADAMANT(31, 30, ItemId.ADAMANT_PICKAXE),
    RUNE(41, 40, ItemId.RUNE_PICKAXE);

    @Getter
    private final int minimumMiningLevel;

    @Getter
    private final int minimumAttackLevel;

    @Getter
    private final int itemId;

    public boolean canEquip() {
        return Skills.getLevel(Skill.ATTACK) >= getMinimumAttackLevel();
    }

    public static Set<Pickaxe> getRemainingPickaxes(){

        Set<Pickaxe> pickaxes = new HashSet<>();

        for(int i = Pickaxe.values().length - 1; i >= 0; i--){

            Pickaxe pickaxe = Pickaxe.values()[i];
            pickaxes.add(pickaxe);

            if(pickaxe.getMinimumMiningLevel() < Skills.getLevel(Skill.MINING))
                break;

        }

        return pickaxes;
    }

    public static Pickaxe getBestUsable() {

        var miningLevel = Skills.getLevel(Skill.MINING);
        for(var i = Pickaxe.values().length - 1; i >= 0; i--) {
            var pickaxe = Pickaxe.values()[i];

            if(pickaxe.getMinimumMiningLevel() <= miningLevel) {
                return pickaxe;
            }
        }

        return Pickaxe.values()[0];
    }

    public static final Predicate<Item> IS_PICKAXE = item -> Arrays.stream(Pickaxe.values()).anyMatch(p -> p.getItemId() == item.getId());
}
