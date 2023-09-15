package net.rlbot.script.api.quests.undergroundpass;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.QuestBuilder;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.QuestNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.condition.Condition;
import net.rlbot.script.api.quests.undergroundpass.nodes.PassBridgeNode;

import java.util.List;

public class UndergroundPassQuestBuilder implements QuestBuilder {

    public static final Quest QUEST = Quest.UNDERGROUND_PASS;
    public static final int FOOD_ITEM_ID = ItemId.MONKFISH;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.WILLOW_SHORTBOW).build()
                .withItem(ItemId.IRON_ARROW).amount(5).build()
                .withItem(ItemId.ROPE).amount(2).build()
                .withItem(ItemId.SPADE).build()
                .withItem(ItemId.STAMINA_POTION4).amount(3).build()
                .withItem(ItemId.ARDOUGNE_TELEPORT).build()
                .withItem(ItemId.WEST_ARDOUGNE_TELEPORT).build()
                .withEquipmentSet()
                    .with(ItemId.RUNE_FULL_HELM).build()
                    .with(ItemId.AMULET_OF_POWER).build()
                    .with(ItemId.RUNE_CHAINBODY).build()
                    .with(ItemId.RUNE_KITESHIELD).build()
                    .with(ItemId.RUNE_PLATELEGS).build()
                    .with(ItemId.LEATHER_GLOVES).build()
                    .with(ItemId.LEATHER_BOOTS).build()
            .build()
            .build();

    private static final Area ARDOUGNE_CASTLE = Area.polygonal(
            new Position(2575, 3295, 1),
            new Position(2578, 3295, 1),
            new Position(2578, 3296, 1),
            new Position(2581, 3296, 1),
            new Position(2581, 3292, 1),
            new Position(2575, 3292, 1)
    );

    private static final String KING_LATHAS = "King Lathas";

    private static final Area PASS_ENTRANCE = Area.rectangular(2434, 3320, 2445, 3309);

    private static final String KOFTIK = "Koftik";

    private static final Area IN_FRONT_OF_BRIDGE = Area.rectangular(2445, 9720, 2453, 9713);

    private static final Area PLANK = Area.rectangular(2430, 9728, 2439, 9723);

    private static final Area GRID_START = Area.rectangular(2476, 9682, 2480, 9672);

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(KING_LATHAS, ARDOUGNE_CASTLE),
            AnonUnaryNode.builder(new DialogAction(KING_LATHAS, 1, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to " + KING_LATHAS)
                    .build(),
            CommonNodes.walkToNpc(KOFTIK, PASS_ENTRANCE),
            AnonUnaryNode.builder(new DialogAction(KOFTIK, 1))
                    .condition(Condition.isAtStage(QUEST, 20))
                    .usePrecondition()
                    .status("Talking to " + KOFTIK)
                    .build(),
            CommonNodes.walkToNpc(KOFTIK, IN_FRONT_OF_BRIDGE),
            new PassBridgeNode(),
            CommonNodes.walkTo(PLANK),
            CommonNodes.pickupItem(ItemId.PLANK),
            CommonNodes.walkTo(GRID_START)
    );

    @Override
    public QuestNode buildQuest() {

        return null;

    }

}
