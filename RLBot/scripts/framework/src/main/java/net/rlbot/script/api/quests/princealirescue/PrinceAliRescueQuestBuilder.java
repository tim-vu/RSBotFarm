package net.rlbot.script.api.quests.princealirescue;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class PrinceAliRescueQuestBuilder extends UnaryNodeQuestBuilder {

    private static final Loadout LOADOUT = Loadout.builder()
            .withItem(ItemId.COINS_995).amount(45).build()
            .withItem(ItemId.ROPE).build()
            .withItem(ItemId.BALL_OF_WOOL).amount(3).build()
            .withItem(ItemId.YELLOW_DYE).build()
            .withItem(ItemId.REDBERRIES).build()
            .withItem(ItemId.BUCKET_OF_WATER).build()
            .withItem(ItemId.POT_OF_FLOUR).build()
            .withItem(ItemId.PINK_SKIRT).build()
            .withItem(ItemId.ASHES).build()
            .withItem(ItemId.SOFT_CLAY).build()
            .withItem(ItemId.BEER).amount(3).build()
            .withItem(ItemId.BRONZE_BAR).build()
            .withItem(PrinceAliRescueQuestDescriptor.FOOD).amount(4).build()
            .build();


    private static final Area SOUTH_ROOM = Area.rectangular(3282, 3166, 3303, 3159);

    private static final String HASSAN = "Chancellor Hassan";

    private static final Area OUTSIDE_PALACE = Area.rectangular(3282, 3186, 3292, 3179);

    private static final String OSMAN = "Osman";

    private static final Area DRAYNOR_VILLAGE = Area.polygonal(
            new Position(3107, 3271, 0),
            new Position(3111, 3267, 0),
            new Position(3114, 3267, 0),
            new Position(3114, 3262, 0),
            new Position(3103, 3262, 0),
            new Position(3103, 3271, 0)
    );

    private static final String LEELA = "Leela";

    private static final Area WESTERN_HOUSE = Area.rectangular(3083, 3261, 3088, 3256);

    private static final String AGGIE = "Aggie";

    private static final Area NEDS_HOUSE = Area.rectangular(3096, 3261, 3101, 3256);

    private static final String NED = "Ned";

    private static final Area JAIL = Area.rectangular(3121, 3246, 3130, 3240);

    private static final String LADY_KELI = "Lady Keli";

    private static final String JOE = "Joe";

    private static final Area CELL = Area.rectangular(3121, 3243, 3125, 3240);

    private static final String PRINCE_ALI = "Prince Ali";

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(HASSAN, SOUTH_ROOM),
            AnonUnaryNode.builder(new DialogAction(HASSAN, 1, 1))
                    .condition(Condition.isQuestStarted(Quest.PRINCE_ALI_RESCUE))
                    .usePrecondition()
                    .status("Talking to " + HASSAN)
                    .build(),
            CommonNodes.walkToNpc(OSMAN, OUTSIDE_PALACE),
            AnonUnaryNode.builder(new DialogAction(OSMAN, 1, 1, 2))
                    .condition(Condition.isAtStage(Quest.PRINCE_ALI_RESCUE, 20))
                    .usePrecondition()
                    .status("Talking to " + OSMAN)
                    .build(),
            CommonNodes.walkToNpc(AGGIE, WESTERN_HOUSE),
            AnonUnaryNode.builder(new DialogAction(AGGIE, 2, 1))
                    .condition(Condition.hasItem(ItemId.PASTE))
                    .usePrecondition()
                    .status("Making skin paste")
                    .build(),
            CommonNodes.walkToNpc(NED, NEDS_HOUSE),
            AnonUnaryNode.builder(new DialogAction(NED, 1, 2, 1))
                    .condition(Condition.hasItem(ItemId.WIG_2421))
                    .usePrecondition()
                    .status("Talking to " + NED)
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.YELLOW_DYE, () -> Inventory.getFirst(ItemId.WIG_2421)))
                    .condition(Condition.hasItem(ItemId.WIG))
                    .usePrecondition()
                    .status("Using the yellow dye on the wig")
                    .build(),
            CommonNodes.walkToNpc(LADY_KELI, JAIL),
            AnonUnaryNode.builder(new DialogAction(LADY_KELI, 1, 1, 2, 1, 1))
                    .condition(Condition.hasItem(ItemId.KEY_PRINT))
                    .usePrecondition()
                    .status("Talking to " + LADY_KELI)
                    .build(),
            CommonNodes.walkToNpc(OSMAN, OUTSIDE_PALACE),
            AnonUnaryNode.builder(new DialogAction(OSMAN, 3))
                    .condition(Condition.doesntHaveItem(ItemId.KEY_PRINT))
                    .usePrecondition()
                    .status("Talking to " + OSMAN)
                    .build(),
            CommonNodes.walkToNpc(LEELA, DRAYNOR_VILLAGE),
            AnonUnaryNode.builder(new DialogAction(LEELA))
                    .condition(Condition.hasItem(ItemId.BRONZE_KEY))
                    .usePrecondition()
                    .status("Talking to " + LEELA)
                    .build(),
            CommonNodes.walkToNpc(JOE, JAIL),
            AnonUnaryNode.builder(new DialogAction(JOE, 1))
                    .condition(Condition.doesntHaveItem(ItemId.BEER))
                    .usePrecondition()
                    .status("Talking to " + JOE)
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.ROPE, () -> Npcs.getNearest(LADY_KELI)))
                    .condition(Condition.doesntHaveItem(ItemId.ROPE))
                    .usePrecondition()
                    .timeout(5000)
                    .wait(600)
                    .status("Tying up " + LADY_KELI)
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.BRONZE_KEY, () -> SceneObjects.getNearest("Prison Gate")))
                    .condition(Condition.isInArea(CELL))
                    .usePrecondition()
                    .status("Opening the prison door")
                    .timeout(5000)
                    .build(),
            AnonUnaryNode.builder(new DialogAction(PRINCE_ALI))
                    .condition(Condition.isAtStage(Quest.PRINCE_ALI_RESCUE, 100))
                    .usePrecondition()
                    .status("Talking to " + PRINCE_ALI)
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Prison Gate", "Open"))
                    .condition(Condition.isNotInArea(CELL))
                    .usePrecondition()
                    .timeout(6000)
                    .wait(600)
                    .status("Exiting the cell")
                    .build(),
            CommonNodes.walkToNpc(HASSAN, SOUTH_ROOM),
            AnonUnaryNode.builder(new DialogAction(HASSAN))
                    .condition(Condition.isQuestDone(Quest.PRINCE_ALI_RESCUE))
                    .usePrecondition()
                    .status("Talking to " + HASSAN)
                    .build()
    );

    public PrinceAliRescueQuestBuilder() {
        super(NODES);
    }
}
