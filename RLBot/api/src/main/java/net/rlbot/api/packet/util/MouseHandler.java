package net.rlbot.api.packet.util;

import lombok.SneakyThrows;
import net.runelite.api.Client;

import java.lang.reflect.Field;

public class MouseHandler {

    private static final String MOUSE_HANDLER_NAME = "bf";
    private static final String LAST_PRESSED_TIME_MILLIS_FIELD_NAME = "as";

    private static final long LAST_PRESSED_TIME_MILLIS_FIELD_GETTER = 4296976941923200635L;

    private static Field LAST_PRESSED_TIME_MILLIS;

    @SneakyThrows
    public static void init(Client client) {
        var mouseHandler = client.getClass().getClassLoader().loadClass(MOUSE_HANDLER_NAME);
        LAST_PRESSED_TIME_MILLIS = mouseHandler.getDeclaredField(LAST_PRESSED_TIME_MILLIS_FIELD_NAME);
    }

    @SneakyThrows
    public static long getLastPressedTimeMillis() {
        LAST_PRESSED_TIME_MILLIS.setAccessible(true);
        var result = LAST_PRESSED_TIME_MILLIS_FIELD_GETTER * LAST_PRESSED_TIME_MILLIS.getLong(null);
        LAST_PRESSED_TIME_MILLIS.setAccessible(false);
        return result;
    }

    @SneakyThrows
    public static void setLastPressedTimeMillis(long value) {
        value = DMath.modInverse(LAST_PRESSED_TIME_MILLIS_FIELD_GETTER) * value;
        LAST_PRESSED_TIME_MILLIS.setAccessible(true);
        LAST_PRESSED_TIME_MILLIS.setLong(null, value);
        LAST_PRESSED_TIME_MILLIS.setAccessible(false);
    }

}
