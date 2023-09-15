package net.rlbot.script.api.task;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.event.listeners.SkillListener;
import net.rlbot.api.event.types.SkillEvent;
import net.rlbot.api.game.Skill;
import net.rlbot.api.script.Stopwatch;
import net.rlbot.script.api.Paint;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class TaskBase implements Task, SkillListener {

    public Duration getRuntime() {
        return this.stopwatch.getDuration();
    }

    private final Stopwatch stopwatch = Stopwatch.createPaused();

    public Map<Skill, Integer> getExperiencedGained() {
        return Collections.unmodifiableMap(experiencedGained);
    }

    private final Map<Skill, Integer> experiencedGained = new HashMap<>();

    @Getter
    private final String name;

    public TaskBase(String name) {
        this.name = name;
    }

    @Override
    public void initialize() {
        log.debug("Starting task stopwatch");
        this.stopwatch.resume();
    }

    @Override
    public void onPause() {
        log.debug("Pausing task stopwatch");
        this.stopwatch.pause();
    }
    @Override
    public void onResume() {
        log.debug("Resuming task stopwatch");
        this.stopwatch.resume();
    }

    @Override
    public void notify(SkillEvent event) {
        var gained = experiencedGained.getOrDefault(event.getSkill(), 0);
        gained += event.getCurrent() - event.getPrevious();
        experiencedGained.put(event.getSkill(), gained);
    }

    @Override
    public List<String> getPaintInfo() {
        return List.of("Runtime: " + Paint.formatRuntime(this.stopwatch.getDuration()));
    }
}
