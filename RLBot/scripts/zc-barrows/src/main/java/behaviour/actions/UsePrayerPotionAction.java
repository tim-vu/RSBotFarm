package behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Prayers;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.Supply;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class UsePrayerPotionAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Using prayer potion";
    }

    @Override
    public void execute() {

        var item = Supply.getLowestDose(ItemIds.PRAYER_POTION);

        if(item == null) {
            log.warn("No prayer potion when trying to use prayer potion");
            Time.sleepTick();
            return;
        }

        var points = Prayers.getPoints();
        Action.logPerform("USE_PRAYER_POTION");
        if(!item.interact("Drink")) {
            Action.logFail("USE_PRAYER_POTION");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Prayers.getPoints() > points, 1800)) {
            Action.logTimeout("USE_PRAYER_POTION");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
