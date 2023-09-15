package net.rlbot.internal;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.scene.Players;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;

import java.util.Arrays;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class Interaction {

        public static void log(String type, Object... args) {

        var source = Util.getCallingClass();
        var logger = LoggerFactory.getLogger(source);

        var message = "Interact {}, {}, {}" + ", {}".repeat(args.length - 1);
        var allArgs = new Object[args.length + 2];
        System.arraycopy(args, 0, allArgs, 2, args.length);
        allArgs[0] = kv("interactionType", type);
        allArgs[1] = kv("position", Players.getLocal().getPosition());

        log.trace(message, allArgs);
    }

}
