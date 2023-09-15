package net.rlbot.script.api.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.script.Script;

import java.util.Queue;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public abstract class TaskQueueScript extends Script {

    @Getter(AccessLevel.PRIVATE)
    private Queue<Task> tasks;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    private Task currentTask;

    private String lastMessage;

    protected void setTasks(@NonNull Queue<Task> tasks) {

        this.tasks = tasks;
    }

    @Override
    public int loop() {

        if (getTasks() == null) {
            log.trace("No tasks set, waiting");
            return 1000;
        }

        Task currentTask = getCurrentTask();
        if (currentTask == null || currentTask.isDone()) {

            if (currentTask != null)
            {
                log.info("Task completed: {}", kv("task", currentTask.getClass().getSimpleName()));
                currentTask.terminate();
            }

            if (getTasks().size() == 0) {
                log.info("All tasks completed, stopping the script");
                return -1;
            }

            Task newTask = getTasks().poll();
            newTask.initialize();
            setCurrentTask(newTask);
            return 0;
        }

        var node = currentTask.getNode();

        if (node != null) {

            String status = node.getStatus();

            if (status != null && !status.equals(lastMessage)) {
                log.info("Status: {}", kv("NodeStatus", status));
                lastMessage = status;
            }

            node.execute();
        }

        return currentTask.getLoopDelay();
    }

    @Override
    public void onResume() {
        if(this.currentTask == null) {
            return;
        }

        this.currentTask.onResume();
    }

    @Override
    public void onPause() {
        if(this.currentTask == null) {
            return;
        }

        this.currentTask.onPause();
    }
    @Override
    public void onStop() {

        var task = getCurrentTask();

        if (task != null)
            task.terminate();

    }

}
