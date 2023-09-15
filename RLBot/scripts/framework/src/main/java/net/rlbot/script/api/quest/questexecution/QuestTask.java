package net.rlbot.script.api.quest.questexecution;

import net.rlbot.api.items.Bank;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.data.Keys;
import net.rlbot.script.api.quest.questexecution.behaviour.QuestExecutorBehaviour;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;

public class QuestTask implements Task {

    private final Blackboard blackboard;

    private final DecisionTree behaviour;

    @Override
    public Node getNode() {
        return behaviour.getValidNode();
    }

    @Override
    public boolean isDone() {
        return blackboard.get(Keys.IS_DONE);
    }

    public QuestTask(QuestDescriptor questDescriptor) {

        var quest = questDescriptor.getQuest();
        var questBuilder = questDescriptor.getQuestBuilder();

        this.blackboard = new Blackboard();
        this.blackboard.put(Keys.PERSISTENT_NODES, questDescriptor.getPersistentNodes());
        this.blackboard.put(Keys.START_NODE, questBuilder.buildQuest());
        this.blackboard.put(Keys.IS_READY, false);
        this.blackboard.put(Keys.IS_DONE, false);

        var restockingSettings = RestockingSettings.builder()
                .sellItems(false)
                .sellBeforeBuy(false)
                .tradeables(questDescriptor.getTradeables())
                .build();

        this.behaviour = QuestExecutorBehaviour.buildTree(blackboard, restockingSettings);

//        if(quest != null && Quests.getState(quest) != QuestState.NOT_STARTED) {
//            this.questContext.setDone(true);
//            this.behaviour = null;
//            return;
//        }



        if(questDescriptor.getTradeables().size() == 0) {
            this.blackboard.put(Keys.IS_READY, true);
            return;
        }

        if(Bank.isCached()) {

            if(questDescriptor.getTradeables().stream().noneMatch(Tradeable::needsBuying)) {
                this.blackboard.put(Keys.IS_READY, true);
                return;
            }

            this.blackboard.put(RestockingKeys.IS_RESTOCKING, true);
        }

        this.blackboard.put(Keys.IS_READY, true);

    }
}
