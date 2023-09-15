package tasks.magic.magiccombat;

import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import tasks.combat.common.combattask.CombatConfiguration;
import tasks.combat.common.combattask.CombatTask;
import tasks.combat.common.combattask.combatstyle.MagicStyle;

import java.util.HashSet;
import java.util.Set;

public class MagicCombatTask extends CombatTask {

    public MagicCombatTask(MagicCombatTaskConfiguration configuration) {
            super(
                    CombatConfiguration.builder()
                                .monsterArea(configuration.getMonsterArea())
                                .loadout(getLoadout(configuration))
                                .tradeables(getTradeables(configuration))
                                .foodItemId(configuration.getFoodItemId())
                                .combatStyle(new MagicStyle(configuration.getCombatSpell().getSpell(), true))
                            .build()
            );
    }

    private static Loadout getLoadout(MagicCombatTaskConfiguration configuration) {
        var builder = Loadout.builder();

        var equipmentBuilder = builder.withEquipmentSet();
        for(var equipment : MagicEquipment.EQUIPMENT.getBest()) {
            equipmentBuilder.with(equipment.getItemId()).build();
        }

        var staff = configuration.getCombatSpell().getElementalStaff();

        equipmentBuilder.with(staff.getItemId()).build();
        equipmentBuilder.build();

        for(var runeRequirement : configuration.getCombatSpell().getSpell().getRuneRequirements()) {

            var rune = runeRequirement.getRune();

            if(rune == staff.getRune()) {
                continue;
            }

            builder.withItem(rune.getItemId()).amount(runeRequirement.getQuantity(), Integer.MAX_VALUE).build();
        }

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(MagicCombatTaskConfiguration configuration) {

        var result = new HashSet<Tradeable>();

        for(var equipment : MagicEquipment.EQUIPMENT.getBest()) {

            if(!equipment.isTradeable()) {
                continue;
            }

            result.add(new Tradeable(equipment.getItemId(), 1));
        }

        for(var runeRequirement : configuration.getCombatSpell().getSpell().getRuneRequirements()) {

            result.add(new Tradeable(runeRequirement.getRune().getItemId(), runeRequirement.getQuantity() * configuration.getRestockAmount()));
        }

        result.add(new Tradeable(configuration.getCombatSpell().getElementalStaff().getItemId(), 1));

        return result;
    }
}
