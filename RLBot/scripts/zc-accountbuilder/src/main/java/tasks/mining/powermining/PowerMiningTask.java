package tasks.mining.powermining;

import net.rlbot.api.common.math.Random;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.mining.powermining.behaviour.Behaviour;
import tasks.mining.powermining.behaviour.actions.DropOreAction;
import tasks.mining.powermining.behaviour.actions.MineRockAction;
import tasks.mining.powermining.data.Keys;

import java.util.Set;

public class PowerMiningTask implements AccountBuilderTask {

    private static final String TASK_NAME = "Powermining";

    private final DecisionTree behaviour;

    private final StopCondition stopCondition;

    public PowerMiningTask(PowerMiningTaskConfiguration configuration, StopCondition stopCondition) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                        .area(configuration.getMiningArea().getArea())
                        .loadout(getLoadout(configuration))
                        .tradeables(getTradeables(configuration))
                        .trainingNode(Behaviour.buildTree())
                .build(),
                createBlackboard(configuration)
        );
        this.stopCondition = stopCondition;
    }

    private static Blackboard createBlackboard(PowerMiningTaskConfiguration configuration) {
        var blackboard = new Blackboard();
        var clusters = configuration.getMiningArea().getClusters().stream().filter(c -> c.getRock() == configuration.getRock()).toList();
        var cluster = Random.nextElement(clusters);
        blackboard.put(Keys.CLUSTER, cluster);
        blackboard.put(Keys.ROCK, configuration.getRock());
        blackboard.put(Keys.IS_DROPPING, false);
        blackboard.put(Keys.PICKAXE, configuration.getPickaxe());
        return blackboard;
    }

    private static Loadout getLoadout(PowerMiningTaskConfiguration configuration) {

        var builder = Loadout.builder();

        if(configuration.getPickaxe().canEquip()) {
            builder.withEquipmentSet()
                    .with(configuration.getPickaxe().getItemId()).build()
                    .build();
        } else {
            builder.withItem(configuration.getPickaxe().getItemId()).build();
        }

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(PowerMiningTaskConfiguration configuration) {
        return Set.of(
                new Tradeable(configuration.getPickaxe().getItemId(), 1)
        );
    }

    private Class previousNode;

    @Override
    public Node getNode() {
        var node = this.behaviour.getValidNode();

        if(this.stop && this.previousNode == DropOreAction.class && node.getClass() != MineRockAction.class) {
            this.stopped = true;
            return null;
        }

        this.previousNode = node.getClass();

        return node;
    }

    @Override
    public boolean isStopped() {
        return this.stopped || this.stopCondition.getAsBoolean();
    }

    private boolean stopped;

    @Override
    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}
