package net.rlbot.api.game;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.widgets.World;
import net.rlbot.api.common.Predicates;
import net.runelite.api.WorldType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Worlds {

    private static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static int getCurrent() {
        return API_CONTEXT.getClient().getWorld();
    }

    public static List<World> getLoaded() {
        return getLoaded(Predicates.always());
    }

    public static List<World> getLoaded(Predicate<World> predicate) {

        var result = new ArrayList<World>();

        for(var rlWorld : API_CONTEXT.getClient().getWorldList()) {

            if(rlWorld == null) {
                continue;
            }

            var world = new World(rlWorld);

            if(!predicate.test(world)) {
                continue;
            }

            result.add(world);
        }

        return result;
    }

    public static boolean isInMemberWorld()
    {
        return API_CONTEXT.getClient().getWorldType().contains(WorldType.MEMBERS);
    }
}
