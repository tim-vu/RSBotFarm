package net.rlbot.script.api.quests.fightarena;

import net.rlbot.api.game.Skill;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.persistent.EatFoodNode;
import net.rlbot.script.api.quest.nodes.persistent.UseStaminaPotionNode;
import net.rlbot.script.api.common.requirements.LevelRequirement;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.List;
import java.util.Set;

public class FightArenaQuestDescriptor extends QuestDescriptorBase {


    public static final Set<Requirement> REQUIREMENTS = Set.of(
            new LevelRequirement(Skill.MAGIC, 13),
            new LevelRequirement(Skill.HITPOINTS, 22)
    );

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.STAMINA_POTION4, 1),
            new Tradeable(FightArenaQuestBuilder.FOOD, 6),
            new Tradeable(ItemId.MIND_RUNE, 300),
            new Tradeable(ItemId.AIR_RUNE, 500),
            new Tradeable(ItemId.EARTH_RUNE, 300),
            new Tradeable(ItemId.STAFF_OF_FIRE, 1),
            new Tradeable(ItemId.VARROCK_TELEPORT, 1)
    );

    public FightArenaQuestDescriptor() {
        super(
                FightArenaQuestBuilder.QUEST,
                REQUIREMENTS,
                new FightArenaQuestBuilder(),
                List.of(new UseStaminaPotionNode(), new EatFoodNode(FightArenaQuestBuilder.FOOD)),
                TRADEABLES
        );
    }
}
