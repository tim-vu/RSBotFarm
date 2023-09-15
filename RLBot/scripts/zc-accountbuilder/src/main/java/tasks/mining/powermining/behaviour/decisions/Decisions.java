package tasks.mining.powermining.behaviour.decisions;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tasks.mining.powermining.data.Keys;

public class Decisions {

    public static Decision isDropping() {
        return b -> {
            if(b.get(Keys.IS_DROPPING)) {
                return true;
            }

            if(!Inventory.isFull()) {
                return false;
            }

            b.put(Keys.IS_DROPPING, true);
            return true;
        };
    }

    public static Decision isMining() {
        return b -> Players.getLocal().isAnimating();
    }
}
