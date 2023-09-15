package tasks.combat.common.combattask.behaviour.decisions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.game.Health;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Npcs;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tasks.combat.common.combattask.behaviour.Looting;
import tasks.combat.common.combattask.data.CombatKeys;

@Slf4j
public class Decisions {

    public static Decision waitForDeath() {
        return b -> {

            var targetIndex = b.get(CombatKeys.TARGET_INDEX);

            if (targetIndex == -1) {
                return false;
            }

            var npc = Npcs.getNearest(n -> n.getIndex() == targetIndex);

            if (npc == null) {
                return false;
            }

            return b.get(CombatKeys.ITEM_IDS_TO_LOOT).size() > 0 && npc.isDying();
        };
    }

    public static Decision dropItems() {
        return b -> Inventory.isFull();
    }

    public static Decision lootItems() {
        return b -> Looting.getLoot(b) != null;
    }

    public static Decision equipItems() {
        return b -> !LoadoutManager.isLoadoutSetup(b.get(BasicActivityKeys.LOADOUT));
    }

    private static final int MINIMUM_HEALTH_PERCENTAGE = 50;

    public static Decision eatFood() {
        return b -> Health.getPercent() < MINIMUM_HEALTH_PERCENTAGE;
    }

    public static Decision buryBones() {
        return b -> Inventory.contains(ItemId.BONES, ItemId.BIG_BONES);
    }

    public static Decision isCombatStyleSetup() {
        return b -> b.get(CombatKeys.COMBAT_STYLE).isSetup();
    }
}
