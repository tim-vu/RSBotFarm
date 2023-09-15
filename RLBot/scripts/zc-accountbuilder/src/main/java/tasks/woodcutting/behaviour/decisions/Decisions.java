package tasks.woodcutting.behaviour.decisions;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tasks.woodcutting.data.Keys;

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

    public static Decision isChopping() {
        return b -> {

            var tree = b.get(Keys.TREE);
            var treePosition = b.get(Keys.CURRENT_TREE_POSITION);

            return Players.getLocal().isAnimating() && treePosition != null && SceneObjects.getFirstAt(treePosition, o -> tree.getNames().contains(o.getName())) != null;
        };
    }
}
