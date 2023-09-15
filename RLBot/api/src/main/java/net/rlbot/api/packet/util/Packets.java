package net.rlbot.api.packet.util;

import net.rlbot.internal.ApiContext;

public class Packets {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static boolean init;

    public static void load() {

        if(init) {
            return;
        }

        var client = API_CONTEXT.getClient().wrappedClient;
        Client.init(client);
        MouseHandler.init(client);
        ClientPacket.init(client);
        IsaacCipher.init(client);
        PacketBuffer.init(client);
        PacketBufferNode.init(client, ClientPacket.getObfuscatedClass(), IsaacCipher.getObfuscatedClass());
        PacketWriter.init(client, PacketBufferNode.getObfuscatedClass());
        PacketWriter.client = client;

        init = true;
    }

}
