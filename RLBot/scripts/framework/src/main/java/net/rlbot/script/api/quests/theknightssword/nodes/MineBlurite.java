package net.rlbot.script.api.quests.theknightssword.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ObjectId;
import net.rlbot.script.api.quest.nodes.UnaryNode;

@Slf4j
public class MineBlurite extends UnaryNode {

    public MineBlurite() {
        super("Mining blurite");
    }

    private static final Position SAFE_SPOT = new Position(3067, 9583, 0);
    private static final Position ROCK = new Position(3067, 9582, 0);

    @Override
    protected ActionResult doExecute() {

        if(Inventory.getCount(ItemId.BLURITE_ORE) > 1) {
            return ActionResult.SUCCESS;
        }

        var player = Players.getLocal();
        if(!player.getPosition().equals(SAFE_SPOT)) {
            Movement.walkTo(SAFE_SPOT);
            return ActionResult.IN_PROGRESS;
        }

        if(player.isAnimating()) {
            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        var rock = SceneObjects.query()
                .locations(ROCK)
                .ids(ObjectId.BLURITE_ROCKS, ObjectId.BLURITE_ROCKS_11379)
                .results()
                .first();

        if(rock == null) {
            log.trace("Unable to find blurite rock");
            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        if(!rock.interact("Mine") || !Time.sleepUntil(() -> Players.getLocal().isMoving(), 1800)) {
            log.warn("Failed to mine rock");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        return ActionResult.IN_PROGRESS;
    }
}
