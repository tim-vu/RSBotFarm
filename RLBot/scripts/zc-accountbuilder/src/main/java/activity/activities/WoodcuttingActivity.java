package activity.activities;

import activity.BasicProgressiveActivity;
import activity.LevelRange;
import activity.TaskFactory;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.game.Skill;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.QuestRequirement;
import net.rlbot.script.api.task.stopcondition.LevelCondition;
import tasks.woodcutting.WoodcuttingTask;
import tasks.woodcutting.WoodcuttingTaskConfiguration;
import tasks.woodcutting.enums.Axe;
import tasks.woodcutting.enums.TreeArea;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WoodcuttingActivity extends BasicProgressiveActivity {

    @Override
    public float getWeight() {
        return 1;
    }

    public WoodcuttingActivity() {
        super(LEVEL_RANGE_TO_TASK_CONFIGURATORS, STOP_LEVELS);
    }

    private static final TaskFactory REGULAR_TREES = new TaskFactory(
            (d, s) -> new WoodcuttingTask(
                    WoodcuttingTaskConfiguration.builder()
                        .axe(Axe.getBestUsable())
                        .treeArea(Random.nextElement(
                                TreeArea.LUMBRIDGE_CASTLE_REGULAR,
                                TreeArea.VARROCK_WEST_REGULAR,
                                TreeArea.GRAND_EXCHANGE_REGULAR
                        ))
                    .build(),
                    new LevelCondition(Skill.WOODCUTTING, s)
            )
    );

    private static final TaskFactory OAK_TREES = new TaskFactory(
            (d, s) -> new WoodcuttingTask(
                    WoodcuttingTaskConfiguration.builder()
                        .axe(Axe.getBestUsable())
                        .treeArea(Random.nextElement(
                                TreeArea.VARROCK_WEST_OAK,
                                TreeArea.CHAMPIONS_GUILD_OAK
                        ))
                    .build(),
                    new LevelCondition(Skill.WOODCUTTING, s)
            )
    );

    private static final TaskFactory WILLOW_TREES = new TaskFactory(
            (d, s) -> new WoodcuttingTask(
                    WoodcuttingTaskConfiguration.builder()
                        .axe(Axe.getBestUsable())
                        .treeArea(Random.nextElement(
                                TreeArea.CHAMPIONS_GUILD_WILLOW,
                                TreeArea.CRAFTING_GUILD_WILLOW,
                                TreeArea.LUMBRIDGE_WEST_FARM_WILLOW,
                                TreeArea.PORT_SARIM_WILLOW
                        ))
                    .build(),
                    new LevelCondition(Skill.WOODCUTTING, s)
            ),
            0.8f
    );

    private static final TaskFactory MAPLE_TREES = new TaskFactory(
            (d, s) -> new WoodcuttingTask(
                    WoodcuttingTaskConfiguration.builder()
                        .axe(Axe.getBestUsable())
                        .treeArea(Random.nextElement(TreeArea.CORSAIR_CAVE_MAPLE))
                    .build(),
                    new LevelCondition(Skill.WOODCUTTING, s)
            ),
            0.4f,
            Set.of(
                    new QuestRequirement(Quest.DRAGON_SLAYER_I),
                    new QuestRequirement(Quest.THE_CORSAIR_CURSE)
            )
    );

    private static final Map<LevelRange, Set<TaskFactory>> LEVEL_RANGE_TO_TASK_CONFIGURATORS = Map.of(
            new LevelRange(1, 15), Set.of(REGULAR_TREES),
            new LevelRange(15, 99), Set.of(OAK_TREES),
            new LevelRange(30, 99), Set.of(WILLOW_TREES),
            new LevelRange(45, 99), Set.of(MAPLE_TREES)
    );

    private static final List<Integer> STOP_LEVELS = List.of(21, 31, 41);
}
