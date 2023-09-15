package net.rlbot.script.api.quests.demonslayer;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;

import java.util.List;

public class DemonSlayerQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.DEMON_SLAYER;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.COINS_995).build()
                .withItem(ItemId.BONES).amount(25).build()
                .withItem(DemonSlayerQuestDescriptor.FOOD_ITEM_ID).amount(5).build()
                .withEquipmentSet()
                    .with(ItemId.MITHRIL_FULL_HELM).build()
                    .with(ItemId.AMULET_OF_POWER).build()
                    .with(ItemId.MITHRIL_PLATEBODY).build()
                    .with(ItemId.MITHRIL_PLATELEGS).build()
                    .with(ItemId.MITHRIL_SCIMITAR).build()
                    .with(ItemId.MITHRIL_KITESHIELD).build()
                    .with(ItemId.LEATHER_GLOVES).build()
                    .with(ItemId.LEATHER_BOOTS).build()
                .build()
            .build();

    private static final List<UnaryNode> NODES = List.of(
        new SetupLoadout(LOADOUT)

    );

    public DemonSlayerQuestBuilder() {
        super(NODES);
    }
}
