package net.rlbot.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.adapter.scene.*;
import net.rlbot.api.common.Calculations;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.Definitions;
import net.rlbot.api.game.*;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.input.Mouse;
import net.rlbot.api.items.*;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.util.Packets;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.*;
import net.rlbot.api.widgets.Tabs;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.widgets.World;
import net.rlbot.api.widgets.WorldHopper;
import net.rlbot.client.farm.FarmConnector;
import net.rlbot.client.farm.minimal.FpsDrawListener;
import net.rlbot.client.script.handler.ScriptHandler;
import net.rlbot.client.script.loader.ScriptLoader;
import net.rlbot.client.ui.BotUI;
import net.rlbot.internal.ApiContext;
import net.rlbot.internal.managers.FocusManager;
import net.rlbot.internal.managers.XpDropManager;
import net.rlbot.internal.menu.Menu;
import net.rlbot.rs.ClientLoader;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.util.ReflectUtil;
import okhttp3.OkHttpClient;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.swing.*;
import java.applet.Applet;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Singleton
@Slf4j
@SuppressWarnings("removal")
public class RuneLite {

    public static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".runelite");

    @Inject
    @Nullable
    public Client client;

    @Inject
    @Nullable
    public Applet applet;

    @Inject
    private BotUI botUI;

    @Inject
    private ScriptHandler scriptHandler;

    @Inject
    private ScriptLoader scriptLoader;

    @Inject(optional = true)
    private FarmConnector farmConnector;

    @Getter
    private static Injector injector;

    @Inject
    private FpsDrawListener fpsDrawListener;

    @Inject
    private ApiContext apiContext;

    @Inject
    private EventBus eventBus;

    public static void init(String[] args) {

        Locale.setDefault(Locale.ENGLISH);

        final OptionParser parser = new OptionParser(false);

        OptionSet options = parser.parse(args);

        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
        {
            log.error("Uncaught exception:", throwable);
            if (throwable instanceof AbstractMethodError)
            {
                log.error("Classes are out of date; Build with maven again.");
            }
        });

        final OkHttpClient okHttpClient = new OkHttpClient();

        try
        {
            final ClientLoader clientLoader = new ClientLoader(okHttpClient, ClientUpdateCheckMode.AUTO, RuneLiteProperties.getJavConfig());

            new Thread(() ->
            {
                clientLoader.get();
                ClassPreloader.preload();
            }, "Preloader").start();

            log.info("RLBot starting up");

            final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            // This includes arguments from _JAVA_OPTIONS, which are parsed after command line flags and applied to
            // the global VM args
            log.info("Java VM arguments: {}", String.join(" ", runtime.getInputArguments()));

            final long start = System.currentTimeMillis();
            injector = Guice.createInjector(new BotModule(
                    clientLoader
            ));

            injector.getInstance(RuneLite.class).start(options);

            final long end = System.currentTimeMillis();
            final long uptime = runtime.getUptime();
            log.info("Client initialization took {}ms. Uptime: {}ms", kv("InitializationTime", end - start), kv("Uptime", uptime));
        }
        catch (Exception e)
        {
            log.error("Failure during startup", e);
        }
    }

    public void start(OptionSet options) throws Exception
    {
        // Load RuneLite or Vanilla client
        final boolean isOutdated = client == null;

        if (!isOutdated)
        {
            // Inject members into client
            injector.injectMembers(client);
        }
        else {
            log.error("Client is outdated, cannot continue");
            return;
        }

        // Start the applet
        // Client size must be set prior to init
        applet.setMinimumSize(Constants.GAME_FIXED_SIZE);
        applet.setPreferredSize(Constants.GAME_FIXED_SIZE);
        applet.setSize(Constants.GAME_FIXED_SIZE);

        var apiContext = injector.getInstance(ApiContext.class);
        initializeApi(apiContext);

        System.setProperty("jagex.disableBouncyCastle", "true");
        // Change user.home so the client places jagexcache in the .runelite directory
        String oldHome = System.setProperty("user.home", Configuration.OSRSBOT_DIR.getAbsolutePath());
        try
        {
            applet.init();
        }
        finally
        {
            System.setProperty("user.home", oldHome);
        }

        applet.start();

        if(farmConnector != null) {
            farmConnector.init();
            botUI.init(true);
        }else {
            botUI.init(false);
        }

        botUI.show();

        ReflectUtil.queueInjectorAnnotationCacheInvalidation(injector);
        ReflectUtil.invalidateAnnotationCaches();
    }

    private void initFarm() {

        if(farmConnector != null && !farmConnector.init()) {
            log.error("Failed to initialize FarmConnector, exiting");
            return;
        }

        fpsDrawListener.setEnabled(true);
//        client.setDrawCallbacks(new DisableRenderCallbacks());
    }

    private static final Set<Class<?>> API_TYPES = Set.of(
            Actor.class,
            Bank.class,
            Calculations.class,
            Chat.class,
            Definitions.class,
            EquipmentSlot.class,
            Camera.class,
            Combat.class,
            Game.class,
            GraphicsObject.class,
            GrandExchange.class,
            HintArrow.class,
            Inventory.class,
            Item.class,
            Keyboard.class,
            Magic.class,
            Menu.class,
            Mouse.class,
            MousePackets.class,
            Movement.class,
            Npcs.class,
            Pickable.class,
            Pickables.class,
            Players.class,
            Position.class,
            Reachable.class,
            SceneObject.class,
            SceneObjects.class,
            Skills.class,
            Tabs.class,
            Tile.class,
            Tiles.class,
            Time.class,
            Trade.class,
            Vars.class,
            Widget.class,
            Widgets.class,
            World.class,
            WorldHopper.class,
            Worlds.class,
            Equipment.class,
            Packets.class,
            Quests.class,
            XpDropManager.class
    );

    private void initializeApi(ApiContext apiContext) {

        for(var clazz : API_TYPES) {
            try {
                var method = clazz.getDeclaredMethod("init", ApiContext.class);
                method.setAccessible(true);
                method.invoke(null, apiContext);
            } catch (NoSuchMethodException ignored) {
                log.warn("Failed to init {}", clazz.getName());
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.warn("Failed to invoke init on {}", clazz.getName());
            }
        }
    }
}
