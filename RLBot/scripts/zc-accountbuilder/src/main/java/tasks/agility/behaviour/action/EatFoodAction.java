package tasks.agility.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Health;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.agility.behaviour.Keys;

@Slf4j
public class EatFoodAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Eating";
    }

    @Override
    public void execute() {

        int currentHp = Health.getCurrent();

        var item = Inventory.getFirst(getBlackboard().get(Keys.FOOD_ITEM_ID));

        if(item.interact(a -> true) || !Time.sleepUntil(() -> Health.getCurrent() >= currentHp, 2000))
        {
            log.warn("Failed to eat food");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();

    }
}
