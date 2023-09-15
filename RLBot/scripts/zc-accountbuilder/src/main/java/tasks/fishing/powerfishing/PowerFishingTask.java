package tasks.fishing.powerfishing;

import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.fishing.powerfishing.behaviour.Behaviour;
import tasks.fishing.powerfishing.behaviour.actions.DropFishAction;
import tasks.fishing.powerfishing.data.Keys;
import tasks.fishing.powerfishing.enums.FishingSpot;

public class PowerFishingTask implements AccountBuilderTask {

    private static final String TASK_NAME = "Powerfishing";

    private final DecisionTree behaviour;

    private final StopCondition stopCondition;

    public PowerFishingTask(PowerFishingTaskConfiguration configuration, StopCondition stopCondition) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                        .area(configuration.getFishingSpot().getArea())
                        .loadout(configuration.getFishingSpot().getFishingType().getLoadout())
                        .tradeables(configuration.getFishingSpot().getFishingType().getTradeables())
                        .trainingNode(Behaviour.buildTree())
                        .build(),
                createBlackboard(configuration.getFishingSpot())
        );
        this.stopCondition = stopCondition;
    }

    private static Blackboard createBlackboard(FishingSpot fishingSpot) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.FISHING_SPOT, fishingSpot);
        return blackboard;
    }


    private Class previousNode;

    @Override
    public Node getNode() {
        var node = this.behaviour.getValidNode();

        if(this.stop && previousNode == DropFishAction.class && node.getClass() != DropFishAction.class) {
            this.stopped = true;
            return null;
        }

        previousNode = node.getClass();

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