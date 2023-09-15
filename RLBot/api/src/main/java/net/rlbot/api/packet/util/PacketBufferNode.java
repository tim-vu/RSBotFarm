package net.rlbot.api.packet.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.runelite.api.Client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@AllArgsConstructor
public class PacketBufferNode {

    private static final String PACKET_BUFFER_NODE_NAME = "ly";
    private static final String CLASS_CONTAINING_GET_PACKET_BUFFER_NODE_METHOD = "cl";
    private static final String GET_PACKET_BUFFER_NODE_METHOD_NAME = "ae";
    private static final int GET_PACKET_BUFFER_NODE_GARBAGE_VALUE = 1633353192;
    private static final String PACKET_BUFFER_FIELD_NAME = "ao";

    public static Class<?> getObfuscatedClass() {
        return clazz;
    }

    private static Class<?> clazz;

    private static Method GET_PACKET_BUFFER_NODE_METHOD;

    private static Field PACKET_BUFFER_FIELD;

    @SneakyThrows
    public static void init(Client client, Class<?> clientPacketClass, Class<?> isaacCipherClass) {
        var classContainingGetPacketBufferNode = client.getClass().getClassLoader().loadClass(CLASS_CONTAINING_GET_PACKET_BUFFER_NODE_METHOD);
        GET_PACKET_BUFFER_NODE_METHOD = classContainingGetPacketBufferNode.getDeclaredMethod(GET_PACKET_BUFFER_NODE_METHOD_NAME, clientPacketClass, isaacCipherClass, int.class);
        clazz = client.getClass().getClassLoader().loadClass(PACKET_BUFFER_NODE_NAME);
        PACKET_BUFFER_FIELD = clazz.getDeclaredField(PACKET_BUFFER_FIELD_NAME);
    }

    @Getter
    private final Object instance;

    @SneakyThrows
    public PacketBuffer getPacketBuffer() {
        return new PacketBuffer(PACKET_BUFFER_FIELD.get(instance));
    }

    @SneakyThrows
    public static PacketBufferNode getPacketBufferNode(ClientPacket clientPacket, IsaacCipher isaacCipher) {
        var instance = GET_PACKET_BUFFER_NODE_METHOD.invoke(null, clientPacket.getInstance(), isaacCipher.getInstance(), GET_PACKET_BUFFER_NODE_GARBAGE_VALUE);
        return new PacketBufferNode(instance);
    }

}
