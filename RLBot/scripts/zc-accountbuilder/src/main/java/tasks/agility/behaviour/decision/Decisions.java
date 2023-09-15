package tasks.agility.behaviour.decision;

import net.rlbot.api.game.Health;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tasks.agility.behaviour.Keys;

public class Decisions {

    public static Decision isOnCourse() {
        return c -> c.get(Keys.COURSE).isOnCourse();
    }

    public static Decision shouldEat() {
        return c -> Health.getPercent() <= 40 && Inventory.contains(c.get(Keys.FOOD_ITEM_ID));
    }

    public static Decision isMarkOfGraceAvailable() {
        return c -> c.get(Keys.COURSE).getMarkOfGrace() != null;
    }
}
