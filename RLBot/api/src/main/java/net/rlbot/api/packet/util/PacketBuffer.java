package net.rlbot.api.packet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.runelite.api.Client;

import java.lang.reflect.Field;

@AllArgsConstructor
public class PacketBuffer {

    private static final String BUFFER_NAME = "tm";
    private static final String PACKET_BUFFER_NAME = "to";

    private static final String BUFFER_ARRAY_NAME = "ap";
    private static final String BUFFER_OFFSET_NAME = "aa";

    private static final int BUFFER_OFFSET_GETTER = 1646688909;

    private static Field BUFFER_ARRAY;
    private static Field BUFFER_OFFSET;

    @SneakyThrows
    public static void init(Client client) {
        var buffer = client.getClass().getClassLoader().loadClass(PACKET_BUFFER_NAME);
        BUFFER_ARRAY = buffer.getField(BUFFER_ARRAY_NAME);
        BUFFER_OFFSET = buffer.getField(BUFFER_OFFSET_NAME);
    }

    @Getter
    private final Object instance;

    @SneakyThrows
    private int getOffset() {
        BUFFER_OFFSET.setAccessible(true);
        var offset = BUFFER_OFFSET.getInt(instance);
        BUFFER_OFFSET.setAccessible(false);
        return offset * BUFFER_OFFSET_GETTER;
    }

    @SneakyThrows
    private void setOffset(int offset) {
        BUFFER_OFFSET.setAccessible(true);
        BUFFER_OFFSET.setInt(instance, offset * DMath.modInverse(BUFFER_OFFSET_GETTER));
        BUFFER_OFFSET.setAccessible(false);
    }

    @SneakyThrows
    public byte[] getArray() {
        BUFFER_ARRAY.setAccessible(true);
        var array = BUFFER_ARRAY.get(instance);
        BUFFER_ARRAY.setAccessible(false);
        return (byte[]) array;
    }

    @SneakyThrows
    private void setArray(byte[] array) {
        BUFFER_ARRAY.setAccessible(true);
        BUFFER_ARRAY.set(instance, array);
        BUFFER_ARRAY.setAccessible(false);
    }

    public void writeByte(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)value;
        setOffset(offset);
        setArray(array);
    }

    public void writeByteAdd(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value + 128);
        setOffset(offset);
        setArray(array);
    }

    public void writeByteSub(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(128 - value);
        setOffset(offset);
        setArray(array);

    }

    public void writeByteNeg(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(-value);
        setOffset(offset);
        setArray(array);
    }

    public void writeShort(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)value;
        setOffset(offset);
        setArray(array);
    }

    public void writeShortAdd(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)(value + 128);
        setOffset(offset);
        setArray(array);
    }

    public void writeShortAddLE(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value + 128);
        array[++offset - 1] = (byte)(value >> 8);
        setOffset(offset);
        setArray(array);
    }

    public void writeShortLE(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value);
        array[++offset - 1] = (byte)(value >> 8);
        setOffset(offset);
        setArray(array);
    }

    public void writeMedium(int var1) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(var1 >> 16);
        array[++offset - 1] = (byte)(var1 >> 8);
        array[++offset - 1] = (byte)var1;
        setOffset(offset);
        setArray(array);
    }

    public void writeInt(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 24);
        array[++offset - 1] = (byte)(value >> 16);
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)value;
        setOffset(offset);
        setArray(array);
    }

    public void writeIntLE(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)value;
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)(value >> 16);
        array[++offset - 1] = (byte)(value >> 24);
        setOffset(offset);
        setArray(array);
    }

    public void writeIntME(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 16);
        array[++offset - 1] = (byte)(value >> 24);
        array[++offset - 1] = (byte)value;
        array[++offset - 1] = (byte)(value >> 8);
        setOffset(offset);
        setArray(array);
    }

    public void writeIntIME(int value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)value;
        array[++offset - 1] = (byte)(value >> 24);
        array[++offset - 1] = (byte)(value >> 16);
        setOffset(offset);
        setArray(array);
    }

    public void writeLongMedium(long value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 40);
        array[++offset - 1] = (byte)(value >> 32);
        array[++offset - 1] = (byte)(value >> 24);
        array[++offset - 1] = (byte)(value >> 16);
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)value;
        setOffset(offset);
        setArray(array);
    }

    public void writeLong(long value) {
        var offset = getOffset();
        var array = getArray();
        array[++offset - 1] = (byte)(value >> 56);
        array[++offset - 1] = (byte)(value >> 48);
        array[++offset - 1] = (byte)(value >> 40);
        array[++offset - 1] = (byte)(value >> 32);
        array[++offset - 1] = (byte)(value >> 24);
        array[++offset - 1] = (byte)(value >> 16);
        array[++offset - 1] = (byte)(value >> 8);
        array[++offset - 1] = (byte)value;
        setOffset(offset);
        setArray(array);
    }

    public void writeStringCp1252NullTerminated(String value) {
        var offset = getOffset();
        var array = getArray();
        encodeStringCp1252(value, 0, value.length(), array, offset);
        array[++offset - 1] = 0;
        setOffset(offset);
        setArray(array);
    }

    private static int encodeStringCp1252(String input, int start, int end, byte[] array, int offset) {
        int length = end - start;

        for (int i = 0; i < length; ++i) {
            char curr = input.charAt(i + start);
            if (curr > 0 && curr < 128 || curr >= 160 && curr <= 255) {
                array[i + offset] = (byte)curr;
            } else if (curr == 8364) {
                array[i + offset] = -128;
            } else if (curr == 8218) {
                array[i + offset] = -126;
            } else if (curr == 402) {
                array[i + offset] = -125;
            } else if (curr == 8222) {
                array[i + offset] = -124;
            } else if (curr == 8230) {
                array[i + offset] = -123;
            } else if (curr == 8224) {
                array[i + offset] = -122;
            } else if (curr == 8225) {
                array[i + offset] = -121;
            } else if (curr == 710) {
                array[i + offset] = -120;
            } else if (curr == 8240) {
                array[i + offset] = -119;
            } else if (curr == 352) {
                array[i + offset] = -118;
            } else if (curr == 8249) {
                array[i + offset] = -117;
            } else if (curr == 338) {
                array[i + offset] = -116;
            } else if (curr == 381) {
                array[i + offset] = -114;
            } else if (curr == 8216) {
                array[i + offset] = -111;
            } else if (curr == 8217) {
                array[i + offset] = -110;
            } else if (curr == 8220) {
                array[i + offset] = -109;
            } else if (curr == 8221) {
                array[i + offset] = -108;
            } else if (curr == 8226) {
                array[i + offset] = -107;
            } else if (curr == 8211) {
                array[i + offset] = -106;
            } else if (curr == 8212) {
                array[i + offset] = -105;
            } else if (curr == 732) {
                array[i + offset] = -104;
            } else if (curr == 8482) {
                array[i + offset] = -103;
            } else if (curr == 353) {
                array[i + offset] = -102;
            } else if (curr == 8250) {
                array[i + offset] = -101;
            } else if (curr == 339) {
                array[i + offset] = -100;
            } else if (curr == 382) {
                array[i + offset] = -98;
            } else if (curr == 376) {
                array[i + offset] = -97;
            } else {
                array[i + offset] = 63;
            }
        }

        return length;
    }
}
