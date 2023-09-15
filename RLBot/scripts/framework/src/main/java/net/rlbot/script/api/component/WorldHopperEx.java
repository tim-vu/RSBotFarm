package net.rlbot.script.api.component;

import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.game.Worlds;
import net.rlbot.api.widgets.World;
import net.rlbot.api.widgets.WorldHopper;

import java.util.function.Predicate;

public class WorldHopperEx {

    public static boolean randomHopInF2p() {

        return randomHopInF2p(Predicates.always());
    }

    public static boolean randomHopInF2p(Predicate<World> predicate) {

        var current = Worlds.getCurrent();
        var newWorld = getRandomSafeWorld(w -> !w.isMembers() && predicate.test(w) && w.getId() != current);
        return WorldHopper.hopTo(newWorld);
    }

    public static boolean randomHopInP2p() {

        return randomHopInP2p(Predicates.always());
    }

    public static boolean randomHopInP2p(Predicate<World> predicate) {

        var current = Worlds.getCurrent();
        var newWorld = getRandomSafeWorld(w -> w.isMembers() && predicate.test(w) && w.getId() != current);
        return WorldHopper.hopTo(newWorld);
    }

    public static World getRandomSafeWorld(Predicate<World> predicate) {

        return getRandomWorld(w -> !w.isBounty()
                && !w.isDeadman()
                && !w.isHighRisk()
                && !w.isPVP()
                && !w.isSeasonDeadman()
                && !w.isSkillTotal()
                && !w.isTournament()
                && !w.isLastManStanding()
                && predicate.test(w));
    }

    public static World getRandomWorld(Predicate<World> predicate) {

        var worlds = Worlds.getLoaded(predicate);
        return Random.nextElement(worlds);
    }

}
