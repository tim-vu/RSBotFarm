package tasks.combat.common.combattask;

import lombok.Builder;
import lombok.Value;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import tasks.combat.common.combattask.combatstyle.CombatStyle;
import tasks.combat.common.combattask.enums.MonsterArea;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

@Builder
@Value
public class CombatConfiguration {

    Loadout loadout;

    Set<Tradeable> tradeables;

    MonsterArea monsterArea;

    @Builder.Default
    int foodItemId = ItemId.SALMON;

    @Builder.Default
    Set<Integer> itemIdsToLoot = Collections.emptySet();

    boolean buryBones;

    CombatStyle combatStyle;
}
