package tasks.woodcutting;

import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.woodcutting.behaviour.actions.ChopTreeAction;
import tasks.woodcutting.behaviour.actions.DropLogsAction;
import tasks.woodcutting.data.Keys;

import java.util.Set;

public class WoodcuttingTask implements AccountBuilderTask {

    private static final String TASK_NAME = "Woodcutting";

    private final DecisionTree behaviour;

    private final StopCondition stopCondition;

    public WoodcuttingTask(WoodcuttingTaskConfiguration configuration, StopCondition stopCondition) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                    .area(configuration.getTreeArea().getArea())
                    .loadout(getLoadout(configuration))
                    .tradeables(getTradeables(configuration))
                    .trainingNode(new ChopTreeAction())
                .build(),
                createBlackboard(configuration)
        );
        this.stopCondition = stopCondition;
    }

    private static Blackboard createBlackboard(WoodcuttingTaskConfiguration configuration) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.TREE, configuration.getTreeArea().getTree());
        blackboard.put(Keys.AXE, configuration.getAxe());
        return blackboard;
    }

    private static Loadout getLoadout(WoodcuttingTaskConfiguration configuration) {

        var builder = Loadout.builder();

        if(configuration.getAxe().canEquip()) {
            builder.withEquipmentSet()
                    .with(configuration.getAxe().getItemId()).build()
                    .build();
        } else {
          builder.withItem(configuration.getAxe().getItemId()).build();
        }

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(WoodcuttingTaskConfiguration configuration) {
        return Set.of(
                new Tradeable(configuration.getAxe().getItemId(), 1)
        );
    }

    private Class previousNode;

    @Override
    public Node getNode() {
        var node = this.behaviour.getValidNode();

        if(this.stop && previousNode == DropLogsAction.class && node.getClass() != DropLogsAction.class) {
            this.stopped = true;
            return null;
        }

        this.previousNode = node.getClass();

        return node;
    }

    @Override
    public boolean isStopped() {
        return this.stopped || stopCondition.getAsBoolean();
    }

    private boolean stopped;

    @Override
    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}
