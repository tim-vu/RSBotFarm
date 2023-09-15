package net.rlbot.client.script.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.GameState;
import net.rlbot.api.common.Time;
import net.rlbot.api.event.EventDispatcher;
import net.rlbot.api.event.listeners.EventListener;
import net.rlbot.api.packet.util.Packets;
import net.rlbot.api.script.Script;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class ScriptHandler {

    private ScriptContext scriptContext;

    private final EventDispatcher eventDispatcher;

    private final String[] args;

    @Inject
    public ScriptHandler(EventDispatcher eventDispatcher, String[] args) {

        this.eventDispatcher = eventDispatcher;
        this.args = args;
    }

    public boolean isScriptRunning() {

        return scriptContext != null;
    }

    public boolean isPaused() {

        return scriptContext != null && scriptContext.getScript().isPaused();
    }

    public void startScript(@NonNull Script script) {

        if (script instanceof EventListener eventListener) {
            eventDispatcher.register(eventListener);
        }

        if (scriptContext != null) {
            log.info("Cannot start a script when there's one running");
            throw new IllegalStateException("Cannot start a script when there's one running");
        }

        scriptContext = new ScriptContext(
                script,
                new Thread(this::doRunScript, "Script")
        );
        scriptContext.getThread().start();
    }

    private void doRunScript() {

        while (scriptContext.getScript().isRunning()) {

            if (Game.getState() == GameState.STARTING ||
                    Game.getState() == GameState.LOADING ||
                    Game.getState() == GameState.CONNECTION_LOST ||
                    Game.getState() == GameState.HOPPING ||
                    Game.getState() == GameState.UNKNOWN) {
                Time.sleep(500);
                continue;
            }

            if(Game.getState() == GameState.LOGGED_IN) {
                Packets.load();
            }

            if (scriptContext.getScript().isPaused()) {
                Time.sleep(200);
                continue;
            }

            try {

                var activated = false;

                for (var handler : scriptContext.getScript().getRandomEventHandlers()) {

                    if (!handler.shouldActivate()) {
                        continue;
                    }

                    if(scriptContext.getScript().hasStarted()) {
                        setPaused(true);
                    }

                    activated = true;
                    var interval = handler.loop();

                    if (handler.shouldStopScript()) {
                        scriptContext.getScript().stopScript();
                        continue;
                    }

                    Time.sleep(interval);
                }

                setPaused(false);

                if (activated) {
                    continue;
                }

            } catch (Exception ex) {
                log.error("An exception occurred in a RandomEventHandler", ex);
                setPaused(false);
                continue;
            }

            if(!scriptContext.getScript().hasStarted()) {
                try {
                    log.info("Starting script: {}", scriptContext.getScript().getClass().getSimpleName());
                    scriptContext.getScript().onStart(args);
                } catch (Exception ex) {
                    log.error("An exception occurred in the script's onStart method", ex);
                    stopScript();
                    return;
                }
            }

            try {
                var interval = scriptContext.getScript().loop();

                if(interval == -1) {
                    stopScript();
                    return;
                }

                Time.sleep(interval);
            } catch (Exception ex) {
                log.error("An exception occurred in script's loop method", ex);
                Time.sleep(1000);
            }
        }

        stopScript();
    }

    public void setPaused(boolean paused) {

        if (scriptContext == null || scriptContext.getScript().isPaused() == paused) {
            return;
        }

        if (scriptContext.getScript() instanceof EventListener eventListener) {
            if (paused) {
                eventDispatcher.deregister(eventListener);
            } else {
                eventDispatcher.register(eventListener);
            }
        }

        scriptContext.getScript().setPaused(paused);
    }

    public void stopScript() {

        if (scriptContext == null)
        {
            return;
        }

        log.info("Stopping script: {}", scriptContext.getScript().getClass().getSimpleName());

        scriptContext.getScript().stopScript();

        if (scriptContext.getScript() instanceof EventListener eventListener) {
            eventDispatcher.deregister(eventListener);
        }

        try {
            scriptContext.getScript().onStop();
        }catch(Exception ex) {
            log.error("An exception occurred in the script's onFinish method");
        }

        if(scriptContext.getThread() != Thread.currentThread()) {
            try {
                scriptContext.getThread().join();
            } catch (InterruptedException ignored) {
            }
        }

        scriptContext = null;
    }
}
