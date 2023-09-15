package net.rlbot.api.packet;

import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.PacketBufferNode;

public class WidgetPackets {

    public static void queueAction(int actionIndex, int widgetId, int childId, int itemId) {

        switch (actionIndex) {
            case 1 -> queueFirstAction(widgetId, childId, itemId);
            case 2 -> queueSecondAction(widgetId, childId, itemId);
            case 3 -> queueThirdAction(widgetId, childId, itemId);
            case 4 -> queueFourthAction(widgetId, childId, itemId);
            case 5 -> queueFifthAction(widgetId, childId, itemId);
            case 6 -> queueSixthAction(widgetId, childId, itemId);
            case 7 -> queueSeventhAction(widgetId, childId, itemId);
            case 8 -> queueEighthAction(widgetId, childId, itemId);
            case 9 -> queueNinthAction(widgetId, childId, itemId);
            case 10 -> queueTenthAction(widgetId, childId, itemId);
            default -> throw new IllegalStateException("Unexpected value: " + actionIndex);
        }
    }

    public static void queueFirstAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON1, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSecondAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON2, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueThirdAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON3, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFourthAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON4, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueFifthAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON5, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSixthAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON6, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueSeventhAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON7, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueEighthAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON8, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueNinthAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON9, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueTenthAction(int widgetId, int childId, int itemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTON10, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeInt(widgetId);
        packetBufferNode.getPacketBuffer().writeShort(childId);
        packetBufferNode.getPacketBuffer().writeShort(itemId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueWidgetOnWidget(int sourceWidgetId, int sourceSlot, int sourceItemId,
                                           int destinationWidgetId, int destinationSlot, int destinationItemId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.IF_BUTTONT, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShortAddLE(sourceItemId);
        packetBufferNode.getPacketBuffer().writeShort(destinationSlot);
        packetBufferNode.getPacketBuffer().writeShortAdd(sourceSlot);
        packetBufferNode.getPacketBuffer().writeShort(destinationItemId);
        packetBufferNode.getPacketBuffer().writeIntME(sourceWidgetId);
        packetBufferNode.getPacketBuffer().writeIntIME(destinationWidgetId);
        packetWriter.addNode(packetBufferNode);
    }

    public static void queueResumePause(int widgetId, int childId) {
        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.RESUME_PAUSE, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeIntLE(widgetId);
        packetBufferNode.getPacketBuffer().writeShortLE(childId);
        packetWriter.addNode(packetBufferNode);
    }

}
