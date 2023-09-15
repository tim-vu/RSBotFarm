package tasks.combat.ranged;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import tasks.combat.common.Equipment;
import tasks.combat.common.combattask.CombatTask;
import tasks.combat.common.combattask.CombatConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Ranged extends CombatTask {

    public Ranged(RangedTaskConfiguration configuration) {
        super(getConfiguration(configuration));
    }

    private static CombatConfiguration getConfiguration(RangedTaskConfiguration configuration) {

        var rangedEquipment = RangedEquipment.EQUIPMENT.getBest();

        return CombatConfiguration.builder()
                    .monsterArea(configuration.getMonsterArea())
                    .loadout(getLoadout(rangedEquipment))
                    .tradeables(getTradeables(rangedEquipment))
                    .itemIdsToLoot(Set.of(ItemId.BRONZE_ARROW, ItemId.IRON_ARROW, ItemId.MITHRIL_ARROW, ItemId.ADAMANT_ARROW))
                    .foodItemId(configuration.getFoodItemId())
                    .buryBones(false)
                    .combatStyle(configuration.getRangedStyle())
                .build();

    }

    private static Loadout getLoadout(List<Equipment> equipmentList) {
        var builder = Loadout.builder();

        var equipmentSetBuilder = builder.withEquipmentSet();

        for(var equipment : equipmentList) {
            equipmentSetBuilder.with(equipment.getItemId()).build();
        }

        equipmentSetBuilder.with(ItemId.IRON_ARROW).amount(1, Integer.MAX_VALUE).build();
        equipmentSetBuilder.build();

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(List<Equipment> equipmentList) {
        var tradeables = new HashSet<Tradeable>();

        for(var equipment : equipmentList) {

            if(!equipment.isTradeable()) {
                continue;
            }

            tradeables.add(new Tradeable(equipment.getItemId(), 1));
        }

        tradeables.add(new Tradeable(ItemId.IRON_ARROW, 1000));

        return tradeables;
    }
}
