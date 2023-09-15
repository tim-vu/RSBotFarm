package tasks.combat.common.combattask.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Health;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.combat.common.combattask.data.CombatKeys;

@Slf4j
public class EatFoodAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Eating food";
    }

    @Override
    public void execute() {

        var food = Inventory.getFirst(getBlackboard().get(CombatKeys.FOOD_ITEM_ID));

        if (food == null)
        {
            log.warn("Unable to find food");
            return;
        }

        var currentHp = Health.getCurrent();

        Action.logPerform("EAT_FOOD");
        if(!food.interact("Eat") || !Time.sleepUntil(() -> Health.getCurrent() > currentHp, 1200)) {
            Action.logFail("EAT_FOOD");
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
