package tasks.magic.magiccombat;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.combat.common.combattask.enums.MonsterArea;
import tasks.magic.magiccombat.enums.CombatSpell;

@Builder
@Value
public class MagicCombatTaskConfiguration implements TaskConfiguration {

    MonsterArea monsterArea;

    int foodItemId;

    CombatSpell combatSpell;

    int restockAmount;
}
