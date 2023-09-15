package net.rlbot.script.api.quests.goblindiplomacy;

import net.rlbot.api.Game;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.widgets.Dialog;
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

public class GoblinDiplomacyQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.GOBLIN_DIPLOMACY;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.RED_DYE).build()
                .withItem(ItemId.YELLOW_DYE).build()
                .withItem(ItemId.BLUE_DYE).build()
                .withItem(ItemId.GOBLIN_MAIL).amount(3).build()
            .build();

    private static final Area TOWN_HALL = Area.polygonal(
            new Position(2956, 3515, 0),
            new Position(2959, 3515, 0),
            new Position(2959, 3514, 0),
            new Position(2962, 3514, 0),
            new Position(2962, 3510, 0),
            new Position(2954, 3510, 0),
            new Position(2954, 3513, 0),
            new Position(2956, 3513, 0)
    );

    private static final String GENERAL_WARTFACE = "General Wartface";

    private static final List<UnaryNode> NODES = List.of(
            //TODO: Test quest again
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(GENERAL_WARTFACE, TOWN_HALL),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.RED_DYE, () -> Inventory.getFirst(ItemId.YELLOW_DYE)))
                     .condition(Condition.hasItem(ItemId.ORANGE_DYE))
                     .usePrecondition()
                     .status("Using red dye of yellow dye")
                     .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.ORANGE_DYE, () -> Inventory.getFirst(ItemId.GOBLIN_MAIL)))
                     .condition(Condition.hasItem(ItemId.ORANGE_GOBLIN_MAIL))
                     .usePrecondition()
                     .status("Using the orange dye on the goblin mail")
                     .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.BLUE_DYE, () -> Inventory.getFirst(ItemId.GOBLIN_MAIL)))
                     .condition(Condition.hasItem(ItemId.BLUE_GOBLIN_MAIL))
                     .usePrecondition()
                     .status("Using the blue dye on the goblin mail")
                     .build(),
            AnonUnaryNode.builder(new DialogAction(GENERAL_WARTFACE, 3, 3, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to " + GENERAL_WARTFACE)
                    .build(),
            createGiveGoblinMail(ItemId.BLUE_GOBLIN_MAIL),
            createGiveGoblinMail(ItemId.GOBLIN_MAIL)
    );

    private static UnaryNode createGiveGoblinMail(int itemId) {
        return AnonUnaryNode.sequence(
                AnonUnaryNode.builder(Action.useItemOn(itemId, () -> Npcs.getNearest(GENERAL_WARTFACE)))
                        .condition(Dialog::isOpen)
                        .usePrecondition()
                        .status("Giving the goblin mail to" + GENERAL_WARTFACE)
                        .build(),
                AnonUnaryNode.builder(new DialogAction())
                        .condition(Condition.doesntHaveItem(itemId))
                        .usePrecondition()
                        .build()
        );
    }

    public GoblinDiplomacyQuestBuilder() {
        super(NODES);
    }
}
