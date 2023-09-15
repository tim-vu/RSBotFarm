package activity.activities;

import activity.BasicProgressiveActivity;
import activity.LevelRange;
import activity.TaskFactory;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.game.Skill;
import net.rlbot.script.api.common.requirements.HasRequirements;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.task.stopcondition.LevelCondition;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import tasks.mining.powermining.PowerMiningTask;
import tasks.mining.powermining.PowerMiningTaskConfiguration;
import tasks.mining.powermining.enums.MiningArea;
import tasks.mining.powermining.enums.Pickaxe;
import tasks.mining.powermining.enums.Rock;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MiningActivity extends BasicProgressiveActivity {

    @Override
    public float getWeight() {
        return 1;
    }

    public MiningActivity() {
        super(LEVEL_RANGE_TO_TASK_CONFIGURATOR, STOP_LEVELS);
    }

    private static final TaskFactory TIN = new TaskFactory(
            (d, s) -> new PowerMiningTask(
                    PowerMiningTaskConfiguration.builder()
                            .miningArea(Random.nextElement(MiningArea.LUMBRIDGE_SWAMP_EAST_MINE, MiningArea.VARROCK_EAST_MINE))
                            .rock(Rock.TIN)
                            .pickaxe(Pickaxe.getBestUsable())
                            .build(),
                    new LevelCondition(Skill.MINING, s)
            )
    );

    private static final TaskFactory COPPER = new TaskFactory(
            (d, s) -> new PowerMiningTask(
                    PowerMiningTaskConfiguration.builder()
                        .miningArea(Random.nextElement(MiningArea.LUMBRIDGE_SWAMP_EAST_MINE, MiningArea.VARROCK_EAST_MINE))
                        .rock(Rock.COPPER)
                        .pickaxe(Pickaxe.getBestUsable())
                    .build(),
                    new LevelCondition(Skill.MINING, s)
            )

    );

    private static final TaskFactory IRON = new TaskFactory(
            (d, s) -> new PowerMiningTask(
                    PowerMiningTaskConfiguration.builder()
                         .miningArea(Random.nextElement(Stream.of(MiningArea.DWARVEN_MINE, MiningArea.VARROCK_EAST_MINE).filter(HasRequirements::areRequirementsSatisfied).toList()))
                         .rock(Rock.IRON)
                         .pickaxe(Pickaxe.getBestUsable())
                    .build(),
                    new LevelCondition(Skill.MINING, s)
            )
    );

    private static final Map<LevelRange, Set<TaskFactory>> LEVEL_RANGE_TO_TASK_CONFIGURATOR = Map.of(
            new LevelRange(1, 15), Set.of(TIN, COPPER),
            new LevelRange(15, 99), Set.of(IRON)
    );

    private static final List<Integer> STOP_LEVELS = List.of(21, 31, 41);
}
