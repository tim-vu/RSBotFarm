package net.rlbot.api.packet.util;

import lombok.SneakyThrows;
import net.runelite.api.Client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PacketWriter {

    private static final String ISAAC_CIPHER_FIELD_NAME = "ac";
    private static final String PACKET_WRITER_TYPE_NAME = "er";

    private static final String ADD_NODE_METHOD_NAME = "ao";

    private static final byte ADD_NODE_GARBAGE_VALUE = 29;

    private static Field ISAAC_CIPHER_FIELD;

    private static Method ADD_NODE_METHOD;

    @SneakyThrows
    public static void init(Client client, Class<?> packetBufferNode) {
        var packetWriterClass = client.getClass().getClassLoader().loadClass(PACKET_WRITER_TYPE_NAME);
        ISAAC_CIPHER_FIELD = packetWriterClass.getDeclaredField(ISAAC_CIPHER_FIELD_NAME);
        ADD_NODE_METHOD = packetWriterClass.getDeclaredMethod(ADD_NODE_METHOD_NAME, packetBufferNode, byte.class);
    }

    private final Object instance;

    public PacketWriter(Object instance) {
        this.instance = instance;
    }

    @SneakyThrows
    public IsaacCipher getIsaacCipher() {
        return new IsaacCipher(ISAAC_CIPHER_FIELD.get(instance));
    }

    @SneakyThrows
    public void addNode(PacketBufferNode packetBufferNode) {
        try{
            ADD_NODE_METHOD.setAccessible(true);
            ADD_NODE_METHOD.invoke(instance, packetBufferNode.getInstance(), ADD_NODE_GARBAGE_VALUE);
        } finally {
            ADD_NODE_METHOD.setAccessible(false);
        }
    }

    public static Client client;

}
