/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.rlbot.rs;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.event.EventDispatcher;
import net.rlbot.api.event.types.*;
import net.rlbot.api.event.types.ChatMessageType;
import net.rlbot.api.event.types.Hitsplat;
import net.rlbot.api.movement.Position;
import net.rlbot.client.farm.minimal.FpsDrawListener;
import net.rlbot.internal.managers.FocusManager;
import net.rlbot.internal.managers.InventoryManager;
import net.rlbot.internal.managers.RegionManager;
import net.rlbot.internal.managers.XpDropManager;
import net.rlbot.internal.wrapper.ClientThread;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.util.DeferredEventBus;

import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains field required for mixins and runelite hooks to work.
 * All remaining method hooks in this class are performance-critical or contain client-specific logic and so they
 * can't just be placed in mixins or sent through event bus.
 */
@Singleton
@Slf4j
public class Hooks implements Callbacks
{
    private static final GameTick GAME_TICK = new GameTick();
    private static final BeforeRender BEFORE_RENDER = new BeforeRender();

    private final Client client;

    private final EventBus eventBus;

    private final DeferredEventBus deferredEventBus;

    private final EventDispatcher eventDispatcher;

    private final ClientThread clientThread;

    private final InventoryManager inventoryManager;

    private final DrawManager drawManager;

    private final XpDropManager xpDropManager;

    private final RegionManager regionManager;

    private boolean ignoreNextNpcUpdate;

    private boolean shouldProcessGameTick;

    private static MainBufferProvider lastMainBufferProvider;

    private static Graphics2D lastGraphics;

    @FunctionalInterface
    public interface RenderableDrawListener
    {
        boolean draw(Renderable renderable, boolean ui);
    }

    private final List<RenderableDrawListener> renderableDrawListeners = new ArrayList<>();

    /**
     * Get the Graphics2D for the MainBufferProvider image
     * This caches the Graphics2D instance, so it can be reused
     */
    private static Graphics2D getGraphics(MainBufferProvider mainBufferProvider)
    {
        if (lastGraphics == null || lastMainBufferProvider != mainBufferProvider)
        {
            if (lastGraphics != null)
            {
                log.debug("Graphics reset!");
                lastGraphics.dispose();
            }

            lastMainBufferProvider = mainBufferProvider;
            lastGraphics = (Graphics2D) mainBufferProvider.getImage().getGraphics();
        }
        return lastGraphics;
    }

    @Inject
    private Hooks(
            Client client,
            ClientThread clientThread,
            EventDispatcher eventDispatcher,
            EventBus eventBus,
            DeferredEventBus deferredEventBus,
            InventoryManager inventoryManager,
            DrawManager drawManager,
            XpDropManager xpDropManager,
            RegionManager regionManager,
            FpsDrawListener fpsDrawListener)
    {
        this.client = client;
        this.clientThread = clientThread;
        this.eventDispatcher = eventDispatcher;
        this.eventBus = eventBus;
        this.deferredEventBus = deferredEventBus;
        this.inventoryManager = inventoryManager;
        this.drawManager = drawManager;
        this.xpDropManager = xpDropManager;
        this.regionManager = regionManager;

        this.drawManager.registerEveryFrameListener(fpsDrawListener);
    }

    @Override
    public void post(Object event){
        eventBus.post(event);
    }

    @Override
    public void postDeferred(Object event) {
        deferredEventBus.post(event);
    }

    @Override
    public void tick()
    {
        if (shouldProcessGameTick)
        {
            shouldProcessGameTick = false;

            eventBus.post(GAME_TICK);

            int tick = client.getTickCount();
            client.setTickCount(tick + 1);
        }

        clientThread.invoke();


        long now = System.nanoTime();}

    @Override
    public void tickEnd()
    {
        clientThread.invokeTickEnd();
        eventBus.post(new PostClientTick());
    }

    @Override
    public void frame()
    {
        eventBus.post(BEFORE_RENDER);
    }
    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent)
    {
        return mouseEvent;
    }

    @Override
    public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event)
    {
        return event;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
    }

    @Override
    public void draw(MainBufferProvider mainBufferProvider, Graphics graphics, int x, int y)
    {
        if (graphics == null)
        {
            return;
        }

        final Graphics2D graphics2d = getGraphics(mainBufferProvider);

        if (client.isGpu())
        {
            // processDrawComplete gets called on GPU by the gpu plugin at the end of its
            // drawing cycle, which is later on.
            return;
        }

        Image image = mainBufferProvider.getImage();

        eventDispatcher.onDraw(image, image.getGraphics());

        // Draw the image onto the game canvas
        graphics.drawImage(image, 0, 0, client.getCanvas());

        drawManager.processDrawComplete(() -> screenshot(image));
    }

    private Image screenshot(Image src)
    {
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.drawImage(src, 0, 0, width, height, null);
        graphics.dispose();
        return image;
    }

    @Override
    public void drawScene()
    {

    }

    @Override
    public void drawAboveOverheads()
    {
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        switch (gameStateChanged.getGameState()) {
            case LOGGING_IN, HOPPING -> ignoreNextNpcUpdate = true;
        }
    }

    @Override
    public void updateNpcs()
    {
        if (ignoreNextNpcUpdate)
        {
            // After logging in an NPC update happens outside the normal game tick, which
            // is sent prior to skills and vars being bursted, so ignore it.
            ignoreNextNpcUpdate = false;
            log.debug("Skipping login updateNpc");
        }
        else
        {
            // The NPC update event seem to run every server tick,
            // but having the game tick event after all packets
            // have been processed is typically more useful.
            shouldProcessGameTick = true;
        }

        // Replay deferred events, otherwise if two npc
        // update packets get processed in one client tick, a
        // despawn event could be published prior to the
        // spawn event, which is deferred
//        deferredEventBus.replay();
    }

    @Override
    public void drawInterface(int interfaceId, List<WidgetItem> widgetItems)
    {
    }

    @Override
    public void drawLayer(Widget layer, List<WidgetItem> widgetItems)
    {
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent)
    {
        if (!scriptCallbackEvent.getEventName().equals("fakeXpDrop"))
        {
            return;
        }

        final int[] intStack = client.getIntStack();
        final int intStackSize = client.getIntStackSize();

        final int statId = intStack[intStackSize - 2];
        final int xp = intStack[intStackSize - 1];

        Skill skill = Skill.values()[statId];
        FakeXpDrop fakeXpDrop = new FakeXpDrop(
                skill,
                xp
        );

        eventBus.post(fakeXpDrop);
    }

    public void registerRenderableDrawListener(RenderableDrawListener listener)
    {
        renderableDrawListeners.add(listener);
    }

    public void unregisterRenderableDrawListener(RenderableDrawListener listener)
    {
        renderableDrawListeners.remove(listener);
    }

    @Override
    public boolean draw(Renderable renderable, boolean drawingUi)
    {
        try
        {
            for (RenderableDrawListener renderableDrawListener : renderableDrawListeners)
            {
                if (!renderableDrawListener.draw(renderable, drawingUi))
                {
                    return false;
                }
            }
        }
        catch (Exception ex)
        {
            log.error("exception from renderable draw listener", ex);
        }
        return true;
    }

    @Override
    public void error(String message, Throwable reason)
    {
    }
}
