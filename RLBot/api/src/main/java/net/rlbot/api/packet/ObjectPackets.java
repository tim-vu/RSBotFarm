package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class ObjectPackets {

    public static void queueAction(int actionIndex, int objectId, int x, int y, boolean ctrlDown) {

        switch (actionIndex) {
            case 1 -> queueFirstAction(objectId, x, y, ctrlDown);
            case 2 -> queueSecondAction(objectId, x, y, ctrlDown);
            case 3 -> queueThirdAction(objectId, x, y, ctrlDown);
            case 4 -> queueFourthAction(objectId, x, y, ctrlDown);
            case 5 -> queueFifthAction(objectId, x, y, ctrlDown);
            default -> throw new IllegalStateException("Unexpected value: " + actionIndex);
        }
    }

    public static void queueFirstAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPLOC1, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShort(x);
        packetBufferNode.getPacketBuffer().writeShortLE(y);
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAdd(objectId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSecondAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPLOC2, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShort(x);
        packetBufferNode.getPacketBuffer().writeShort(y);
        packetBufferNode.getPacketBuffer().writeShortLE(objectId);
        packetBufferNode.getPacketBuffer().writeByteNeg(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void  queueThirdAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPLOC3, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShort(objectId);
        packetBufferNode.getPacketBuffer().writeShortAdd(x);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShort(y);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFourthAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPLOC4, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAddLE(y);
        packetBufferNode.getPacketBuffer().writeShort(objectId);
        packetBufferNode.getPacketBuffer().writeShortLE(x);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFifthAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPLOC5, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAddLE(objectId);
        packetBufferNode.getPacketBuffer().writeShortAddLE(y);
        packetBufferNode.getPacketBuffer().writeShortAddLE(x);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueWidgetOnObject(int objectId, int x, int y, int sourceSlot,
                                           int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPLOCT, packetWriter.getIsaacCipher());

        packetBufferNode.getPacketBuffer().writeShortAdd(objectId);
        packetBufferNode.getPacketBuffer().writeShortAddLE(x);
        packetBufferNode.getPacketBuffer().writeShort(y);
        packetBufferNode.getPacketBuffer().writeShortAdd(sourceSlot);
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAdd(sourceItemId);
        packetBufferNode.getPacketBuffer().writeIntLE(sourceWidgetId);
        packetWriter.addNode(packetBufferNode);
    }

}
