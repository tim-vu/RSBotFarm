package net.rlbot.internal.managers;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.*;

@Slf4j
@Singleton
@SuppressWarnings("removal")
public class InputManager {

    private final Client client;


    @Inject
    public InputManager(Client client) {
        this.client = client;
    }

    public void clickMouse(int x, int y, boolean leftClick){
        pressMouse(x, y, leftClick);
        Time.sleep(80, 120);
        releaseMouse(x, y, leftClick);
    }

    private char getKeyChar(final char c) {

        if ((c >= 36) && (c <= 40)) {
            return KeyEvent.VK_UNDEFINED;
        }

        return c;
    }

    public void setMouse(final int x, final int y) {
        final MouseEvent me = new MouseEvent(
                client.getCanvas(),
                MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(),
                0,
                x,
                y,
                0,
                false);
        sendEvent(me);
    }

    private void pressMouse(final int x, final int y, final boolean left) {
        final MouseEvent me = new MouseEvent(
                client.getCanvas(),
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                0,
                x,
                y,
                1,
                false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        sendEvent(me);
    }

    private void releaseMouse(final int x, final int y, final boolean leftClick) {

        MouseEvent me = new MouseEvent(
                client.getCanvas(),
                MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(),
                0,
                x,
                y,
                1,
                false,
                leftClick ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        sendEvent(me);

        me = new MouseEvent(
                client.getCanvas(),
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                x,
                y,
                1,
                false,
                leftClick ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        sendEvent(me);
    }

    public void sendKey(final char ch) {

        boolean shift = false;

        int code = ch;

        if ((ch >= 'a') && (ch <= 'z')) {
            code -= 32;
        } else if ((ch >= 'A') && (ch <= 'Z')) {
            shift = true;
        }

        KeyEvent ke;

        if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_UP) || (code == KeyEvent.VK_DOWN)) {

            ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, code,
                    getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
            sendEvent(ke);

            ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, code,
                    getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
            sendEvent(ke);
            return;
        }

        if (!shift) {

            ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, code,
                    getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
            sendEvent(ke);

            // Event Typed
            ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 0, ch, 0);
            sendEvent(ke);

            // Event Released
            ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, code,
                    getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
            sendEvent(ke);
            return;
        }

        // Event Pressed for shift key
        ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
                KeyEvent.KEY_LOCATION_LEFT);
        sendEvent(ke);

        // Event Pressed for char to send
        ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                InputEvent.SHIFT_DOWN_MASK, code, getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
        sendEvent(ke);

        // Event Typed for char to send
        ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(),
                InputEvent.SHIFT_DOWN_MASK, 0, ch, 0);
        sendEvent(ke);

        // Event Released for char to send
        ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
                InputEvent.SHIFT_DOWN_MASK, code, getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
        sendEvent(ke);

        // Event Released for shift key
        ke = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
                InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
                KeyEvent.KEY_LOCATION_LEFT);
        sendEvent(ke);
    }

    public void sendKeys(final String text, final boolean pressEnter) {

        final char[] chs = text.toCharArray();

        for (final char element : chs) {
            sendKey(element);
        }

        if (!pressEnter) {
            return;
        }

        sendKey((char) KeyEvent.VK_ENTER);
    }

    public void sendEvent(AWTEvent e) {

        EventQueue.invokeLater(() -> {
            var canvas = client.getCanvas();
            canvas.dispatchEvent(e);
        });
    }
}
