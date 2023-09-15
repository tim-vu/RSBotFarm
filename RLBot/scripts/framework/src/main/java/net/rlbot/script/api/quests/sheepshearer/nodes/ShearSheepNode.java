package net.rlbot.script.api.quests.sheepshearer.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

@Slf4j
public class ShearSheepNode extends UnaryNode {
    public ShearSheepNode() {
        super("Shearing sheep");
    }

    @Override
    protected ActionResult doExecute() {

        if(Inventory.getCount(ItemId.WOOL) >= 20) {
            return ActionResult.SUCCESS;
        }

        var sheep = Npcs.query()
                .names("Sheep")
                .actions("Shear")
                .results()
                .nearest();

        if(sheep == null) {
            log.warn("Unable to find sheep");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        var count = Inventory.getCount(ItemId.WOOL);
        if(!sheep.interact("Shear") || !Time.sleepUntil(() -> Inventory.getCount(ItemId.WOOL) > count, () -> Players.getLocal().isMoving(), 2400)) {
            log.warn("Failed to shear sheep");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.IN_PROGRESS;
    }
}
