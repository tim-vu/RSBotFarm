package net.rlbot.api.event;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.scene.*;
import net.rlbot.api.event.listeners.*;
import net.rlbot.api.event.types.*;
import net.rlbot.api.movement.Position;
import net.runelite.api.NPC;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Singleton;
import java.awt.*;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@Slf4j
@Singleton
public class EventDispatcher {

    private Set<WeakReference<EventListener>> eventListeners = new HashSet<>();

    @Inject
    public EventDispatcher(EventBus eventBus) {
        eventBus.register(this);
    }

    public boolean isRegistered(EventListener eventListener) {
        for(var listener : eventListeners) {

            if(listener.get() == eventListener)
                return true;

        }

        return false;
    }

    public synchronized void register(EventListener eventListener) {
        if(isRegistered(eventListener))
            return;

        var newListeners = new HashSet<>(eventListeners);
        newListeners.add(new WeakReference<>(eventListener));
        eventListeners = newListeners;
    }

    public synchronized void deregister(EventListener eventListener) {

        var newListeners = new HashSet<>(eventListeners);
        newListeners.removeIf(listener -> listener.get() == eventListener);
        eventListeners = newListeners;
    }

    @Subscribe
    void onGraphicsObjectCreated(GraphicsObjectCreated graphicsObjectCreated) {
        var event = new GraphicsObjectSpawnedEvent(new GraphicsObject(graphicsObjectCreated.getGraphicsObject()));
        notifyListeners(event, GraphicsObjectSpawnedListener.class, GraphicsObjectSpawnedListener::notify);
    }

    @Subscribe
    void onActorDeath(ActorDeath actorDeath) {
        var event = new DeathEvent(getActor(actorDeath.getActor()));
        notifyListeners(event, DeathListener.class, DeathListener::notify);
    }

    @Subscribe
    void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        var event = new HitsplatEvent(
                getActor(hitsplatApplied.getActor()),
                new Hitsplat(
                        hitsplatApplied.getHitsplat().getHitsplatType(),
                        hitsplatApplied.getHitsplat().getAmount(),
                        hitsplatApplied.getHitsplat().getDisappearsOnGameCycle()
                )
        );
        notifyListeners(event, HitsplatListener.class, HitsplatListener::notify);
    }

    @Subscribe
    void onChatMessage(ChatMessage chatMessage) {
        var event = new ChatMessageEvent(
                ChatMessageType.of(chatMessage.getType().getType()),
                chatMessage.getMessage(),
                chatMessage.getSender()
        );
        notifyListeners(event, ChatMessageListener.class, ChatMessageListener::notify);
    }

    @Subscribe
    void onGraphicChanged(GraphicChanged graphicChanged) {
        var event = new GraphicChangedEvent(getActor(graphicChanged.getActor()));
        notifyListeners(event, GraphicChangedListener.class, GraphicChangedListener::notify);
    }

    @Subscribe
    void onItemSpawned(ItemSpawned itemSpawned) {
        var worldLocation = itemSpawned.getTile().getWorldLocation();
        var position = new Position(worldLocation.getX(), worldLocation.getY(), worldLocation.getPlane());
        var event = new PickableSpawnedEvent(new Pickable(position, itemSpawned.getItem()));
        notifyListeners(event, PickableSpawnedListener.class, PickableSpawnedListener::notify);
    }

    @Subscribe
    void onAnimationChanged(AnimationChanged animationChanged) {
        var event = new AnimationChangedEvent(getActor(animationChanged.getActor()));
        notifyListeners(event, AnimationChangedListener.class, AnimationChangedListener::notify);
    }

    private <E, L> void notifyListeners(E event, Class<L> listenerType, BiConsumer<L, E> invokeNotify) {

        eventListeners.stream()
                .map(Reference::get)
                .filter(Objects::nonNull)
                .filter(l -> listenerType.isAssignableFrom(l.getClass()))
                .map(listenerType::cast)
                .forEach(l -> {
                    try {
                        invokeNotify.accept(l, event);
                    }catch (Exception ex) {
                        log.error("An exception occurred in a script's listener", ex);
                    }
                });
    }

    public void onSkillEvent(SkillEvent skillEvent) {
        notifyListeners(skillEvent, SkillListener.class, SkillListener::notify);
    }

    public void onDraw(Image image, Graphics graphics) {
        var event = new RenderEvent(image, graphics);
        notifyListeners(event, RenderListener.class, RenderListener::notify);
    }

    private static Actor getActor(net.runelite.api.Actor actor) {
        if(actor instanceof NPC npc) {
            return new Npc(npc);
        }

        return new Player((net.runelite.api.Player)actor);
    }
}
