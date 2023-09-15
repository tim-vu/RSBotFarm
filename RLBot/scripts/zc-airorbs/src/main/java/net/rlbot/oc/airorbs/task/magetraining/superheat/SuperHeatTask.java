package net.rlbot.oc.airorbs.task.magetraining.superheat;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.oc.airorbs.task.magetraining.superheat.behaviour.CastSuperHeatItemAction;
import net.rlbot.oc.airorbs.task.magetraining.superheat.data.Keys;
import net.rlbot.oc.airorbs.task.magetraining.superheat.enums.Bar;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityTask;
import net.rlbot.script.api.tree.Blackboard;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

@Slf4j
public class SuperHeatTask extends BasicActivityTask {

    private static final int RESTOCK_AMOUNT = 1000;

    private static final String TASK_NAME = "Superheat item";


    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.STAFF_OF_FIRE, 1),
            new Tradeable(ItemId.NATURE_RUNE, RESTOCK_AMOUNT)
    );

    private final BooleanSupplier stopCondition;

    public SuperHeatTask(@NonNull Bar bar, @NonNull BooleanSupplier stopCondition) {
        super(
                TASK_NAME,
                createBlackboard(bar),
                BasicActivityConfiguration.builder()
                            .area(Areas.GRAND_EXCHANGE)
                            .loadout(getLoadout(bar))
                            .tradeables(getTradeables(bar))
                            .trainingNode(new CastSuperHeatItemAction())
                        .build()
        );
        this.stopCondition = stopCondition;
    }

    private static Blackboard createBlackboard(Bar bar) {
        var blackboard = new Blackboard();
        //noinspection OptionalGetWithoutIsPresent
        blackboard.put(Keys.ORE_ITEM_ID, bar.getInventorySupplies().stream().findAny().get().getItemIds().get(0));
        return blackboard;
    }

    private static Loadout getLoadout(Bar bar){

        var builder = Loadout.builder()
                        .withEquipmentSet()
                            .with(ItemId.STAFF_OF_FIRE).build()
                            .build()
                        .withItem(ItemId.NATURE_RUNE).amount(1, Integer.MAX_VALUE).build();

        for(var supply : bar.getInventorySupplies()){
            builder.with(supply);
        }

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(Bar bar){
        Set<Tradeable> tradeables = new HashSet<>(TRADEABLES);

        for(var supply : bar.getInventorySupplies()){
            tradeables.add(new Tradeable(supply.getItemIds().get(0), RESTOCK_AMOUNT));
        }

        tradeables.add(new Tradeable(bar.getBarItemId(), 0));

        return tradeables;
    }


    @Override
    public boolean isDone() {
        return stopCondition.getAsBoolean();
    }
}
