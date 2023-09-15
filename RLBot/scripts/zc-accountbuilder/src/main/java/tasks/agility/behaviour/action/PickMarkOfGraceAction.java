package tasks.agility.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.agility.behaviour.Keys;

@Slf4j
public class PickMarkOfGraceAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Picking up a Mark of grace";
    }

    @Override
    public void execute() {

        var course = getBlackboard().get(Keys.COURSE);

        Pickable mark = course.getMarkOfGrace();

        if (mark == null) {
            log.warn("Unable to find mark of grace");
            Time.sleepTick();
            return;
        }

        int markCount = getMarkOfGraceCount();

        if(!mark.interact("Take") || !Time.sleepUntil(() -> getMarkOfGraceCount() > markCount, 5000))
        {
            log.warn("Failed to pick up mark of grace");
            return;
        }

        Reaction.REGULAR.sleep();
    }

    private int getMarkOfGraceCount() {
        return Inventory.getCount(true, ItemId.MARK_OF_GRACE);
    }
}
