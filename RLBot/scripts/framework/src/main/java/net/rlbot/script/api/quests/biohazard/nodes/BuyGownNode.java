package net.rlbot.script.api.quests.biohazard.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Shop;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quests.biohazard.BiohazardQuestBuilder;
import net.rlbot.script.api.reaction.Reaction;

@Slf4j
public class BuyGownNode extends UnaryNode {

    public BuyGownNode() {
        super("Buying gown");
    }

    @Override
    protected ActionResult doExecute() {

        if(Inventory.contains(ItemId.PRIEST_GOWN) && Inventory.contains(ItemId.PRIEST_GOWN_428)) {

            if(Shop.isOpen()) {

                log.debug("Closing the shop");
                if(!Shop.close()) {
                    log.warn("Failed to close the shop");
                    Time.sleepTick();
                    return ActionResult.FAILURE;
                }
            }

            return ActionResult.SUCCESS;
        }

        if(!Shop.isOpen()) {

            log.debug("Opening the shop");

            var npc = Npcs.getNearest(BiohazardQuestBuilder.THESSALIA);

            if(npc == null) {
                log.warn("Unable to find {}", BiohazardQuestBuilder.THESSALIA);
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            if(!npc.interact("Trade") || !Time.sleepUntil(Shop::isOpen, () -> Players.getLocal().isMoving(), 1200)) {
                log.warn("Failed to open the shop");
                Time.sleepTick();
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(!Inventory.contains(ItemId.PRIEST_GOWN)) {
            buyItem(ItemId.PRIEST_GOWN);
            return ActionResult.IN_PROGRESS;
        }

        buyItem(ItemId.PRIEST_GOWN_428);
        return ActionResult.IN_PROGRESS;
    }

    private static void buyItem(int itemId) {

        if(!Shop.buyOne(itemId) || !Time.sleepUntil(() -> Inventory.contains(itemId), 1200)) {
            log.warn("Failed to buy {}", ItemDefinition.getName(itemId));
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
        return;
    }
}
