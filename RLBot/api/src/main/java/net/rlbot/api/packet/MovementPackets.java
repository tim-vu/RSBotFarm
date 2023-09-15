package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class MovementPackets {

    public static void queueMovement(int x, int y, boolean ctrlDown) {

        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.MOVE_GAMECLICK, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeByte(5);
        packetBufferNode.getPacketBuffer().writeShortAdd(x);
        packetBufferNode.getPacketBuffer().writeShortAddLE(y);
        packetBufferNode.getPacketBuffer().writeByteSub(ctrlDown ? 2 : 0);
        packetWriter.addNode(packetBufferNode);
    }

}
