package net.rlbot.script.api.quest;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class QuestDescriptorBase implements QuestDescriptor {

    @Override
    public QuestBuilder getQuestBuilder() {

        return questBuilder;
    }

    private final QuestBuilder questBuilder;

    @Override
    public List<PersistentNode> getPersistentNodes() {

        return Collections.unmodifiableList(persistentNodes);
    }

    private final List<PersistentNode> persistentNodes;

    @Override
    public Set<Requirement> getRequirements() {

        return Collections.unmodifiableSet(requirements);
    }
    private final Set<Requirement> requirements;

    @Override
    public Quest getQuest() {

        return gameQuest;
    }

    private final Quest gameQuest;

    public Set<Tradeable> getTradeables() {
        return Collections.unmodifiableSet(tradeables);
    }

    private final Set<Tradeable> tradeables;

    public QuestDescriptorBase(Quest gameQuest, Set<Requirement> requirements, QuestBuilder questBuilder, List<PersistentNode> persistentNodes, Set<Tradeable> tradeables) {

        this.gameQuest = gameQuest;
        this.requirements = requirements;
        this.questBuilder = questBuilder;
        this.persistentNodes = persistentNodes;
        this.tradeables = tradeables;
    }

    public QuestDescriptorBase(Quest gameQuest, QuestBuilder questBuilder) {

        this(gameQuest, Collections.emptySet(), questBuilder, Collections.emptyList(), Collections.emptySet());

    }
}
