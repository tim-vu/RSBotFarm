package net.rlbot.script.api.quests.impcatcher;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestBuilder;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ImpCatcherQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.RED_BEAD, 1),
            new Tradeable(ItemId.YELLOW_BEAD, 1),
            new Tradeable(ItemId.BLACK_BEAD, 1),
            new Tradeable(ItemId.WHITE_BEAD, 1)
    );

    public ImpCatcherQuestDescriptor() {
        super(ImpCatcherQuestBuilder.QUEST, Collections.emptySet(), new ImpCatcherQuestBuilder(), Collections.emptyList(), TRADEABLES);
    }
}
