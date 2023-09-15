package net.rlbot.api.packet;

import net.rlbot.api.common.math.Random;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.packet.util.Client;
import net.rlbot.api.packet.util.ClientPacket;
import net.rlbot.api.packet.util.MouseHandler;
import net.rlbot.api.packet.util.PacketBufferNode;
import net.rlbot.internal.ApiContext;

import java.awt.event.KeyEvent;

public class MousePackets {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static long randomDelay = 0;

    public static void queueClickPacket(int x, int y, boolean ctrlDown) {

        var mouseHandlerPressed = System.currentTimeMillis();

        MouseHandler.setLastPressedTimeMillis(mouseHandlerPressed);

        var clientPressed = Client.getMouseLastPressedTimeMillis();

        var diff = mouseHandlerPressed - clientPressed;

        if (diff > 32767L) {
            diff = 32767L;
        }

        Client.setMouseLastPressedTimeMillis(mouseHandlerPressed);

        var packetWriter = Client.getPacketWriter();
        var packetBufferNode = PacketBufferNode.getPacketBufferNode(ClientPacket.MOUSE_CLICK, packetWriter.getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShort((ctrlDown ? 1 : 0) + (int)(diff << 1));
        packetBufferNode.getPacketBuffer().writeShort(x);
        packetBufferNode.getPacketBuffer().writeShort(y);
        packetWriter.addNode(packetBufferNode);

        if(checkIdle()) {
            randomDelay = randomDelay();
            Keyboard.sendKey((char) KeyEvent.VK_BACK_SPACE);
        }
    }

    public static void queueClickPacket() {
        queueClickPacket(0, 0, false);
    }

    private static long randomDelay()
    {
        return (long) clamp(Math.round(Random.nextDouble() * 8000));
    }

    private static double clamp(double value)
    {
        return Math.max(1, Math.min(5000, value));
    }

    private static boolean checkIdle()
    {
        int idleClientTicks = API_CONTEXT.getClient().getKeyboardIdleTicks();
        if (API_CONTEXT.getClient().getMouseIdleTicks() < idleClientTicks)
        {
            idleClientTicks = API_CONTEXT.getClient().getMouseIdleTicks();
        }

        return idleClientTicks >= randomDelay;
    }
}
