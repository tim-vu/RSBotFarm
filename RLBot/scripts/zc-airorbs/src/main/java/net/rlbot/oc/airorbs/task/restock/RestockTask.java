package net.rlbot.oc.airorbs.task.restock;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.oc.airorbs.task.restock.behaviour.RestockBehaviour;
import net.rlbot.oc.airorbs.task.restock.data.Keys;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;

import java.util.List;
import java.util.Set;

@Slf4j
public class RestockTask implements Task {

    private final DecisionTree behaviour;

    private final Blackboard blackboard;

    @Override
    public Node getNode() {
        return behaviour.getValidNode();
    }

    @Override
    public boolean isDone() {
        return this.blackboard.get(Keys.IS_DONE);
    }

    @Override
    public List<String> getPaintInfo(){
        return List.of(
                "Current task: Restocking"
        );
    }

    public RestockTask(Set<Tradeable> tradeables){

        this.blackboard = new Blackboard();
        this.blackboard.put(Keys.IS_DONE, false);

        var restockingSettings = RestockingSettings.builder()
                .sellItems(false)
                .sellBeforeBuy(false)
                .tradeables(tradeables)
                .build();

        this.behaviour = RestockBehaviour.buildTree(blackboard, restockingSettings);
        tradeables.forEach(t -> log.info(t.toString()));
    }
}
