package tasks.prayer.gildedaltar.behaviour;

import net.rlbot.api.scene.Players;

public class Wilderness {

    public static boolean arePlayersNearby() {
        return Players.query().within(20).results().size() > 0;
    }

}
