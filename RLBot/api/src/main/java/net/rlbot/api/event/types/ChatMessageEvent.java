package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.rlbot.api.event.Event;

@AllArgsConstructor
@Value
public class ChatMessageEvent implements Event {

    ChatMessageType type;

    String message;

    String sender;

}


