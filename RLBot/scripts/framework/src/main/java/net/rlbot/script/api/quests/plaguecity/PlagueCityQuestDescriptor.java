package net.rlbot.script.api.quests.plaguecity;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.persistent.UseStaminaPotionNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PlagueCityQuestDescriptor extends QuestDescriptorBase {

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.DWELLBERRIES, 1),
            new Tradeable(ItemId.ROPE, 1),
            new Tradeable(ItemId.BUCKET_OF_WATER, 4),
            new Tradeable(ItemId.BUCKET_OF_MILK, 1),
            new Tradeable(ItemId.CHOCOLATE_DUST, 1),
            new Tradeable(ItemId.SNAPE_GRASS, 1),
            new Tradeable(ItemId.SPADE, 1),
            new Tradeable(ItemId.STAMINA_POTION4, 1),
            new Tradeable(ItemId.VARROCK_TELEPORT, 1)
    );

    public PlagueCityQuestDescriptor() {
        super(PlagueCityQuestBuilder.QUEST, Collections.emptySet(), new PlagueCityQuestBuilder(), List.of(new UseStaminaPotionNode()), TRADEABLES);
    }
}
