package net.rlbot.client;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import net.rlbot.client.spoofing.SystemSpoofing;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Program {

    public static final String LOGBACK_OVERRIDE_FILENAME = "logback_override.xml";

    public static void main(String[] args) throws JoranException, IOException {

        var loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        var classLoader = Thread.currentThread().getContextClassLoader();

        var configStream = classLoader.getResourceAsStream(LOGBACK_OVERRIDE_FILENAME);

        if(configStream == null) {
            var logger = loggerContext.getLogger(Program.class);

            logger.error("Unable to retrieve {}, cannot continue", LOGBACK_OVERRIDE_FILENAME);
            return;
        }

        configurator.setContext(loggerContext);
        configurator.doConfigure(configStream);
        configStream.close();

        SystemSpoofing.spoofProperties();

        RuneLite.init(args);
    }
}
