package net.rlbot.script.api.quest.questexecution.behaviour.actions;

import net.rlbot.api.Game;
import net.rlbot.api.event.listeners.EventListener;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.quest.nodes.QuestNode;
import net.rlbot.script.api.quest.nodes.common.EndNode;
import net.rlbot.script.api.quest.questexecution.data.Keys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class PerformQuestStepAction extends LeafNodeBase {

    private void setCurrentQuestNode(QuestNode value) {
        if(this.currentQuestNode == value) {
            return;
        }

        if(this.currentQuestNode != null) {

            if(this.currentQuestNode instanceof EventListener eventListener) {
                Game.getEventDispatcher().deregister(eventListener);
            }

        }

        if(value instanceof EventListener eventListener) {
            Game.getEventDispatcher().register(eventListener);
        }

        this.currentQuestNode = value;
    }

    private QuestNode getCurrentQuestNode() {

        if(currentQuestNode == null) {
            return currentQuestNode = getBlackboard().get(Keys.START_NODE);
        }

        return currentQuestNode;
    }

    private QuestNode currentQuestNode;

    @Override
    public String getStatus() {

        var persistentNode = getBlackboard().get(Keys.PERSISTENT_NODES)
                .stream()
                .filter(PersistentNode::validate)
                .findFirst();

        if(persistentNode.isPresent()) {
            return persistentNode.get().getStatus();
        }

        return getCurrentQuestNode().getStatus();
    }

    @Override
    public void execute() {

        var persistentNode = getBlackboard().get(Keys.PERSISTENT_NODES)
                .stream()
                .filter(PersistentNode::validate)
                .findFirst();

        if(persistentNode.isPresent()) {
            persistentNode.get().execute();
            return;
        }

        if(currentQuestNode.getClass().equals(EndNode.class)) {
            setCurrentQuestNode(currentQuestNode.execute());

            if(currentQuestNode == null) {
                getBlackboard().put(Keys.IS_DONE, true);
            }

            return;
        }

        setCurrentQuestNode(currentQuestNode.execute());
    }
}
