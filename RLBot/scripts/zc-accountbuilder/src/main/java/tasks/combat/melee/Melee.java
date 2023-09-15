package tasks.combat.melee;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import tasks.combat.common.Equipment;
import tasks.combat.common.combattask.CombatTask;
import tasks.combat.common.combattask.CombatConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Melee extends CombatTask {

    public Melee(MeleeTaskConfiguration configuration) {
        super(getConfiguration(configuration));
    }

    private static CombatConfiguration getConfiguration(MeleeTaskConfiguration configuration){

        var equipmentList = MeleeEquipment.EQUIPMENT.getBest();

        var loadout = getLoadout(equipmentList);
        var tradeables = getTradeables(equipmentList);

        return CombatConfiguration.builder()
                    .monsterArea(configuration.getMonsterArea())
                    .loadout(loadout)
                    .tradeables(tradeables)
                    .buryBones(configuration.getMonsterArea().isBuryBones())
                    .foodItemId(configuration.getFoodItemId())
                    .combatStyle(configuration.getMeleeStyle())
                .build();
    }

    private static Loadout getLoadout(List<Equipment> equipmentList) {
        var builder = Loadout.builder();

        var equipmentSetBuilder = builder.withEquipmentSet();

        for(var equipment : equipmentList) {
            equipmentSetBuilder.with(equipment.getItemId()).build();
        }

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

        return tradeables;
    }
}
