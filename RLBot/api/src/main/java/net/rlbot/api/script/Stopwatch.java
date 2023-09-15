package net.rlbot.api.script;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

public class Stopwatch {

    private Duration duration = Duration.ZERO;
    private Instant startTime;

    @Getter
    private boolean running;

    @Getter
    private boolean paused;

    @Getter
    private boolean stopped;

    private Stopwatch(boolean paused) {

        this.running = true;

        if(!paused) {
            this.startTime = Instant.now();
            return;
        }

        this.paused = true;
    }

    public static Stopwatch start() {
        return new Stopwatch(false);
    }

    public static Stopwatch createPaused() {
        return new Stopwatch(true);
    }

    public void pause() {
        if(!this.isRunning()) {
            throw new IllegalStateException("isRunning() must be true");
        }

        if(this.isPaused()) {
            throw new IllegalStateException("isPaused() must be false");
        }

        this.paused = true;
        this.duration = this.duration.plus(getCurrentDuration());
        this.startTime = null;
    }

    public void resume() {
        if(!this.isRunning()) {
            throw new IllegalStateException("isRunning() must be true");
        }

        if(!this.isPaused()) {
            throw new IllegalStateException("isPaused() must be true");
        }

        this.paused = false;
        this.startTime = Instant.now();
    }

    public void stop() {

        if(!this.isRunning()) {
            throw new IllegalStateException("IsRunning() must be true");
        }

        if(!isPaused()) {
            this.duration = this.duration.plus(getCurrentDuration());
        }

        this.startTime = null;
        this.running = false;
    }

    private static final int SECONDS_IN_HOUR = 60 * 60;

    private Duration getCurrentDuration() {
        return Duration.between(startTime, Instant.now());
    }

    public Duration getDuration() {

        if(!isRunning()) {
            return this.duration;
        }

        if(isPaused()) {
            return this.duration;
        }

        return this.duration.plus(getCurrentDuration());
    }
}
