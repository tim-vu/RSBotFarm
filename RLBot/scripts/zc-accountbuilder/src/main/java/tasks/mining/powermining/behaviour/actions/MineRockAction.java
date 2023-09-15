package tasks.mining.powermining.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.mining.powermining.data.Keys;

@Slf4j
public class MineRockAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Mining rock";
    }

    @Override
    public void execute() {

        var cluster = getBlackboard().get(Keys.CLUSTER);
        var rockType = getBlackboard().get(Keys.ROCK);

        var rock = SceneObjects.query()
                .within(cluster.getArea())
                .filter(rockType)
                .results()
                .nearest();

        if(rock == null) {
            log.debug("Unable to find rock");
            Time.sleepTick();
            return;
        }

        if(!rock.interact("Mine") || !Time.sleepUntil(() -> Players.getLocal().isAnimating(), () -> Players.getLocal().isMoving(), 2400)) {
            log.warn("Failed to mine rock");
            Time.sleepTick();
            return;
        }
    }
}
