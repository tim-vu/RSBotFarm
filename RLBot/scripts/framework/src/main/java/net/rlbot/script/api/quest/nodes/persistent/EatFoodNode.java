package net.rlbot.script.api.quest.nodes.persistent;

import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Health;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Collections;
import java.util.List;

public class EatFoodNode implements PersistentNode {

    private static final int MINIMUM_HEALTH_PERCENTAGE = 60;

    private final List<Integer> foodItemIds;

    public EatFoodNode(List<Integer> foodItemIds) {

        this.foodItemIds = foodItemIds;
    }

    public EatFoodNode(int itemId) {

        this.foodItemIds = Collections.singletonList(itemId);
    }

    private List<Integer> getFoodItemIds() {

        return Collections.unmodifiableList(foodItemIds);
    }

    @Override
    public boolean validate() {

        return Health.getPercent() < MINIMUM_HEALTH_PERCENTAGE && Inventory.contains(i -> getFoodItemIds().contains(i.getId()));
    }

    @Override
    public String getStatus() {

        return "Eating food";
    }

    @Override
    public void execute() {

        int itemId = 0;

        //TODO: Remove duplication
        for (var i = getFoodItemIds().size() - 1; i >= 0; i--) {

            var dosedItemId = getFoodItemIds().get(i);

            if (Inventory.getCount(dosedItemId) == 0) {
                continue;
            }

            itemId = dosedItemId;
            break;
        }

        if (itemId == -1) {
            return;
        }

        Item food = Inventory.getFirst(itemId);

        int currentHp = Health.getCurrent();


        if(!food.interact(Predicates.always()) || !Time.sleepUntil(() -> Health.getCurrent() > currentHp, 3000)) {
            return;
        }

        Reaction.REGULAR.sleep();
    }

}
