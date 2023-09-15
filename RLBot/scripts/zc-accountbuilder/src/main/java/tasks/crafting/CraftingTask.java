package tasks.crafting;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Calculations;
import net.rlbot.api.event.listeners.SkillListener;
import net.rlbot.api.event.types.SkillEvent;
import net.rlbot.api.game.Skill;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityTask;
import net.rlbot.script.api.tree.Blackboard;
import tasks.crafting.behaviour.CraftAction;
import tasks.crafting.data.Keys;
import tasks.crafting.data.Product;
import tasks.crafting.data.Resource;
import tasks.crafting.enums.Facility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class CraftingTask extends BasicActivityTask implements SkillListener {

    private static final String TASK_NAME = "Crafting";

    private final StopCondition stopCondition;

    public CraftingTask(@NonNull Product product, StopCondition stopCondition) {
        super(TASK_NAME, createBlackboard(product), getConfiguration(product));
        this.stopCondition = stopCondition;
    }

    private static BasicActivityConfiguration getConfiguration(Product product) {
        return BasicActivityConfiguration.builder()
                    .area(getArea(product))
                    .loadout(getLoadout(product))
                    .tradeables(getTradeables(product))
                    .trainingNode(new CraftAction())
                    .restocking(true)
                .build();
    }

    private static Blackboard createBlackboard(Product product) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.PRODUCT, product);
        blackboard.put(Keys.LAST_XP_DROP, Instant.MIN);
        return blackboard;
    }

    private static Loadout getLoadout(Product product) {
        var resources = product.getResources();
        var slotsRequired = resources.stream().filter(r -> !r.isStackable()).mapToInt(Resource::getCount).sum();
        var remainingSlots = Inventory.SLOTS - 1 - resources.stream().filter(Resource::isStackable).count();

        var multiplier = remainingSlots / slotsRequired;

        var builder = Loadout.builder();
        builder.withItem(product.getToolItemId());

        for(var resource : resources) {
            builder.withItem(resource.getItemId()).amount(resource.getCount(), (int)multiplier * resource.getCount());
        }

        return builder.build();
    }

    private static final int RESTOCK_AMOUNT = 200;

    public static Set<Tradeable> getTradeables(Product product) {

        var tradeables = new HashSet<Tradeable>();
        tradeables.add(new Tradeable(product.getToolItemId(), 1));

        for(var resource : product.getResources()) {
            tradeables.add(new Tradeable(resource.getItemId(), resource.getCount()));
        }

        return tradeables;
    }
    public static Area getArea(Product product){

        var facility = product.getFacility();

        if(facility == Facility.FURNACE) {
            return Area.rectangular(3105, 3501, 3110, 3496);
        }

        if(facility == Facility.POTTERY_OVEN) {
            return Area.rectangular(3082, 3411, 3087, 3407);
        }

        return Areas.GRAND_EXCHANGE;
    }
    @Override
    public void initialize() {
        Game.getEventDispatcher().register(this);
        super.initialize();
    }

    @Override
    public void terminate() {
        Game.getEventDispatcher().deregister(this);
        super.terminate();
    }

    @Override
    public boolean isDone() {
        return this.stopCondition.getAsBoolean();
    }

    @Override
    public List<String> getPaintInfo() {

        var result = new ArrayList<>(super.getPaintInfo());

        var xpGained = getExperiencedGained().getOrDefault(Skill.CRAFTING, 0);
        result.add(String.format("Xp gained (/h): %.2fk (%.2fk)", xpGained / (double)1000, Calculations.getHourlyRate(xpGained, getRuntime()) / (double)1000));

        return result;
    }

    @Override
    public void notify(SkillEvent skillEvent) {

       if(skillEvent.getSkill() != Skill.CRAFTING) {
          return;
       }

       getBlackboard().put(Keys.LAST_XP_DROP, Instant.now());
       super.notify(skillEvent);
    }
}
