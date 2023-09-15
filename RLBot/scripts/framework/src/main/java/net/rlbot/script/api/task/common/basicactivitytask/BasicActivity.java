package net.rlbot.script.api.task.common.basicactivitytask;

import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.Behaviour;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;

public class BasicActivity {

    public static DecisionTree createBehaviour(BasicActivityConfiguration configuration, Blackboard blackboard) {

        blackboard.put(BasicActivityKeys.LOADOUT, configuration.getLoadout());
        blackboard.put(BasicActivityKeys.TRAINING_AREA, configuration.getArea());
        blackboard.put(BasicActivityKeys.HAS_LOADOUT, new TempMemory<>(() -> LoadoutManager.hasLoadout(configuration.getLoadout(), configuration.getExtraItemIds()), 5000));

        var restockingSettings = RestockingSettings.builder()
                .sellBeforeBuy(false)
                .sellItems(true)
                .tradeables(configuration.getTradeables())
                .build();

        var behaviour = Behaviour.buildTree(blackboard, restockingSettings, configuration.getTrainingNode());

        blackboard.put(RestockingKeys.IS_RESTOCKING, configuration.isRestocking());

        return behaviour;
    }
}
