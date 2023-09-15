package net.rlbot.internal;

import lombok.Getter;
import net.rlbot.api.event.EventDispatcher;
import net.rlbot.internal.managers.FocusManager;
import net.rlbot.internal.wrapper.ClientImpl;
import net.rlbot.internal.managers.InputManager;
import net.rlbot.internal.managers.InventoryManager;
import net.runelite.client.ui.DrawManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.applet.Applet;

@Singleton
@SuppressWarnings({"deprecation", "removal"})
public class ApiContext {

    @Getter
    private final Applet applet;

    @Getter
    private final ClientImpl client;

    @Getter
    private final InputManager inputManager;

    @Getter
    private final EventDispatcher eventDispatcher;

    @Getter
    private final InventoryManager inventoryManager;

    @Getter
    private final DrawManager drawManager;

    @Getter
    private final FocusManager focusManager;

    @Inject
    public ApiContext(Applet applet,
                      ClientImpl client,
                      InputManager inputManager,
                      EventDispatcher eventDispatcher,
                      InventoryManager inventoryManager,
                      DrawManager drawManager,
                      FocusManager focusManager) {

        this.applet = applet;
        this.client = client;
        this.inputManager = inputManager;
        this.eventDispatcher = eventDispatcher;
        this.inventoryManager = inventoryManager;
        this.drawManager = drawManager;
        this.focusManager = focusManager;
    }

}
