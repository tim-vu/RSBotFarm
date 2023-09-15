package net.rlbot.script.api.quest.nodes;

/**
 * Represents a single step in the quest
 */
public interface QuestNode {

    QuestNode execute();

    String getStatus();

}
