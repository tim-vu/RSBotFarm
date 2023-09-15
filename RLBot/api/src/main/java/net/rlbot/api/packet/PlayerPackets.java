package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class PlayerPackets {

    public static void queueAction(int actionIndex, int playerIndex, boolean ctrlDown) {

        switch (actionIndex) {
            case 1 -> queueFirstAction(playerIndex, ctrlDown);
            case 2 -> queueSecondAction(playerIndex, ctrlDown);
            case 3 -> queueThirdAction(playerIndex, ctrlDown);
            case 4 -> queueFourthAction(playerIndex, ctrlDown);
            case 5 -> queueFifthAction(playerIndex, ctrlDown);
            case 6 -> queueSixthAction(playerIndex, ctrlDown);
            case 7 -> queueSeventhAction(playerIndex, ctrlDown);
            case 8 -> queueEighthAction(playerIndex, ctrlDown);
            default -> throw new IllegalStateException("Unexpected value: " + actionIndex);
        }
    }

    public static void queueFirstAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER1, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAdd(playerIndex);
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSecondAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER2, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAdd(playerIndex);
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueThirdAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER3, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortLE(playerIndex);
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFourthAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER4, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAddLE(playerIndex);
        packetBufferNode.getPacketBuffer().writeByteNeg(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFifthAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER5, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAddLE(playerIndex);
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSixthAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER6, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAddLE(playerIndex);
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSeventhAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER7, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortLE(playerIndex);
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueEighthAction(int playerIndex, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYER8, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByte(ctrlDown ? 1 : 0);
        packetBufferNode.getPacketBuffer().writeShortAddLE(playerIndex);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueWidgetOnPlayer(int playerIndex, int sourceSlot,
                                           int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.OPPLAYERT, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAdd(sourceSlot);
        packetBufferNode.getPacketBuffer().writeShortLE(sourceItemId);
        packetBufferNode.getPacketBuffer().writeIntME(sourceWidgetId);
        packetBufferNode.getPacketBuffer().writeShortLE(playerIndex);
        packetBufferNode.getPacketBuffer().writeByteAdd(ctrlDown ? 1 : 0);
        packetWriter.addNode(packetBufferNode);
    }

}
