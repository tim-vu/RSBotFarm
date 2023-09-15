package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class DialogPackets {

    public static void sendNumberedInput(int value) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.RESUME_COUNTDIALOG, packetWriter.getIsaacCipher());

        packetBufferNode.getPacketBuffer().writeInt(value);
        packetWriter.addNode(packetBufferNode);
    }

    public static void sendNameInput(String value) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.RESUME_NAMEDIALOG, packetWriter.getIsaacCipher());

        packetBufferNode.getPacketBuffer().writeByte(value.length() + 1);
        packetBufferNode.getPacketBuffer().writeStringCp1252NullTerminated(value);
        packetWriter.addNode(packetBufferNode);
    }

}
