package activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import tasks.common.AccountBuilderTask;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class TaskFactory implements Weighted {

    @Getter
    float weight;

    @Getter
    Set<Requirement> requirements;

    public AccountBuilderTask createTask(Duration duration, int stopLevel) {
        return taskFactory.apply(duration, stopLevel);
    }

    BiFunction<Duration, Integer, AccountBuilderTask> taskFactory;

    public TaskFactory(BiFunction<Duration, Integer, AccountBuilderTask> taskFactory) {
        this.weight = 1;
        this.requirements = Collections.emptySet();
        this.taskFactory = taskFactory;
    }

    public TaskFactory(BiFunction<Duration, Integer, AccountBuilderTask> taskFactory, float weight) {
        this.weight = weight;
        this.requirements = Collections.emptySet();
        this.taskFactory = taskFactory;
    }

    public TaskFactory(BiFunction<Duration, Integer, AccountBuilderTask> taskFactory, float weight, Set<Requirement> requirements) {
        this.weight = weight;
        this.requirements = requirements;
        this.taskFactory = taskFactory;
    }
}
