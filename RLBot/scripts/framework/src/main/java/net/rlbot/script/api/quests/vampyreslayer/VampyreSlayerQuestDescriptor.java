package net.rlbot.script.api.quests.vampyreslayer;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.persistent.EatFoodNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VampyreSlayerQuestDescriptor extends QuestDescriptorBase {

    public static final int FOOD_ITEM_ID = ItemId.SALMON;

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.HAMMER, 1),
            new Tradeable(ItemId.BEER, 1),
            new Tradeable(ItemId.IRON_FULL_HELM, 1),
            new Tradeable(ItemId.IRON_PLATEBODY, 1),
            new Tradeable(ItemId.IRON_PLATELEGS, 1),
            new Tradeable(ItemId.IRON_KITESHIELD, 1),
            new Tradeable(ItemId.IRON_SCIMITAR, 1),
            new Tradeable(ItemId.GARLIC, 1),
            new Tradeable(FOOD_ITEM_ID, 4)
    );

    public VampyreSlayerQuestDescriptor() {
        super(Quest.VAMPYRE_SLAYER, Collections.emptySet(), new VampyreSlayerQuestBuilder(), List.of(new EatFoodNode(FOOD_ITEM_ID)), TRADEABLES);
    }
}
