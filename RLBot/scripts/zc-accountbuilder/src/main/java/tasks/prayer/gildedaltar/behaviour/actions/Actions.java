package tasks.prayer.gildedaltar.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.Teleportation;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.tree.decisiontree.AnonLeafNode;
import net.rlbot.script.api.tree.decisiontree.LeafNode;
import tasks.prayer.gildedaltar.data.Constants;

@Slf4j
public class Actions {

    public static LeafNode teleportToWilderness() {
        return new AnonLeafNode(c -> {
            Teleportation.useTeleport(ItemIds.BURNING_AMULET, "Lava Maze", Constants.LAVA_MAZE);
        }, "Teleporting to the wilderness");
    }

    public static LeafNode goToGrandExchange() {
        return new AnonLeafNode(c -> Movement.walkTo(Areas.GRAND_EXCHANGE), "Going to the grand exchange");
    }

    public static LeafNode logout() {
        return new AnonLeafNode(c -> {

            if(!Game.logout()) {
                log.warn("Failed to log out");
                Time.sleepTick();
                return;
            }

            Time.sleepUntil(() -> !Game.isLoggedIn(), 2000);
        }, "Logging out");
    }

    public static LeafNode goToGildedAltar() {
        return new AnonLeafNode(c -> {
            Movement.walkTo(Constants.GILDED_ALTAR);
        }, "Going to the gilded altar");
    }
}
