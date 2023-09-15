package net.rlbot.script.api.quests.undergroundpass;

import net.rlbot.api.game.Skill;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.LevelRequirement;
import net.rlbot.script.api.common.requirements.QuestRequirement;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.quest.nodes.persistent.EatFoodNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.List;
import java.util.Set;

public class UndergroundPassQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Requirement> REQUIREMENTS = Set.of(
            new QuestRequirement(Quest.BIOHAZARD),
            new LevelRequirement(Skill.RANGED, 25),
            new LevelRequirement(Skill.ATTACK, 40),
            new LevelRequirement(Skill.DEFENCE, 40),
            new LevelRequirement(Skill.AGILITY, 50)
    );

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.WILLOW_SHORTBOW, 1),
            new Tradeable(ItemId.IRON_ARROW, 5),
            new Tradeable(ItemId.ROPE, 2),
            new Tradeable(ItemId.SPADE, 1),
            new Tradeable(ItemId.STAMINA_POTION4, 3),
            new Tradeable(ItemId.RUNE_FULL_HELM, 1),
            new Tradeable(ItemId.AMULET_OF_POWER, 1),
            new Tradeable(ItemId.RUNE_CHAINBODY, 1),
            new Tradeable(ItemId.RUNE_KITESHIELD, 1),
            new Tradeable(ItemId.RUNE_PLATELEGS, 1),
            new Tradeable(ItemId.LEATHER_GLOVES, 1),
            new Tradeable(ItemId.LEATHER_BOOTS, 1),
            new Tradeable(ItemId.ARDOUGNE_TELEPORT, 1),
            new Tradeable(ItemId.WEST_ARDOUGNE_TELEPORT, 1)
    );

    private static final List<PersistentNode> PERSISTENT_NODES = List.of(
            new EatFoodNode(UndergroundPassQuestBuilder.FOOD_ITEM_ID)
    );

    public UndergroundPassQuestDescriptor() {
        super(UndergroundPassQuestBuilder.QUEST, REQUIREMENTS, new UndergroundPassQuestBuilder(), PERSISTENT_NODES, TRADEABLES);
    }

}
