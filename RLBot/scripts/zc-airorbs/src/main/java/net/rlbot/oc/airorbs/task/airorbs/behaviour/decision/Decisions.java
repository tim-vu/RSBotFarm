package net.rlbot.oc.airorbs.task.airorbs.behaviour.decision;


import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.BankLocation;
import net.rlbot.api.game.*;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.DangerousPlayers;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.tree.decisiontree.Decision;

@Slf4j
public class Decisions {

    public static Decision hasGold() {
        return c -> c.get(Keys.HAS_GOLD);
    }

    public static Decision hopToMemberWorld() {
        return c -> !Worlds.isInMemberWorld();
    }

    public static Decision hasLoadout() {
        return c -> LoadoutManager.hasLoadout(Constants.LOADOUT);
    }

    public static Decision replenishStats() {
        return c -> Health.getPercent() < 100 ||
                Movement.getRunEnergy() < 100 ||
                Prayers.getPoints() < Skills.getLevel(Skill.PRAYER);
    }

    public static Decision isAtFeroxEnclave() {
        return c -> Constants.FEROX_ENCLAVE.contains(Players.getLocal());
    }

    public static Decision isAtBank() {
        return c -> BankLocation.getNearest().getArea().contains(Players.getLocal());
    }

    public static Decision isNearMule() {
        return c -> {
            var mulingRequest = c.get(Keys.MULING_REQUEST);

            if(mulingRequest.getWorld() != Worlds.getCurrent()){
                log.debug("Not in the same world as the mule. Mule is in world {} and we are in world {}", mulingRequest.getWorld(), Worlds.getCurrent());
                return false;
            }

            var position = new Position(mulingRequest.getPosition().getX(), mulingRequest.getPosition().getY(), mulingRequest.getPosition().getZ());

            log.debug("Distance to mule: {}", position.distance());
            return position.distance() < 10;
        };
    }

    public static Decision isCharging() {
        return c-> c.get(Keys.IS_CHARGING);
    }

    public static Decision shouldEscape() {
        return DangerousPlayers::shouldEscape;
    }

    public static Decision isAtObelisk() {
        return c -> Constants.OBELISK.contains(Players.getLocal());
    }

    public static Decision isRestocking() {
        return c -> c.get(Keys.IS_RESTOCKING);
    }
}
