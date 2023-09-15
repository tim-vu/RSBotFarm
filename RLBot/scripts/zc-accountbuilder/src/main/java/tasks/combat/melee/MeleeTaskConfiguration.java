package tasks.combat.melee;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.combat.common.combattask.combatstyle.MeleeStyle;
import tasks.combat.common.combattask.enums.MonsterArea;

@Builder
@Value
public class MeleeTaskConfiguration implements TaskConfiguration {

    MonsterArea monsterArea;

    MeleeStyle meleeStyle;

    int foodItemId;
}
