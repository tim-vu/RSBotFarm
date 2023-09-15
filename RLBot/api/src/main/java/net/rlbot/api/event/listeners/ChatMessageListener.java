package net.rlbot.api.event.listeners;

import net.rlbot.api.event.types.ChatMessageEvent;

public interface ChatMessageListener extends EventListener {

    void notify(ChatMessageEvent event);

}
