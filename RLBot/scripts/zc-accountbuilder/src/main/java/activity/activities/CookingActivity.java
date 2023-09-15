package activity.activities;

import activity.BasicProgressiveActivity;
import activity.LevelRange;
import activity.TaskFactory;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.script.api.task.stopcondition.LevelCondition;
import tasks.cooking.jugsofwine.JugsOfWineTask;
import tasks.cooking.rangecooking.RangeCookingTask;
import tasks.cooking.rangecooking.RangeCookingTaskConfiguration;
import tasks.cooking.rangecooking.enums.Food;
import tasks.cooking.rangecooking.enums.Range;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CookingActivity extends BasicProgressiveActivity {

    @Override
    public float getWeight() {
        return 0;
    }

    public CookingActivity() {
        super(LEVEL_RANGE_TO_TASK_FACTORIES, Collections.emptyList());
    }

    private static final Map<LevelRange, Set<Food>> LEVEL_RANGE_TO_FOOD = Map.of(
            new LevelRange(1, 15), Set.of(Food.SHRIMP, Food.CHICKEN, Food.ANCHOVIES),
            new LevelRange(15, 25), Set.of(Food.TROUT),
            new LevelRange(25, 30), Set.of(Food.SALMON),
            new LevelRange(30, 35), Set.of(Food.TUNA)
    );

    private static final TaskFactory RANGE_COOKING = new TaskFactory(
            (d, s) -> {
                 var currentLevel = Skills.getLevel(Skill.COOKING);
                 var range = LEVEL_RANGE_TO_FOOD.entrySet().stream()
                         .filter(p -> p.getKey().isInRange(currentLevel))
                         .findFirst()
                         .get();

                 var food = Random.nextElement(range.getValue());
                 var currentExperience = Skills.getExperience(Skill.COOKING);
                 var remainingExperience = Skills.getExperienceAt(s) - currentExperience;
                 var foodNecessary = (remainingExperience / food.getXp()) + 1;

                 return new RangeCookingTask(
                         RangeCookingTaskConfiguration.builder()
                                 .range(Range.EDGEVILLE)
                                 .food(food)
                                 .restockAmount(foodNecessary)
                                 .restock(true)
                                 .build(),
                         new LevelCondition(Skill.COOKING, range.getKey().getFrom())
                 );
            }
    );

    private static final TaskFactory JUG_OF_WINE = new TaskFactory(
            (d, s) -> new JugsOfWineTask()
    );

    private static final Map<LevelRange, Set<TaskFactory>> LEVEL_RANGE_TO_TASK_FACTORIES = Map.of(
            new LevelRange(1, 35), Set.of(RANGE_COOKING),
            new LevelRange(35, 99), Set.of(JUG_OF_WINE)
    );


}
