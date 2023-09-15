package net.rlbot.internal.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.math.Random;
import net.runelite.api.Client;
import net.runelite.api.events.CanvasSizeChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.tools.Tool;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

@Slf4j
@Singleton
public class FocusManager {

    private final Client client;

    public boolean hasControl() {
        return controlled;
    }

    private boolean controlled;

    private FocusListener[] focusListeners;

    @Getter
    private boolean focused;

    @Inject
    public FocusManager(Client client, EventBus eventBus) {
        eventBus.register(this);
        this.client = client;
    }


    public void takeControl() {

        if(controlled) {
            return;
        }

        regainControl();
        controlled = true;
    }

    @SneakyThrows
    public void focus() {

        if(!controlled) {
            log.trace("Ignoring call as control is not taken");
            return;
        }

        var canvas = client.getCanvas();

        if(canvas == null) {
            return;
        }

        var dispatchKeyEvent = Random.between(0, 1) > 0.5;

        EventQueue.invokeLater(() -> {
            releaseControl();
            canvas.dispatchEvent(new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, (char)KeyEvent.VK_TAB, KeyEvent.KEY_LOCATION_STANDARD));
            canvas.dispatchEvent(new FocusEvent(canvas, FocusEvent.FOCUS_GAINED));
            regainControl();
        });
    }

    @SneakyThrows
    public void unfocus() {

        if(!controlled) {
            log.trace("Ignoring call as control is not taken");
            return;
        }

        var canvas = client.getCanvas();

        if(canvas == null) {
            return;
        }

        EventQueue.invokeLater(() -> {
            releaseControl();
            canvas.dispatchEvent(new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ALT, (char)KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_STANDARD));
            canvas.dispatchEvent(new FocusEvent(canvas, FocusEvent.FOCUS_LOST));
            regainControl();
        });

    }

    private void regainControl() {

        var canvas = client.getCanvas();

        if(canvas == null) {
            return;
        }

        if(focusListeners == null) {
            focusListeners = canvas.getFocusListeners();
        }

        for(var listener : canvas.getFocusListeners()) {
            canvas.removeFocusListener(listener);
        }
    }

    public void releaseControl() {

        if(!controlled) {
            return;
        }

        var canvas = client.getCanvas();

        if(canvas == null) {
            return;
        }

        for(var listener : focusListeners) {
            canvas.addFocusListener(listener);
        }

        controlled = true;
    }

    @Subscribe
    void onCanvasSizeChanged(CanvasSizeChanged event) {
        if(controlled) {
            regainControl();
        }
    }

}
