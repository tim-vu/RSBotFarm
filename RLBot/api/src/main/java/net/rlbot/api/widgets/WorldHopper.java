package net.rlbot.api.widgets;

import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Worlds;
import net.rlbot.api.input.Keyboard;
import net.rlbot.internal.ApiContext;

public class WorldHopper {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static boolean isWorldHopperOpen() {
        return Widgets.isVisible(WidgetInfo.WORLD_SWITCHER_LIST);
    }

    public static boolean openWorldHopper() {
        API_CONTEXT.getClient().openWorldHopper();
        return Time.sleepUntil(() -> Widgets.isVisible(WidgetInfo.WORLD_SWITCHER_LIST), 1200);
    }

    public static boolean hopTo(int id) {
        var world = Worlds.getLoaded(w -> w.getId() == id).stream().findFirst().orElse(null);

        if(world == null) {
            return false;
        }

        return hopTo(world);
    }

    public static boolean hopTo(World world)  {

        var rlWorld = world.getWorld();

        if(!isWorldHopperOpen()) {
            openWorldHopper();
        }

        var rsWorld = API_CONTEXT.getClient().createWorld();
        rsWorld.setActivity(rlWorld.getActivity());
        rsWorld.setAddress(rlWorld.getAddress());
        rsWorld.setId(rlWorld.getId());
        rsWorld.setPlayerCount(rlWorld.getPlayerCount());
        rsWorld.setLocation(rlWorld.getLocation());
        rsWorld.setTypes(rlWorld.getTypes());

        API_CONTEXT.getClient().hopToWorld(rsWorld);

        if(!Time.sleepUntil(Dialog::isOpen, 1200)) {
            return false;
        }

        Keyboard.sendSpace();
        return Time.sleepUntil(() -> !Game.isLoadingRegion() && Worlds.getCurrent() == rsWorld.getId(), 12000);
    }
}
