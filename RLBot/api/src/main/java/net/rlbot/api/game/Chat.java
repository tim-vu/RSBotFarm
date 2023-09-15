package net.rlbot.api.game;

import net.rlbot.internal.ApiContext;
import net.runelite.api.ChatMessageType;

public class Chat {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static void addMessage(String message) {

        if(API_CONTEXT == null) {
            return;
        }

        API_CONTEXT.getClient().addChatMessage(ChatMessageType.SPAM, "", message, "");
    }
}
