package net.rlbot.client;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import lombok.AllArgsConstructor;
import net.rlbot.api.event.EventDispatcher;
import net.rlbot.client.farm.FarmConnector;
import net.rlbot.client.farm.minimal.FpsDrawListener;
import net.rlbot.client.script.handler.ScriptHandler;
import net.rlbot.client.script.loader.ScriptLoader;
import net.rlbot.client.ui.BotUI;
import net.rlbot.internal.ApiContext;
import net.rlbot.internal.managers.*;
import net.rlbot.internal.wrapper.ClientImpl;
import net.rlbot.internal.wrapper.ClientThread;
import net.rlbot.rs.Hooks;
import net.runelite.api.Client;
import net.runelite.api.hooks.Callbacks;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.ExecutorServiceExceptionLogger;

import javax.annotation.Nullable;
import java.applet.Applet;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

@AllArgsConstructor
@SuppressWarnings({"deprecation", "removal"})
public class BotModule extends AbstractModule
{
    private final Supplier<Applet> clientLoader;

    @Override
    protected void configure()
    {
        binder().requireExplicitBindings();
        bindConstant().annotatedWith(Names.named("insecureWriteCredentials")).to(false);
        bind(File.class).annotatedWith(Names.named("runeLiteDir")).toInstance(net.runelite.client.RuneLite.RUNELITE_DIR);

        bind(EventBus.class).toInstance(new EventBus());
        bind(DeferredEventBus.class);
        bind(ScheduledExecutorService.class).toInstance(new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor()));
        bind(ClientThread.class);
        bind(Callbacks.class).to(Hooks.class);
        bind(DrawManager.class);
        bind(RuneLite.class);
//        bind(BotUI.class);
        bind(ClientImpl.class);
        bind(FocusManager.class);
        bind(InputManager.class);
        bind(EventDispatcher.class);
        bind(ScriptLoader.class);
        bind(InventoryManager.class);
        bind(XpDropManager.class);
        bind(RegionManager.class);
        bind(ApiContext.class);
        bind(FpsDrawListener.class);
        bind(BotUI.class);

        var botId = System.getenv("BOT_ID");

        if(botId != null && !botId.equals("")) {
            bind(String.class).annotatedWith(Names.named("botId")).toInstance(botId);
            bind(FarmConnector.class);
        }else {
            bind(String.class).annotatedWith(Names.named("botId")).toProvider(Providers.of(null));
        }
    }

    @Provides
    @Singleton
    Applet provideApplet()
    {
        return clientLoader.get();
    }

    @Provides
    @Singleton
    Client provideClient(@Nullable Applet applet)
    {
        return applet instanceof Client ? (Client) applet : null;
    }

    @Provides
    @Singleton
    ScriptHandler provideScriptHandler(EventDispatcher eventDispatcher, @Nullable @Named("botId") String botId)
    {
        if(botId != null) {
            return new ScriptHandler(eventDispatcher, new String[]{ botId });
        }

        return new ScriptHandler(eventDispatcher, new String[0]);
    }
}
