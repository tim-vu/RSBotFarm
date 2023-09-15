package activity.activities;

import activity.BasicProgressiveActivity;
import activity.LevelRange;
import activity.TaskFactory;
import net.rlbot.api.game.Skill;
import net.rlbot.script.api.task.stopcondition.LevelCondition;
import tasks.fishing.powerfishing.PowerFishingTask;
import tasks.fishing.powerfishing.PowerFishingTaskConfiguration;
import tasks.fishing.powerfishing.enums.FishingSpot;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FishingActivity extends BasicProgressiveActivity {

    @Override
    public float getWeight() {
        return 1;
    }

    public FishingActivity() {
        super(LEVEL_RANGE_TO_TASK_CONFIGURATORS, STOP_LEVELS);
    }

    private static final TaskFactory RAW_SHRIMP_AND_ANCHOVIES = new TaskFactory(
            (d, s) -> new PowerFishingTask(
                    PowerFishingTaskConfiguration.builder()
                        .fishingSpot(FishingSpot.LUMBRIDGE_SWAMP)
                    .build(),
                    new LevelCondition(Skill.FISHING, s)
            )
    );

    private static final TaskFactory RAW_TROUT_AND_SALMON = new TaskFactory(
            (d, s) -> new PowerFishingTask(
                    PowerFishingTaskConfiguration.builder()
                        .fishingSpot(FishingSpot.BARBARIAN_VILLAGE)
                    .build(),
                    new LevelCondition(Skill.FISHING, s)
            )
    );

    private static final Map<LevelRange, Set<TaskFactory>> LEVEL_RANGE_TO_TASK_CONFIGURATORS = Map.of(
            new LevelRange(1, 20), Set.of(RAW_SHRIMP_AND_ANCHOVIES),
            new LevelRange(20, 99), Set.of(RAW_TROUT_AND_SALMON)
    );

    private static final List<Integer> STOP_LEVELS = List.of(20);

}
