package tasks.combat.ranged;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.combat.common.combattask.combatstyle.RangedStyle;
import tasks.combat.common.combattask.enums.MonsterArea;

@Builder
@Value
public class RangedTaskConfiguration implements TaskConfiguration {

    MonsterArea monsterArea;

    int foodItemId;

    RangedStyle rangedStyle;
}
