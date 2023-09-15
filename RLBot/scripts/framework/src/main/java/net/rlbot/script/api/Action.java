package net.rlbot.script.api;

import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class Action {

    public static void logPerform(String action) {

        var source = Util.getCallingClass();
        var logger = LoggerFactory.getLogger(source);

        logger.debug("Action: {} {}", kv("actionEventType", "PERFORM"), kv("actionType", action));
    }

    public static void logFail(String action) {

        var source = Util.getCallingClass();
        var logger = LoggerFactory.getLogger(source);

        logger.debug("Action {}, {}", kv("actionEventType", "FAILURE"), kv("actionType", action));
    }

    public static void logTimeout(String action) {
        var source = Util.getCallingClass();
        var logger = LoggerFactory.getLogger(source);

        logger.debug("Action {} {}", kv("actionEventType", "TIMEOUT"), kv("actionType", action));
    }
}
