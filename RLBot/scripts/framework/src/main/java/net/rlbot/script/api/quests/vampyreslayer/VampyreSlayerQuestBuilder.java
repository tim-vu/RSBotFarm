package net.rlbot.script.api.quests.vampyreslayer;


import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.Npcs;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CombatNode;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class VampyreSlayerQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.VAMPYRE_SLAYER;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.HAMMER).build()
                .withItem(ItemId.BEER).build()
                .withItem(VampyreSlayerQuestDescriptor.FOOD_ITEM_ID).amount(4).build()
                .withEquipmentSet()
                    .with(ItemId.IRON_FULL_HELM).build()
                    .with(ItemId.IRON_PLATEBODY).build()
                    .with(ItemId.IRON_PLATELEGS).build()
                    .with(ItemId.IRON_KITESHIELD).build()
                    .with(ItemId.IRON_SCIMITAR).build()
                .build()
            .build();

    private static final String MORGAN = "Morgan";

    private static final Area MORGANS_HOUSE = Area.rectangular(3096, 3270, 3102, 3266);
    private static final Area VARROCK_SQUARE = Area.rectangular(3204, 3438, 3222, 3420);
    private static final Area DRAYNOR_VILLAGE = Area.rectangular(3096, 3265, 3113, 3248);
    private static final Area BLUE_MOON_INN = Area.rectangular(3218, 3402, 3229, 3393);

    private static final String DR_HARLOW = "Dr Harlow";

    private static final Area CELLAR = Area.rectangular(3074, 9778, 3080, 9768);

    private static final String COUNT_DRAYNOR = "Count Draynor";


    private static final List<UnaryNode> QUEST_NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(MORGAN, MORGANS_HOUSE),
            AnonUnaryNode.builder(new DialogAction("Morgan", 1))
                .condition(Condition.isQuestStarted(QUEST))
                .usePrecondition()
                .status("Talking to " + MORGAN).build(),
            CommonNodes.walkToNpc(DR_HARLOW, BLUE_MOON_INN),
            AnonUnaryNode.builder(new DialogAction(DR_HARLOW, 2))
                .condition(Condition.isAtStage(QUEST, 2))
                .timeout(4000)
                .status("Talking to " + DR_HARLOW).build(),
            AnonUnaryNode.builder(new DialogAction(DR_HARLOW))
                .condition(Condition.hasItem(ItemId.STAKE))
                .usePrecondition()
                .timeout(4000)
                .status("Talking to " + DR_HARLOW).build(),
            CommonNodes.walkTo(CELLAR),
            AnonUnaryNode.builder(Action.interactWithObject("Coffin", "Open"))
                .condition(Condition.isNpcPresent("Count Draynor"))
                .usePrecondition()
                .timeout(8000)
                .status("Opening the coffin").build(),
            //Verify if this works correctly
            CommonNodes.waitUntil(() -> {
                var npc = Npcs.getNearest(COUNT_DRAYNOR);
                return npc != null && !npc.isAnimating();
            }, 8000),
            CombatNode.builder(COUNT_DRAYNOR)
                .condition(Condition.isQuestDone(QUEST))
                .build()
    );

    public VampyreSlayerQuestBuilder() {
        super(QUEST_NODES);
    }

}
