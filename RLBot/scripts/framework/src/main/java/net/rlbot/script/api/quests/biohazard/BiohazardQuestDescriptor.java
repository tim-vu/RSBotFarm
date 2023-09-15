package net.rlbot.script.api.quests.biohazard;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.persistent.UseStaminaPotionNode;
import net.rlbot.script.api.common.requirements.QuestRequirement;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.List;
import java.util.Set;

public class BiohazardQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Requirement> REQUIREMENTS = Set.of(
            new QuestRequirement(Quest.PLAGUE_CITY)
    );

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.STAMINA_POTION4, 2),
            new Tradeable(ItemId.SUPER_ENERGY4, 1),
            new Tradeable(BiohazardQuestBuilder.FOOD, 2),
            new Tradeable(ItemId.MIND_RUNE, 100),
            new Tradeable(ItemId.AIR_RUNE, 100),
            new Tradeable(ItemId.EARTH_RUNE, 100),
            new Tradeable(ItemId.SKILLS_NECKLACE4, 1),
            new Tradeable(ItemId.VARROCK_TELEPORT, 2)
    );

    public BiohazardQuestDescriptor() {
        super(BiohazardQuestBuilder.QUEST, REQUIREMENTS, new BiohazardQuestBuilder(), List.of(new UseStaminaPotionNode()), TRADEABLES);
    }
}
