package net.rlbot.script.api.quests.sheepshearer.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Production;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

@Slf4j
public class SpinWoolNode extends UnaryNode {

    public SpinWoolNode() {
        super("Spinning wool");
    }

    @Override
    protected ActionResult doExecute() {

        if(Inventory.getCount(ItemId.BALL_OF_WOOL) >= 20) {
            return ActionResult.SUCCESS;
        }

        if(!Production.isOpen()) {

            var spinningWheel = SceneObjects.getNearest("Spinning wheel");

            if(spinningWheel == null) {
                log.warn("Unable to find spinning wheel");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!spinningWheel.interact("Spin") || !Time.sleepUntil(Production::isOpen, () -> Players.getLocal().isMoving(), 1200)) {
                log.warn("Failed to open spin wool interact");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            return ActionResult.IN_PROGRESS;
        }

        if(!Production.chooseOption("Ball of Wool") || !Time.sleepUntil(() -> !Inventory.contains(ItemId.WOOL), () -> Players.getLocal().isAnimating(), 5000)) {
            log.warn("Failed to wait for wool to spin");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.IN_PROGRESS;
    }
}
