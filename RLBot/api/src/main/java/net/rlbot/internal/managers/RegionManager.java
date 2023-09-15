package net.rlbot.internal.managers;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.game.Varbits;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.TeleportLoader;
import net.rlbot.api.movement.pathfinder.TransportLoader;
import net.rlbot.api.movement.pathfinder.Walker;
import net.rlbot.api.movement.pathfinder.model.Teleport;
import net.rlbot.api.movement.pathfinder.model.Transport;
import net.rlbot.api.quest.QuestVarbits;
import net.rlbot.api.widgets.WidgetInfo;
import net.runelite.api.InventoryID;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Singleton
public class RegionManager
{
    private static final Set<Integer> REFRESH_WIDGET_IDS = Set.of(
            WidgetInfo.QUEST_COMPLETED_NAME_TEXT.getGroupId(),
            WidgetInfo.LEVEL_UP_LEVEL.getGroupId()
    );

    private static final Set<Integer> REFRESH_VARBITS = Set.of(
            // Motherlode mine shortcut (54 agility)
            QuestVarbits.ACHIEVEMENT_DIARY_FALADOR_MEDIUM.getId(),

            // Draynor village wall shortcut (42 agility)
            QuestVarbits.ACHIEVEMENT_DIARY_LUMBRIDGE_MEDIUM.getId(),

            // Big windows shortcut in Al Kharid Palace (70 agility)
            QuestVarbits.ACHIEVEMENT_DIARY_DESERT_HARD.getId(),

            // Constructible darkmeyer shortcut on the eastern wall (63 agility)
            Varbits.DARKMEYER_EAST_WALL_SHORTCUT_1, Varbits.DARKMEYER_EAST_WALL_SHORTCUT_2,

            // Digsite gate might be accessible when value is >=153
            Varbits.KUDOS,

            // Zeah transport dialog is different first time talking to Veos
            Varbits.VEOS_HAS_TALKED_TO_BEFORE,
            // Zeah transport dialog is different when getting to a certain point in X Marks the spot (progress varbit >= 7)
            QuestVarbits.QUEST_X_MARKS_THE_SPOT.getId(),

            // Door states change depending on the lever states
            Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_1_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_2_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_3_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_4_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_5_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_6_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_7_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_8_STATE, Varbits.DRAYNOR_MANOR_BASEMENT_DOOR_9_STATE

    );

    private static boolean REFRESH_PATH = false;
    private static boolean INITIAL_LOGIN = true;

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    @Inject
    public RegionManager(EventBus eventBus) {
        eventBus.register(this);
    }

    public static boolean shouldRefreshPath()
    {
        boolean refreshPath = REFRESH_PATH;
        REFRESH_PATH = false;
        return refreshPath;
    }

    @Inject
    public void init()
    {
        executorService.submit(TransportLoader::init);
    }

    @Subscribe
    void onGameStateChanged(GameStateChanged event)
    {

        switch (event.getGameState()) {
            case UNKNOWN, STARTING, LOGIN_SCREEN, LOGIN_SCREEN_AUTHENTICATOR, CONNECTION_LOST -> INITIAL_LOGIN = true;
            case LOGGED_IN -> {
                if (INITIAL_LOGIN) {
                    INITIAL_LOGIN = false;
                    executorService.schedule(() -> {
                        REFRESH_PATH = true;
                        TeleportLoader.refreshTeleports();
                        TransportLoader.refreshTransports();
                    }, 1000, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    public boolean isTransport(List<Transport> transports, Position from, Position to)
    {
        if (transports == null)
        {
            return false;
        }

        return transports.stream().anyMatch(t -> t.getSource().equals(from) && t.getDestination().equals(to));
    }

    @Subscribe
    void onWidgetLoaded(WidgetLoaded event)
    {
        if (!REFRESH_WIDGET_IDS.contains(event.getGroupId())) {
            return;
        }

        if (!hasChanged()) {
            return;
        }

        REFRESH_PATH = true;
    }

    @Subscribe
    void onVarbitChanged(VarbitChanged event)
    {
        if (!REFRESH_VARBITS.contains(event.getVarbitId())) {
            return;
        }

        if (!hasChanged()) {
            return;
        }

        log.debug("Path refresh triggered by varbit {} change", event.getVarbitId());
        REFRESH_PATH = true;
    }

    @Subscribe
    void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() != InventoryID.INVENTORY.getId() && event.getContainerId() != InventoryID.EQUIPMENT.getId()) {
            return;
        }

        if (!hasChanged()) {
            return;
        }

        REFRESH_PATH = true;
    }

    private boolean hasChanged()
    {
        boolean tranChanged = transportsChanged();
        boolean teleChanged = teleportsChanged();

        boolean changed = tranChanged || teleChanged;

        if (changed)
        {
            log.debug("Transports/teleports changed!");
        }

        return changed;
    }

    private boolean transportsChanged()
    {
        List<Position> path = Walker.remainingPath(Walker.buildPath());

        if (path.isEmpty())
        {
            TransportLoader.refreshTransports();
            return false;
        }

        Map<Position, List<Transport>> previousTransports = Walker.buildTransportLinksOnPath(path);
        TransportLoader.refreshTransports();
        Map<Position, List<Transport>> currentTransports = Walker.buildTransportLinksOnPath(path);

        for (Position point : path)
        {
            List<Transport> prevTran = previousTransports.getOrDefault(point, new ArrayList<>());
            List<Transport> currTran = currentTransports.getOrDefault(point, new ArrayList<>());
            if (!prevTran.equals(currTran))
            {
                return true;
            }
        }

        return false;
    }

    private boolean teleportsChanged()
    {
        List<Position> path = Walker.remainingPath(Walker.buildPath());

        if (path.isEmpty())
        {
            TeleportLoader.refreshTeleports();
            return false;
        }

        LinkedHashMap<Position, Teleport> previousTeleports = Walker.buildTeleportLinksOnPath(path);
        TeleportLoader.refreshTeleports();
        LinkedHashMap<Position, Teleport> currentTeleports = Walker.buildTeleportLinksOnPath(path);

        for (Position point : path)
        {
            Teleport prevTele = previousTeleports.getOrDefault(point, null);
            Teleport currTele = currentTeleports.getOrDefault(point, null);
            if ((prevTele == null && currTele != null) || (prevTele != null && currTele == null))
            {
                return true;
            }
        }

        return false;
    }
}