package net.rlbot.script.api.quests.xmarksthespot;

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

public class XMarkTheSpotQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.SPADE, 1)
    );

    public XMarkTheSpotQuestDescriptor() {
        super(XMarksTheSpotQuestBuilder.QUEST, Collections.emptySet(), new XMarksTheSpotQuestBuilder(), Collections.emptyList(), TRADEABLES);
    }

}
