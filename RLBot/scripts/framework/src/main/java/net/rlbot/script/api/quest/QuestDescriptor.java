package net.rlbot.script.api.quest;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.List;
import java.util.Set;

public interface QuestDescriptor {

    QuestBuilder getQuestBuilder();

    List<PersistentNode> getPersistentNodes();

    Set<Requirement> getRequirements();

    Quest getQuest();

    Set<Tradeable> getTradeables();
}
