package tasks.magic.superheat;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.magic.superheat.behaviour.SuperHeatAction;
import tasks.magic.superheat.data.Keys;

import java.util.HashSet;
import java.util.Set;

public class SuperHeatTask implements AccountBuilderTask {

    private final DecisionTree behaviour;

    public SuperHeatTask(SuperHeatTaskConfiguration configuration) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                        .loadout(getLoadout(configuration))
                        .tradeables(getTradeables(configuration))
                        .extraItemIds(Set.of(configuration.getBar().getBarItemId()))
                        .trainingNode(new SuperHeatAction())
                        .build(),
                createBlackboard(configuration)
        );
    }

    private static Blackboard createBlackboard(SuperHeatTaskConfiguration configuration) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.ORE_ITEM_ID, configuration.getBar().getInventorySupplies().stream().findFirst().get().getItemIds().get(0));
        return blackboard;
    }

    private static Loadout getLoadout(SuperHeatTaskConfiguration configuration){

        var builder = Loadout.builder()
                .withEquipmentSet()
                .with(ItemId.STAFF_OF_FIRE).build()
                .build()
                .withItem(ItemId.NATURE_RUNE).amount(1, Integer.MAX_VALUE).build();

        for(var supply : configuration.getBar().getInventorySupplies()){
            builder.with(supply);
        }

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(SuperHeatTaskConfiguration configuration){
        var tradeables = new HashSet<Tradeable>();

        tradeables.add(new Tradeable(ItemId.STAFF_OF_FIRE, 1));
        tradeables.add(new Tradeable(ItemId.NATURE_RUNE, configuration.getRestockAmount()));

        for(var supply : configuration.getBar().getInventorySupplies()){
            tradeables.add(new Tradeable(supply.getItemIds().get(0), supply.getMinimumAmount() * configuration.getRestockAmount()));
        }

        tradeables.add(new Tradeable(configuration.getBar().getBarItemId(), 0));

        return tradeables;
    }

    @Override
    public Node getNode() {

        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == SuperHeatAction.class) {
            this.stopped = true;
            return null;
        }

        return node;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    private boolean stopped;

    @Override
    public void signalStop() {

    }

    private boolean stop;
}
