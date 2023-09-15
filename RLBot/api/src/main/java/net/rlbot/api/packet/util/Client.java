package net.rlbot.api.packet.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class Client {

    private static final String CLIENT_NAME = "client";
    private static final String PACKET_WRITER_FIELD_NAME = "ia";

    private static final String MOUSE_LAST_PRESSED_TIME_MILLIS_NAME = "ey";

    private static final long MOUSE_LAST_PRESSED_TIME_MILLIS_GETTER = 3989900610207861207L;

    public static PacketWriter getPacketWriter() {
        return PACKET_WRITER;
    }

    private static PacketWriter PACKET_WRITER;

    private static Field MOUSE_LAST_PRESSED_TIME_MILLIS;

    @SneakyThrows
    public static void init(net.runelite.api.Client client) {
        var clientClazz = client.getClass().getClassLoader().loadClass("client");
        var packetWriterField = clientClazz.getDeclaredField(PACKET_WRITER_FIELD_NAME);
        PACKET_WRITER = new PacketWriter(packetWriterField.get(null));
        MOUSE_LAST_PRESSED_TIME_MILLIS = clientClazz.getDeclaredField(MOUSE_LAST_PRESSED_TIME_MILLIS_NAME);
    }

    @SneakyThrows
    public static long getMouseLastPressedTimeMillis() {
        MOUSE_LAST_PRESSED_TIME_MILLIS.setAccessible(true);
        var result = MOUSE_LAST_PRESSED_TIME_MILLIS_GETTER * MOUSE_LAST_PRESSED_TIME_MILLIS.getLong(null);
        MOUSE_LAST_PRESSED_TIME_MILLIS.setAccessible(false);
        return result;
    }

    @SneakyThrows
    public static void setMouseLastPressedTimeMillis(long value) {
        value = DMath.modInverse(MOUSE_LAST_PRESSED_TIME_MILLIS_GETTER) * value;
        MOUSE_LAST_PRESSED_TIME_MILLIS.setAccessible(true);
        MOUSE_LAST_PRESSED_TIME_MILLIS.setLong(null, value);
        MOUSE_LAST_PRESSED_TIME_MILLIS.setAccessible(false);
    }
}
