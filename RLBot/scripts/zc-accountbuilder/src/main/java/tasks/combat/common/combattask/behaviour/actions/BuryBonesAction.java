package tasks.combat.common.combattask.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class BuryBonesAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Burying bones";
    }

    @Override
    public void execute() {
        var bone = Inventory.getFirst(ItemId.BONES, ItemId.BIG_BONES);

        if(bone == null) {
            log.warn("Unable to find bones");
            Time.sleepTick();
            return;
        }

        var boneCount = Inventory.getCount(bone.getId());
        if(!bone.interact("Bury") || !Time.sleepUntil(() -> Inventory.getCount(bone.getId()) < boneCount, 2400)) {
            log.warn("Failed to bury bones");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
