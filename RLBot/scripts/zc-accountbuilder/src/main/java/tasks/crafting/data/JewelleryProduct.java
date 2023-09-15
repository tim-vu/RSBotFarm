package tasks.crafting.data;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Furnace;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.Tradeable;
import tasks.crafting.enums.Facility;

import java.util.Set;

@Slf4j
public class JewelleryProduct extends Product{

    private final int productItemId;

    public JewelleryProduct(int productItemId, Loadout loadout, Set<Tradeable> tradeables, int ticks) {
        super(1, null, Facility.FURNACE, ticks);

        this.productItemId = productItemId;
    }

    @Override
    public void make() {

        if(!Furnace.isOpen()) {

            var furnace = SceneObjects.getNearest("Furnace");

            if(furnace == null) {
                log.warn("Unable to find furnace");
                Time.sleepTick();
                return;
            }

            if(!furnace.interact("Smelt") || !Time.sleepUntil(Furnace::isOpen, () -> Players.getLocal().isMoving(), 2400)) {
                log.warn("Failed to open the furnace");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
        }

        if(Furnace.getSelectedAmount() != Furnace.Amount.ALL) {

            if(!Furnace.setSelectedAmount(Furnace.Amount.ALL)) {
                log.warn("Failed to set amount");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        if(!Furnace.make(productItemId) || !Time.sleepUntil(() -> Players.getLocal().isAnimating(), 1800)) {
            log.warn("Failed to make product");
            Time.sleepTick();
            return;
        }
    }
}
