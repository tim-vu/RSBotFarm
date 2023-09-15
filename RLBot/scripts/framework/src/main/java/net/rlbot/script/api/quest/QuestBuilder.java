package net.rlbot.script.api.quest;

import net.rlbot.script.api.quest.nodes.QuestNode;

/**
 * Represents the object responsible for building the {@link QuestNode} graph
 */
public interface QuestBuilder {

    QuestNode buildQuest();

}
