package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class PickablePackets {

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
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPOBJ1, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAddLE(objectId);
        packetBufferNode.getPacketBuffer().writeShortAdd(x);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShort(x);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSecondAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPOBJ2, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShort(y);
        packetBufferNode.getPacketBuffer().writeShortAdd(x);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortLE(objectId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueThirdAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPOBJ3, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortLE(y);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAdd(objectId);
        packetBufferNode.getPacketBuffer().writeShortLE(x);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFourthAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPOBJ4, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortLE(x);
        packetBufferNode.getPacketBuffer().writeShortAddLE(y);
        packetBufferNode.getPacketBuffer().writeShortLE(objectId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFifthAction(int objectId, int x, int y, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPOBJ5, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAddLE(x);
        packetBufferNode.getPacketBuffer().writeShort(y);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShort(objectId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueWidgetOnPickable(int objectId, int x, int y, int sourceSlot,
                                           int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPOBJT, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortLE(x);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAddLE(sourceItemId);
        packetBufferNode.getPacketBuffer().writeShortAdd(sourceSlot);
        packetBufferNode.getPacketBuffer().writeIntME(sourceWidgetId);
        packetBufferNode.getPacketBuffer().writeShortAddLE(y);
        packetBufferNode.getPacketBuffer().writeShortAddLE(objectId);
        packetWriter.addNode(packetBufferNode);
    }

}
