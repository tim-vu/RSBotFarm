package net.rlbot.script.api.quest.nodes.common;


import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.quest.common.QuestDialog;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

import java.util.function.Supplier;

@Slf4j
public class SearchForItemNode extends UnaryNode {

    private final Supplier<SceneObject> objectSupplier;

    private final String action;

    private final int itemId;

    public SearchForItemNode(String objectName, String action, int itemId) {
        this(() -> SceneObjects.getNearest(objectName), action, itemId);
    }

    public SearchForItemNode(Supplier<SceneObject> objectSupplier, String action, int itemId) {

        super("Searching for " + itemId);
        this.objectSupplier = objectSupplier;
        this.action = action;
        this.itemId = itemId;
    }

    @Override
    protected ActionResult doExecute() {

        if (Inventory.contains(this.itemId))
        {
            return ActionResult.SUCCESS;
        }

        if (Dialog.isOpen()) {

            QuestDialog.doContinue();
            return Time.sleepUntil(() -> Inventory.contains(this.itemId), 3000) ? ActionResult.SUCCESS : ActionResult.FAILURE;
        }

        SceneObject object = this.objectSupplier.get();

        if (object == null) {
            log.warn("Unable to find object");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        if (!object.interact(this.action) || !Time.sleepUntil(() -> Dialog.isOpen() || Inventory.contains(this.itemId), () -> Players.getLocal().isMoving(), 5000))
        {
            log.warn("Failed to search for item");
            Time.sleepTick();
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.IN_PROGRESS;
    }

}
