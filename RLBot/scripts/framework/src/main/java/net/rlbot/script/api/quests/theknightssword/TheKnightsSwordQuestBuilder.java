package net.rlbot.script.api.quests.theknightssword;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class TheKnightsSwordQuestBuilder extends UnaryNodeQuestBuilder {

    public static final int FOOD_ITEM_ID = ItemId.TUNA;
    public static final Quest QUEST = Quest.THE_KNIGHTS_SWORD;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.IRON_BAR).amount(2).build()
                .withItem(ItemId.IRON_PICKAXE).build()
                .withItem(ItemId.TUNA).amount(5).build()
                .withItem(ItemId.REDBERRY_PIE).build()
            .build();

    private static final Area WHITE_KNIGHTS_CASTLE = Area.rectangular(2971, 3346, 2980, 3337);
    private static final String SQUIRE = "Squire";

    private static final Area VARROCK_LIBRARY = Area.polygonal(
            new Position(3207, 3498, 0),
            new Position(3218, 3498, 0),
            new Position(3218, 3494, 0),
            new Position(3215, 3494, 0),
            new Position(3215, 3490, 0),
            new Position(3207, 3490, 0)
    );

    private static final String RELDO = "Reldo";

    private static final Area THURGOS_HUT = Area.rectangular(2994, 3150, 3003, 3140);

    private static final String THURGO = "Thurgo";

    private static final Area BEDROOM = Area.polygonal(
            new Position(2981, 3337, 2),
            new Position(2987, 3337, 2),
            new Position(2987, 3336, 2),
            new Position(2985, 3334, 2),
            new Position(2984, 3334, 2),
            new Position(2984, 3331, 2),
            new Position(2981, 3331, 2)
    );

    private static final String SIR_VYVIN = "Sir Vyvin";

    private static final List<UnaryNode> NODES = List.of(
            //new SetupLoadout(LOADOUT),
            //CommonNodes.walkToNpc(SQUIRE, WHITE_KNIGHTS_CASTLE),
            //AnonUnaryNode.builder(new DialogAction(SQUIRE, 1, 2, 1, 1))
            //        .condition(Condition.isQuestStarted(QUEST))
            //        .usePrecondition()
            //        .status("Talking to " + SQUIRE)
            //        .build(),
            //CommonNodes.walkToNpc(RELDO, VARROCK_LIBRARY),
            //AnonUnaryNode.builder(new DialogAction(RELDO, 4))
            //        .condition(Condition.isAtStage(QUEST, 2))
            //        .usePrecondition()
            //        .status("Talking to " + RELDO)
            //        .build(),
            //CommonNodes.walkToNpc(THURGO, THURGOS_HUT),
            //AnonUnaryNode.builder(new DialogAction(THURGO, 2, 1))
            //        .condition(Condition.isAtStage(QUEST, 4))
            //        .usePrecondition()
            //        .status("Talking to " + THURGO)
            //        .build(),
            //CommonNodes.walkToNpc(SQUIRE, WHITE_KNIGHTS_CASTLE),
            //AnonUnaryNode.builder(new DialogAction(SQUIRE))
            //        .condition(Condition.isAtStage(QUEST, 5))
            //        .usePrecondition()
            //        .status("Talking to" + SQUIRE)
            //        .build(),
            //CommonNodes.walkTo(BEDROOM),
            //UnaryNode.sequence(
            //        () -> Npcs.query().within(BEDROOM).names(SIR_VYVIN).results().isEmpty(),
            //        AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
            //                .condition(Condition.objectHasFirstAction("Door", "Close"))
            //                .usePrecondition()
            //                .status("Opening the door")
            //                .build(),
            //        CommonNodes.waitUntil(() -> Npcs.query().within(BEDROOM).names(SIR_VYVIN).results().isEmpty(), 20000),
            //        AnonUnaryNode.builder(Action.interactWithObject("Door", "Close"))
            //                .condition(Condition.objectHasFirstAction("Door", "Open"))
            //                .usePrecondition()
            //                .status("Closing the door")
            //                .build()
            //),
            //AnonUnaryNode.builder(Action.interactWithObject("Cupboard", "Open"))
            //        .condition(Condition.objectHasFirstAction("Cupboard", "Search"))
            //        .usePrecondition()
            //        .status("Opening the cupboard")
            //        .build(),
            //new SearchForItemNode("Cupboard", "Search", ItemId.PORTRAIT),
            //CommonNodes.walkToNpc(THURGO, THURGOS_HUT),
            //AnonUnaryNode.builder(new DialogAction(THURGO, 1))
            //        .condition(Condition.doesntHaveItem(ItemId.PORTRAIT))
            //        .usePrecondition()
            //        .status("Talking to " + THURGO)
            //        .build(),
            //new MineBlurite(),
            //CommonNodes.walkToNpc(THURGO, THURGOS_HUT),
            AnonUnaryNode.builder(new DialogAction(THURGO, 1))
                    .condition(Condition.hasItem(ItemId.BLURITE_SWORD))
                    .usePrecondition()
                    .status("Talking to " + THURGO)
                    .build(),
            CommonNodes.walkToNpc(SQUIRE, WHITE_KNIGHTS_CASTLE),
            AnonUnaryNode.builder(new DialogAction(SQUIRE))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .status("Talking to " + SQUIRE)
                    .build()
    );



    public TheKnightsSwordQuestBuilder() {
        super(NODES);
    }
}
