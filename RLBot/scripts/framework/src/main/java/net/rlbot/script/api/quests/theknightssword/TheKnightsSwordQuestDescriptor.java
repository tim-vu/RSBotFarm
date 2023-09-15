package net.rlbot.script.api.quests.theknightssword;

import net.rlbot.api.game.Skill;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.CombatLevelRequirement;
import net.rlbot.script.api.common.requirements.LevelRequirement;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestBuilder;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.quest.nodes.persistent.EatFoodNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.List;
import java.util.Set;

public class TheKnightsSwordQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Requirement> REQUIREMENTS = Set.of(
            new LevelRequirement(Skill.MINING, 10),
            new CombatLevelRequirement(25)
    );

    private static final List<PersistentNode> PERSISTENT_NODES = List.of(
            new EatFoodNode(TheKnightsSwordQuestBuilder.FOOD_ITEM_ID)
    );

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.REDBERRY_PIE, 1),
            new Tradeable(ItemId.IRON_BAR, 2),
            new Tradeable(ItemId.IRON_PICKAXE, 1),
            new Tradeable(TheKnightsSwordQuestBuilder.FOOD_ITEM_ID, 5)
    );

    public TheKnightsSwordQuestDescriptor() {
        super(TheKnightsSwordQuestBuilder.QUEST, REQUIREMENTS, new TheKnightsSwordQuestBuilder(), PERSISTENT_NODES, TRADEABLES);
    }
}
