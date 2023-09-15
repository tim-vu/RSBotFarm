package tasks.fishing.powerfishing.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.fishing.powerfishing.data.Keys;

@Slf4j
public class FishAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Fishing";
    }

    @Override
    public void execute() {

        var fishingSpot = getBlackboard().get(Keys.FISHING_SPOT);

        var npc = Npcs.query()
                .nameContains("Fishing spot")
                .within(fishingSpot.getArea())
                .results()
                .nearest();

        if(npc == null) {
            log.warn("Unable to find fishingspot");
            Time.sleepTick();
            return;
        }

        if(npc.distance() > 8) {
            Movement.walk(npc);
            Time.sleepTick();
            return;
        }

        if(!npc.interact("Net", "Lure", "Use-rod") || !Time.sleepUntil(() -> Players.getLocal().isAnimating(), () -> Players.getLocal().isMoving(), 2400)) {
            log.warn("Failed to start fishing");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
