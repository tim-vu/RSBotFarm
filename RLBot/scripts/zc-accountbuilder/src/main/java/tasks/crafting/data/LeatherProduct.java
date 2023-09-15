package tasks.crafting.data;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Production;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Set;

@Slf4j
public class LeatherProduct extends Product {

    @Getter
    private final int index;

    @Getter
    private final int ticks;

    public LeatherProduct(int index, int ticks) {
        this(false, index, ticks);
    }

    public LeatherProduct(boolean hardLeather, int index, int ticks) {
        super(
                ItemId.NEEDLE,
                Set.of(
                        new Resource(hardLeather ? ItemId.HARD_LEATHER : ItemId.LEATHER, 1),
                        new Resource(ItemId.THREAD, 1, 5)
                ),
                null,
                3
        );

        this.ticks = ticks;
        this.index = index;
    }

    @Override
    public void make() {

        if(!Production.isOpen()) {

            var needle = Inventory.getFirst(ItemId.NEEDLE);

            if(needle == null) {
                log.warn("Unable to find needle");
                Time.sleepTick();
                return;
            }

            var leather = Inventory.getFirst(ItemId.LEATHER, ItemId.HARD_LEATHER);

            if(leather == null) {
                log.warn("Unable to find leather");
                Time.sleepTick();
                return;
            }

            if(!needle.useOn(leather) || !Time.sleepUntil(() -> Production.isOpen() || Players.getLocal().isAnimating(), 2400)) {
                log.warn("Failed to use needle on leather");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var amount = Inventory.getCount(ItemId.LEATHER, ItemId.HARD_LEATHER);
        if(Production.getSelectedAmount() != amount) {
            Production.setSelectedAmount(amount);
            Reaction.REGULAR.sleep();
            return;
        }

        if(!Production.chooseOption(getIndex()) || !Time.sleepUntil(() -> Players.getLocal().isAnimating(), 1800)) {
            log.warn("Failed to choose product");
            Time.sleepTick();
            return;
        }
    }
}
