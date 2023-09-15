package tasks.crafting.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.crafting.data.Keys;
import tasks.crafting.data.Product;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class CraftAction extends LeafNodeBase {

    private static final int MAX_CRAFT_AMOUNT = 20;

    @Override
    public String getStatus() {
        return "Crafting";
    }

    @Override
    public void execute() {

        var product = getBlackboard().get(Keys.PRODUCT);

        if(isCrafting(product, getBlackboard().get(Keys.LAST_XP_DROP))) {
            Time.sleepTick();
            return;
        }

        product.make();
    }

    private static final Duration TICK = Duration.ofMillis(600);

    private static boolean isCrafting(Product product, Instant lastAnimationChanged) {

        if(Players.getLocal().isAnimating()) {
            return true;
        }

        return lastAnimationChanged.plus(TICK.multipliedBy(product.getTicks() + 2)).isAfter(Instant.now());
    }
}
