package activity;

import net.rlbot.script.api.task.Task;
import tasks.common.AccountBuilderTask;

import java.time.Duration;
import java.util.Queue;

public interface Activity extends Weighted {

    Queue<AccountBuilderTask> createTasks(Duration duration);

}
