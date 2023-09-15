package net.rlbot.client.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

public class LoggerFilter extends TurboFilter {

    public void setLogger(String logger) {
        this.logger = logger;
    }

    private String logger;

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {

        if(!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if(logger.getName().contains(this.logger)) {
            return FilterReply.DENY;
        }

        return FilterReply.NEUTRAL;
    }
}
