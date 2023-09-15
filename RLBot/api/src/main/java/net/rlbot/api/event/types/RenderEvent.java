package net.rlbot.api.event.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import net.rlbot.api.event.Event;
import net.rlbot.api.event.listeners.RenderListener;

import java.awt.*;

@AllArgsConstructor
@Value
public class RenderEvent implements Event {

    Image image;

    Graphics graphics;

    public void dispatch(RenderListener listener) {
        listener.notify(this);
    }

}
