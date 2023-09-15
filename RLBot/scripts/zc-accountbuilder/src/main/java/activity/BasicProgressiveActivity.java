package activity;

import lombok.AllArgsConstructor;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.task.stopcondition.LevelCondition;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import tasks.common.AccountBuilderTask;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class BasicProgressiveActivity implements Activity {

    private final Map<LevelRange, Set<TaskFactory>> taskConfigurators;
    private final List<Integer> stopLevels;

    public Queue<AccountBuilderTask> createTasks(Duration duration) {

        var currentLevel = Skills.getLevel(Skill.WOODCUTTING);
        var stopLevel = this.stopLevels.stream().filter(s -> s > currentLevel).findFirst();

        var taskFactories = this.taskConfigurators.entrySet().stream()
                .filter(e -> e.getKey().isInRange(currentLevel))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(c -> c.getRequirements().stream().allMatch(Requirement::isSatisfied))
                .collect(Collectors.toList());

        var taskFactory = WeightedCollection.pickRandom(taskFactories);

        var queue = new ArrayDeque<AccountBuilderTask>();
        queue.add(taskFactory.createTask(duration, stopLevel.orElse(99)));
        return queue;
    }
}
