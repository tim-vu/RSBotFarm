package net.rlbot.script.api.quests.demonslayer;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestBuilder;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.quest.nodes.persistent.EatFoodNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.List;
import java.util.Set;

public class DemonSlayerQuestDescriptor extends QuestDescriptorBase {

    public static final int FOOD_ITEM_ID = ItemId.SALMON;

    //TODO: Add combat level requirement
    private static final Set<Requirement> REQUIREMENTS = Set.of(

    );

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.BONES, 25),
            new Tradeable(FOOD_ITEM_ID, 5),
            new Tradeable(ItemId.MITHRIL_FULL_HELM, 1),
            new Tradeable(ItemId.AMULET_OF_POWER, 1),
            new Tradeable(ItemId.MITHRIL_PLATEBODY, 1),
            new Tradeable(ItemId.MITHRIL_PLATELEGS, 1),
            new Tradeable(ItemId.MITHRIL_SCIMITAR, 1),
            new Tradeable(ItemId.MITHRIL_KITESHIELD, 1),
            new Tradeable(ItemId.LEATHER_GLOVES, 1),
            new Tradeable(ItemId.LEATHER_BOOTS, 1)
    );

    public DemonSlayerQuestDescriptor() {
        super(DemonSlayerQuestBuilder.QUEST, REQUIREMENTS, new DemonSlayerQuestBuilder(), List.of(new EatFoodNode(FOOD_ITEM_ID)), TRADEABLES);
    }
}
