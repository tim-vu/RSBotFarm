package net.rlbot.script.api.quests.doricsquest;

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

public class DoricsQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.CLAY, 6),
            new Tradeable(ItemId.IRON_ORE, 2),
            new Tradeable(ItemId.COPPER_ORE, 4)
    );

    public DoricsQuestDescriptor() {
        super(DoricsQuestBuilder.QUEST, Collections.emptySet(), new DoricsQuestBuilder(), Collections.emptyList(), TRADEABLES);
    }
}
