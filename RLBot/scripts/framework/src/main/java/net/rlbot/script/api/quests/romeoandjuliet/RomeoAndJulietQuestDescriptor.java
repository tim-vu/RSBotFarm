package net.rlbot.script.api.quests.romeoandjuliet;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.Set;

public class RomeoAndJulietQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.CADAVA_BERRIES, 1)
    );

    public RomeoAndJulietQuestDescriptor() {
        super(RomeoAndJulietQuestBuilder.QUEST, Collections.emptySet(), new RomeoAndJulietQuestBuilder(), Collections.emptyList(), TRADEABLES);
    }
}
