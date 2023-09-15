package net.rlbot.api.script;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.script.randoms.*;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class Script {

	public final boolean isRunning() {
		return running;
	}

	public final boolean isActive() {
		return running && !paused;
	}

	private volatile boolean running = true;

	private volatile boolean paused = false;

	private volatile boolean started = false;

	public Duration getRuntime() {
		return stopwatch.getDuration();
	}

	private final Stopwatch stopwatch = Stopwatch.createPaused();

	public boolean hasStarted() {
		return started;
	}

	public Set<RandomEventHandler> getRandomEventHandlers() {
		return Collections.unmodifiableSet(randomEventHandlers);
	}

	public void removeRandomEventHandler(Class<?> clazz) {
		randomEventHandlers.removeIf(randomEventHandler -> randomEventHandler.getClass().equals(clazz));
	}

	public void addRandomEventHandler(RandomEventHandler randomEventHandler) {
		randomEventHandlers.add(randomEventHandler);
	}

	private final Set<RandomEventHandler> randomEventHandlers;

	public Script() {
		this.randomEventHandlers = new HashSet<>();
		randomEventHandlers.add(new LoginHandler());
		randomEventHandlers.add(new WelcomeScreenHandler());
		randomEventHandlers.add(new DeathEventHandler());
		randomEventHandlers.add(new BankTutorialHandler());
	}

	public boolean onStart(String[] args) {
		stopwatch.resume();
		started = true;
		return true;
	}

	public abstract int loop();

	public void onStop() {

	}

	public void onPause() {

	}

	public void onResume() {

	}
	public final void setPaused(boolean paused) {

		if(paused) {
			this.stopwatch.pause();
		}else {
			this.stopwatch.resume();
		}

		this.paused = paused;

		if(this.paused) {
			onPause();
		} else {
			onResume();
		}
	}

	public final boolean isPaused() {
		return paused;
	}

	public void stopScript() {
		this.running = false;
	}
}
