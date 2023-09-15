package net.rlbot.script.api.quests.princealirescue;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.persistent.EatFoodNode;
import net.rlbot.script.api.quest.nodes.persistent.UseStaminaPotionNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PrinceAliRescueQuestDescriptor extends QuestDescriptorBase {

    public static final int FOOD = ItemId.SALMON;

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.ROPE, 1),
            new Tradeable(ItemId.BALL_OF_WOOL, 3),
            new Tradeable(ItemId.YELLOW_DYE, 1),
            new Tradeable(ItemId.REDBERRIES, 1),
            new Tradeable(ItemId.BUCKET_OF_WATER, 1),
            new Tradeable(ItemId.POT_OF_FLOUR, 1),
            new Tradeable(ItemId.PINK_SKIRT, 1),
            new Tradeable(ItemId.ASHES, 1),
            new Tradeable(ItemId.SOFT_CLAY, 1),
            new Tradeable(ItemId.BEER, 3),
            new Tradeable(ItemId.BRONZE_BAR, 1),
            new Tradeable(FOOD, 4)
    );

    public PrinceAliRescueQuestDescriptor() {
        super(Quest.PRINCE_ALI_RESCUE, Collections.emptySet(), new PrinceAliRescueQuestBuilder(), List.of(new EatFoodNode(FOOD), new UseStaminaPotionNode()), TRADEABLES);
    }
}
