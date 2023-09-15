package net.rlbot.client.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import net.rlbot.api.game.Chat;

import java.nio.charset.StandardCharsets;

public class ChatAppender<E> extends AppenderBase<E> {

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    private Encoder<E> encoder;

    @Override
    protected void append(E eventObject) {

        if (isStarted() && eventObject != null) {
            var fullMessage = new String(encoder.encode(eventObject), StandardCharsets.UTF_8);
            // Access the full message and perform any desired operations
            Chat.addMessage(fullMessage);
        }

    }

    @Override
    public void start() {
        if (encoder == null) {
            addError("No encoder set for the appender named [" + name + "].");
            return;
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
