package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class NpcPackets {

    public static void queueAction(int actionIndex, int npcIndex, boolean ctrlDown) {

        switch (actionIndex) {
            case 1 -> queueFirstAction(npcIndex, ctrlDown);
            case 2 -> queueSecondAction(npcIndex, ctrlDown);
            case 3 -> queueThirdAction(npcIndex, ctrlDown);
            case 4 -> queueFourthAction(npcIndex, ctrlDown);
            case 5 -> queueFifthAction(npcIndex, ctrlDown);
            default -> throw new IllegalStateException("Unexpected value: " + actionIndex);
        }
    }

    public static void queueFirstAction(int npcIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPNPC1, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAddLE(npcIndex);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSecondAction(int npcIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPNPC2, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortLE(npcIndex);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueThirdAction(int npcIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPNPC3, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAdd(npcIndex);
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFourthAction(int npcIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPNPC4, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortLE(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFifthAction(int npcIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPNPC5, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortLE(npcIndex);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueWidgetOnNpc(int npcIndex, int sourceSlot,
                                           int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPNPCT, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAddLE(npcIndex);
        packetBufferNode.getPacketBuffer().writeShortLE(sourceItemId);
        packetBufferNode.getPacketBuffer().writeIntME(sourceWidgetId);
        packetBufferNode.getPacketBuffer().writeShortAdd(sourceSlot);
        packetWriter.addNode(packetBufferNode);
    }

}
