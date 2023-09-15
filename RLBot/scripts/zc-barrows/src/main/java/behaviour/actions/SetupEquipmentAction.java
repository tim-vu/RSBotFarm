package behaviour.actions;

import equipment.Equipment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.loadout.EquipmentSet;
import net.rlbot.script.api.loadout.Supply;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
@Slf4j
public class SetupEquipmentAction extends LeafNodeBase {

    private final Supplier<EquipmentSet> equipmentSetSupplier;

    @Override
    public String getStatus() {
        return "Setting up equipment";
    }

    private static final Map<Integer, Instant> SLOT_TO_LAST_EQUIP = new HashMap<>();
    private static final Duration EQUIP_TIMEOUT = Duration.ofSeconds(2);

    @Override
    public void execute() {

        var equipmentSet = equipmentSetSupplier.get();
        for(var equipment : equipmentSet.getSupplies()) {

            if(!Equipment.canEquip(equipment)) {
                continue;
            }

            var item = Supply.getLowestDose(equipment.getItemIds());

            //noinspection DataFlowIssue
            if(SLOT_TO_LAST_EQUIP.getOrDefault(item.getSlot(), Instant.EPOCH).minus(EQUIP_TIMEOUT).isAfter(Instant.now())) {
                continue;
            }

            Action.logPerform("EQUIP");
            if(!item.interact(Predicates.always())) {
                Action.logFail("EQUIP");
                Time.sleepTick();
                return;
            }

            Reaction.PREDICTABLE.sleep();
        }

        if(!Time.sleepUntil(() -> equipmentSet.getSupplies().stream().noneMatch(Equipment::canEquip), 1200)) {
            log.warn("Failed to wait for all equipment to be equipped");
            Time.sleepTick();
            return;
        }

    }
}
