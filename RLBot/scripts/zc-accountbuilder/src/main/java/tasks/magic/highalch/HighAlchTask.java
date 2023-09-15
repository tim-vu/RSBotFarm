package tasks.magic.highalch;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.magic.highalch.behaviour.HighAlchAction;
import tasks.magic.highalch.data.Keys;

import java.util.HashSet;
import java.util.Set;

public class HighAlchTask implements AccountBuilderTask {

    private final DecisionTree behaviour;

    public HighAlchTask(HighAlchTaskConfiguration configuration) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                        .trainingNode(new HighAlchAction())
                        .loadout(getLoadout(configuration))
                        .tradeables(getTradeables(configuration))
                        .extraItemIds(Set.of(ItemId.COINS_995))
                        .build(),
                createBlackboard(configuration)
        );
    }

    private static Blackboard createBlackboard(HighAlchTaskConfiguration configuration) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.ITEM_ID, configuration.getItemId());
        return blackboard;
    }

    private static Loadout getLoadout(HighAlchTaskConfiguration configuration) {
        var builder = Loadout.builder();
        builder.withItem(configuration.getItemId()).noted().amount(1, Integer.MAX_VALUE).build();
        builder.withItem(ItemId.NATURE_RUNE).amount(1, Integer.MAX_VALUE).build();
        builder.withEquipmentSet()
                    .with(ItemId.STAFF_OF_FIRE).build()
                .build();
        return builder.build();
    }

    private static Set<Tradeable> getTradeables(HighAlchTaskConfiguration configuration) {
        var result = new HashSet<Tradeable>();

        result.add(new Tradeable(ItemId.STAFF_OF_FIRE, 1));
        result.add(new Tradeable(ItemId.NATURE_RUNE, configuration.getRestockAmount()));
        result.add(new Tradeable(configuration.getItemId(), configuration.getRestockAmount()));

        return result;
    }

    @Override
    public Node getNode() {

        if(this.stop) {
            this.stopped = true;
            return null;
        }

        return this.behaviour.getValidNode();
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    private boolean stopped;

    @Override
    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}
