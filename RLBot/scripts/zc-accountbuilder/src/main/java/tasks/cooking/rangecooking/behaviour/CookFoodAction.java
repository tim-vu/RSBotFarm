package tasks.cooking.rangecooking.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Production;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.cooking.rangecooking.data.Keys;

@Slf4j
public class CookFoodAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Cooking";
    }

    @Override
    public void execute() {

        if(Players.getLocal().isAnimating()) {
            Time.sleepTick();
            return;
        }

        if(!Production.isOpen()) {

            var range = getBlackboard().get(Keys.RANGE);
            var object = SceneObjects.query()
                    .names(range.getName())
                    .within(range.getArea())
                    .results()
                    .nearest();

            if(object == null) {
                log.warn("Unable to find range");
                Time.sleepTick();
                return;
            }

            if(!object.interact("Cook") || !Time.sleepUntil(Production::isOpen, () -> Players.getLocal().isMoving(), 2400)) {
                log.warn("Failed to open production menu");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var food = getBlackboard().get(Keys.FOOD);
        var foodCount = Inventory.getCount(food.getRawItemId());
        if(Production.getSelectedAmount() != foodCount) {
            log.debug("Setting amount");
            Production.setSelectedAmount(foodCount);
            Reaction.REGULAR.sleep();
            return;
        }

        if(!Production.chooseOption(1) || !Time.sleepUntil(() -> Players.getLocal().isAnimating(), 1200)) {
            log.warn("Failed to select option");
            Time.sleepTick();
            return;
        }
    }
}
