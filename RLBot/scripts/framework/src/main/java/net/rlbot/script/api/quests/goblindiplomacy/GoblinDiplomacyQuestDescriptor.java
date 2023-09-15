package net.rlbot.script.api.quests.goblindiplomacy;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.Set;

public class GoblinDiplomacyQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.RED_DYE, 1),
            new Tradeable(ItemId.YELLOW_DYE, 1),
            new Tradeable(ItemId.BLUE_DYE, 1),
            new Tradeable(ItemId.GOBLIN_MAIL, 3)
    );

    public GoblinDiplomacyQuestDescriptor() {
        super(GoblinDiplomacyQuestBuilder.QUEST, Collections.emptySet(), new GoblinDiplomacyQuestBuilder(), Collections.emptyList(), TRADEABLES);
    }
}