package tunnels;

import enums.Brother;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;

import java.util.List;

@Slf4j
public class Tunnels {
    private static final Room ROOM_0_0 = new Room(Area.rectangular(3529, 9717, 3540, 9706));
    private static final Room ROOM_0_1 = new Room(Area.rectangular(3546, 9717, 3557, 9706));
    private static final Room ROOM_0_2 = new Room(Area.rectangular(3563, 9717, 3574, 9706));
    private static final Room ROOM_1_0 = new Room(Area.rectangular(3529, 9700, 3540, 9689));
    private static final Room ROOM_1_1 = new Room(Area.rectangular(3546, 9700, 3557, 9689), true);
    private static final Room ROOM_1_2 = new Room(Area.rectangular(3563, 9700, 3574, 9689));
    private static final Room ROOM_2_0 = new Room(Area.rectangular(3529, 9683, 3540, 9672));
    private static final Room ROOM_2_1 = new Room(Area.rectangular(3546, 9683, 3557, 9672));
    private static final Room ROOM_2_2 = new Room(Area.rectangular(3563, 9683, 3574, 9672));

    public static final Area CHEST_ROOM = ROOM_1_1.getArea();

    private static final Room[][] GRAPH = {{ROOM_0_0, ROOM_0_1, ROOM_0_2}, {ROOM_1_0, ROOM_1_1, ROOM_1_2}, {ROOM_2_0, ROOM_2_1, ROOM_2_2}};

    public static final int MAX_TARGET_DISTANCE = 4;

    public static final int MAX_REWARD_POTENTIAL = 885;

    private static boolean isTargetValid(Npc npc, int rewardPotential, List<Player> allPlayers) {

        if(!npc.isAlive()) {
            return false;
        }

        var local = Players.getLocal();

        var currentRoom = getCurrentRoom();
        var monsterRoom = getCurrentRoom(npc);

        if(currentRoom != monsterRoom) {
            return false;
        }

        rewardPotential += Brother.getLivingBrothers().stream().mapToInt(Brother::getCombatLevel).sum();
        rewardPotential += npc.getCombatLevel();

        if(allPlayers.stream().anyMatch(p -> !p.equals(local) && npc.equals(p.getTarget()))) {
            return false;
        }

        if(npc.getTarget() != null && !npc.getTarget().equals(local)) {
            return false;
        }

        if(local.isMoving() && !isFacing(local, npc)) {
            return false;
        }

        //TODO: Add interactable check?
        return rewardPotential < MAX_REWARD_POTENTIAL;
    }

    public static boolean isTargetValid(Npc npc, int rewardPotential) {
        return isTargetValid(npc, rewardPotential, Players.getAll());
    }

    public static Npc getValidTarget(int rewardPotential) {

        var currentRoom = getCurrentRoom();

        List<Npc> nearMe = Npcs.query()
                .filter(npc -> npc.distance(Distance.CHEBYSHEV) <= 2)
                .filter(npc -> getCurrentRoom(npc) == currentRoom)
                .health(1)
                .results()
                .list();

        if(nearMe.size() > 1)
        {
            var targetingMe = nearMe.stream().filter(n -> n.getTarget() == Players.getLocal()).toList();

            if(targetingMe.size() == 1 && isTargetValid(targetingMe.get(0), rewardPotential)){
                return targetingMe.get(0);
            }

            List<Npc> animating = nearMe.stream().filter(n -> n.getTarget() == Players.getLocal() && n.isAnimating()).toList();

            if(animating.size() == 1 && isTargetValid(animating.get(0), rewardPotential)) {
                return animating.get(0);
            }

            return null;
        }

        Npc nearestNpc = Npcs.query()
                .filter(npc -> npc.distance(Distance.CHEBYSHEV) <= MAX_TARGET_DISTANCE)
                .filter(npc -> getCurrentRoom(npc) == currentRoom)
                .health(1)
                .results()
                .nearest(Distance.CHEBYSHEV);

        if(nearestNpc != null && isTargetValid(nearestNpc, rewardPotential))
        {
            log.info("The nearest npc is a valid target");
            return nearestNpc;
        }

        return null;
    }

    private static Room getCurrentRoom() {
        return getCurrentRoom(Players.getLocal());
    }

    private static Room getCurrentRoom(Positionable positionable) {

        for (int y = 0; y < GRAPH.length; y++) {
            for (int x = 0; x < GRAPH[0].length; x++) {

                if (GRAPH[y][x].getArea().contains(positionable))
                    return GRAPH[y][x];

            }
        }

        return null;
    }

    private static final double FOV = 90;

    private static boolean isFacing(Player local, Npc npc) {

        var orientation = local.getOrientation();

        var fx = 1 * Math.cos(orientation);
        var fy = 1 * Math.sin(orientation);

        var tx = (npc.getX() - local.getX());
        var ty = (npc.getY() - local.getY());

        var lx = Math.cos((FOV / 2) * fx) - Math.sin((FOV / 2) * fy);
        var ly = Math.sin((FOV / 2) * fx) - Math.cos((FOV / 2) * fy);

        var rx = Math.cos((-FOV / 2) * fx) - Math.sin((-FOV / 2) * fy);
        var ry = Math.sin((-FOV / 2) * fx) - Math.cos((-FOV / 2) * fy);

        return crossProduct(rx, ry, tx, ty) > 0 && crossProduct(tx, ty, lx, ly) > 0;
    }

    private static double crossProduct(double x1, double y1, double x2, double y2) {
        return x1 * y2 - x2 * y1;
    }
}
