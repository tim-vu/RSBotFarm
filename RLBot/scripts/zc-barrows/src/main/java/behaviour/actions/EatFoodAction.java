package behaviour.actions;

import data.Keys;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.loadout.Supply;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class EatFoodAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Eating";
    }

    @Override
    public void execute() {

        var item = Supply.getLowestDose(getBlackboard().get(Keys.SETTINGS).getFoodItemIds());

        if(item == null) {
            log.warn("No food when trying to eat");
            Time.sleepTick();
            return;
        }

        Action.logPerform("EAT");
        if(!item.interact("Eat")) {
            Action.logFail("EAT");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Players.getLocal().isAnimating(), 1200)) {
            Action.logTimeout("EAT");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
