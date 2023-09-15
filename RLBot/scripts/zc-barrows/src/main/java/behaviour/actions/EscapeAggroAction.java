package behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class EscapeAggroAction extends LeafNodeBase {

    private static final List<Position> CORNERS = List.of(
            new Position(3547, 9690, 0),
            new Position(3556, 9690, 0),
            new Position(3547, 9699, 0),
            new Position(3556, 9699, 0)
    );

    @Override
    public String getStatus() {
        return "Escaping aggro";
    }

    @Override
    public void execute() {

        var local = Players.getLocal();
        var npc = Npcs.query()
                .filter(n -> local.equals(n.getTarget()))
                .within(3)
                .results()
                .first();

        if(npc == null) {
            log.warn("No aggressive monster when trying to escape aggro");
            Time.sleepTick();
            return;
        }

        var destination = CORNERS.get(0);

        for(var corner : CORNERS) {

            if(corner.distanceTo(npc) < destination.distanceTo(npc)) {
                continue;
            }

            destination = corner;
        }

        var position = local.getPosition();
        Movement.walkTo(destination);
        Time.sleepUntil(() -> position.distance() > 2, 4000);
    }
}
