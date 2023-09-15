package net.rlbot.script.api.quests.witchspotion;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.Set;

public class WitchsPotionQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.ONION, 1),
            new Tradeable(ItemId.EYE_OF_NEWT, 1),
            new Tradeable(ItemId.COOKED_MEAT, 1)
    );

    public WitchsPotionQuestDescriptor() {
        super(WitchsPotionQuestBuilder.QUEST, Collections.emptySet(), new WitchsPotionQuestBuilder(), Collections.emptyList(), TRADEABLES);
    }
}
