package tasks.cooking.jugsofwine.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Production;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class MakeJugsOfWineAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Making jugs of wine";
    }

    @Override
    public void execute() {

        if(Players.getLocal().isAnimating()) {
            log.trace("Waiting for the wines to be made");
            Time.sleepTick();
            return;
        }

        if(!Production.isOpen()) {

            var grapes = Inventory.getFirst(ItemId.GRAPES);

            if(grapes == null) {
                log.warn("Unable to find grapes");
                Time.sleepTick();
                return;
            }

            var jug = Inventory.getFirst(ItemId.JUG_OF_WATER);

            if(jug == null) {
                log.warn("Unable to find jug");
                Time.sleepTick();
                return;
            }

            if(!grapes.useOn(jug) || !Time.sleepUntil(Production::isOpen, 1200)) {
                log.warn("Failed to open production menu");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var amount = Math.min(Inventory.getCount(ItemId.GRAPES), Inventory.getCount(ItemId.JUG_OF_WATER));

        if(Production.getSelectedAmount() != amount) {

            log.debug("Setting amount");
            Production.setSelectedAmount(amount);
            Reaction.REGULAR.sleep();
            return;
        }

        if(!Production.chooseOption(1) || !Time.sleepUntil(() -> Players.getLocal().isAnimating(), 2400)) {
            log.warn("Failed to select option");
            Time.sleepTick();
            return;
        }
    }
}
