package net.rlbot.script.api.task.common.basicactivitytask;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.task.TaskBase;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.Behaviour;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;

public abstract class BasicActivityTask extends TaskBase {

    private final DecisionTree behaviour;

    @Getter(AccessLevel.PROTECTED)
    private final Blackboard blackboard;

    public BasicActivityTask(
            @NonNull String taskName,
            @NonNull Blackboard blackboard,
            @NonNull BasicActivityConfiguration configuration) {
        super(taskName);

        blackboard.put(BasicActivityKeys.LOADOUT, configuration.getLoadout());
        blackboard.put(BasicActivityKeys.TRAINING_AREA, configuration.getArea());
        blackboard.put(BasicActivityKeys.HAS_LOADOUT, new TempMemory<>(() -> LoadoutManager.hasLoadout(configuration.getLoadout()), 5000));

        var restockingSettings = RestockingSettings.builder()
                .sellBeforeBuy(false)
                .sellItems(true)
                .tradeables(configuration.getTradeables())
                .build();


        this.behaviour = Behaviour.buildTree(blackboard, restockingSettings, configuration.getTrainingNode());

        blackboard.put(RestockingKeys.IS_RESTOCKING, configuration.isRestocking());

        this.blackboard = blackboard;
    }

    @Override
    public Node getNode(){
        return this.behaviour.getValidNode();
    }
}
