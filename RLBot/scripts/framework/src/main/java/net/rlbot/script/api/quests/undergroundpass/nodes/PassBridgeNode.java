package net.rlbot.script.api.quests.undergroundpass.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class PassBridgeNode extends UnaryNode {

    private static final Area FIRING_AREA = Area.rectangular(2444, 9728, 2450, 9719);
    private static final Area ACROSS_THE_RIVER = Area.rectangular(2439, 9720, 2443, 9713);

    public PassBridgeNode() {
        super("Passing the bridge");
    }

    @Override
    protected ActionResult doExecute() {

        if(Equipment.contains(ItemId.WILLOW_SHORTBOW) && Equipment.contains(ItemId.IRON_FIRE_ARROW_LIT)) {

            if(!FIRING_AREA.contains(Players.getLocal())) {
                Movement.walkTo(FIRING_AREA);
                return ActionResult.IN_PROGRESS;
            }

            var guideRope = SceneObjects.getNearest("Guide rope");

            if(guideRope == null) {
                log.warn("Unable to find guide rope");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!Time.sleepUntil(() -> { var local = Players.getLocal(); return !local.isMoving() && ACROSS_THE_RIVER.contains(local); }, () -> Players.getLocal().isMoving(), 8000)) {
                log.warn("Failed to cross the bridge");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.SUCCESS;
        }

        if(Inventory.contains(ItemId.IRON_FIRE_ARROW_LIT)) {

            var item = Inventory.getFirst(ItemId.IRON_FIRE_ARROW_LIT);

            if(!item.interact("Equip") || !Time.sleepUntil(() -> Equipment.contains(ItemId.IRON_FIRE_ARROW_LIT), 1200)) {
                log.warn("Failed to equip fire arrow");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.PREDICTABLE.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(Equipment.contains(ItemId.IRON_FIRE_ARROW_LIT) && Inventory.contains(ItemId.WILLOW_SHORTBOW)) {
            var item = Inventory.getFirst(ItemId.WILLOW_SHORTBOW);

            if(!item.interact("Equip") || !Time.sleepUntil(() -> Equipment.contains(ItemId.IRON_FIRE_ARROW_LIT), 1200)) {
                log.warn("Failed to equip willow shortbow");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.PREDICTABLE.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(Inventory.contains(ItemId.IRON_FIRE_ARROW)) {

            var item = Inventory.getFirst(ItemId.IRON_FIRE_ARROW);

            var fire = SceneObjects.getNearest("Fire");

            if(fire == null) {
                log.warn("Unable to find fire");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!item.useOn(fire) || !Time.sleepUntil(() -> Inventory.contains(ItemId.IRON_FIRE_ARROW_LIT), 2400)) {
                log.warn("Failed to light fire arrow");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(Inventory.contains(ItemId.OILY_CLOTH)) {

            var cloth = Inventory.getFirst(ItemId.OILY_CLOTH);
            var arrow = Inventory.getFirst(ItemId.IRON_ARROW);

            if(arrow == null) {
                log.warn("Unable to find iron arrow");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!cloth.useOn(arrow) || !Time.sleepUntil(() -> Inventory.contains(ItemId.IRON_FIRE_ARROW), 1800)) {
                log.warn("Failed to create iron fire arrow");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(!Dialog.isOpen()) {

            var npc = Npcs.getNearest("Koftik");

            if(npc == null) {
                log.warn("Unable to find Koftik");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!npc.interact("Talk-to") || !Time.sleepUntil(Dialog::isOpen, () -> Players.getLocal().isMoving(), 1800)) {
                log.warn("Failed to talk to Koftik");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        Dialog.continueSpace();
        Reaction.PREDICTABLE.sleep();
        return ActionResult.IN_PROGRESS;
    }

}
