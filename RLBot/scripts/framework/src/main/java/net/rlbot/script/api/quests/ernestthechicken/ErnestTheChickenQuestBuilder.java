package net.rlbot.script.api.quests.ernestthechicken;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SearchForItemNode;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;
import java.util.Map;

public class ErnestTheChickenQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.ERNEST_THE_CHICKEN;

    private static final Area QUEST_START = Area.rectangular(3107, 3330, 3114, 3324);
    private static final Area UPSTRAIRS = Area.rectangular(3104, 3370, 3112, 3362, 2);;
    private static final Area MIDDLE_ROOM = Area.rectangular(3107, 3361, 3110, 3354, 1);
    private static final Area NEXT_TO_KITCHEN = Area.rectangular(3097, 3366, 3101, 3364);
    private static final Area LIBRARY = Area.rectangular(3097, 3363, 3104, 3354);
    private static final Area HIDDEN_ROOM = Area.rectangular(3091, 3363, 3096, 3354);
    private static final Area EASTERN_ROOM = Area.rectangular(3120, 3360, 3126, 3354);
    private static final Area RUBBER_TUBE = Area.rectangular(3108, 3368, 3112, 3366);

    private static final Position OIL_CAN = new Position(3092, 9755, 0);
    private static final Position LADDER = new Position(3116, 9754, 0);
    private static final Position COMPOST = new Position(3087, 3357, 0);
    private static final Position FOUNTAIN = new Position(3090, 3338, 0);

    private static final String VERONICA = "Veronica";
    private static final String PROFESSOR_ODDENSTEIN = "Professor Oddenstein";

    private static final Map<String, Integer> LEVER_TO_VARBIT = Map.of(
            "A", 1788,
            "B", 1789,
            "C", 1790,
            "D", 1791,
            "E", 1792,
            "F", 1793
    );

    private static final List<UnaryNode> NODES = List.of(
            CommonNodes.walkToNpc(VERONICA, QUEST_START),
            AnonUnaryNode.builder(new DialogAction(VERONICA, 1))
                .condition(Condition.isQuestStarted(QUEST))
                .usePrecondition()
                .status("Talking to Veronica").build(),
            CommonNodes.walkToNpc(PROFESSOR_ODDENSTEIN, UPSTRAIRS),
            AnonUnaryNode.builder(new DialogAction(PROFESSOR_ODDENSTEIN, 1, 2))
                .condition(Condition.isAtStage(QUEST, 2))
                .usePrecondition()
                .status("Talking to Professor Oddenstein").build(),
            CommonNodes.walkTo(MIDDLE_ROOM, "the fish food"),
            CommonNodes.pickupItem(ItemId.FISH_FOOD),
            CommonNodes.walkTo(NEXT_TO_KITCHEN, "the kitchen"),
            CommonNodes.pickupItem(ItemId.POISON),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.POISON, () -> Inventory.getFirst(ItemId.FISH_FOOD)))
                .condition(Condition.hasItem(ItemId.POISONED_FISH_FOOD))
                .usePrecondition()
                .status("Making poisened fish food").build(),
            CommonNodes.walkTo(LIBRARY, "the library"),
            AnonUnaryNode.builder(Action.interactWithObject("Bookcase", "Search"))
                .condition(Condition.isInArea(HIDDEN_ROOM))
                .usePrecondition()
                .status("Searching the bookcase").build(),
            CommonNodes.changeFloorLevel("Ladder", "Climb-down", -1),
            CommonNodes.walkToObjectLocal("Lever B", 7),
            createPullLevelNode("B", false),
            CommonNodes.walkToObjectLocal("Lever A", 7),
            createPullLevelNode("A", false),

            passThroughDoor(new Position(3108, 9757, 0), new Position(3108, 9759, 0)),
            createPullLevelNode("D", false),
            passThroughDoor(new Position(3108, 9759, 0), new Position(3108, 9757, 0)),

            createPullLevelNode("B", true),
            CommonNodes.walkToObjectLocal("Lever A", 4),
            createPullLevelNode("A", true),

            passThroughDoor(new Position(3102, 9757, 0), new Position(3102, 9759, 0)),
            passThroughDoor(new Position(3101, 9760, 0), new Position(3099, 9760, 0)),
            passThroughDoor(new Position(3097, 9762, 0), new Position(3097, 9764, 0)),
            createPullLevelNode("F", false),
            createPullLevelNode("E", false),

            passThroughDoor(new Position(3099, 9765, 0), new Position(3101, 9765, 0)),
            passThroughDoor(new Position(3104, 9765, 0), new Position(3106, 9765, 0)),
            createPullLevelNode("C", false),

            passThroughDoor(new Position(3106, 9765, 0), new Position(3104, 9765, 0)),
            passThroughDoor(new Position(3101, 9765, 0), new Position(3099, 9765, 0)),
            createPullLevelNode("E", true),

            passThroughDoor(new Position(3099, 9765, 0),  new Position(3101, 9765, 0)),
            passThroughDoor(new Position(3102, 9764, 0), new Position(3102, 9762, 0)),
            passThroughDoor(new Position(3102, 9759, 0), new Position(3102, 9757, 0)),
            passThroughDoor(new Position(3101, 9755, 0), new Position(3099, 9755, 0)),

            CommonNodes.pickupItem(ItemId.OIL_CAN),

            passThroughDoor(new Position(3099, 9755, 0), new Position(3101, 9755, 0)),

            CommonNodes.walkToObjectLocal("Ladder", 7),
            CommonNodes.changeFloorLevel("Ladder", "Climb-up", 0),
            AnonUnaryNode.builder(Action.interactWithObject("Lever", "Pull"))
                    .condition(Condition.isInArea(LIBRARY))
                    .usePrecondition()
                    .timeout(6000)
                    .status("Pulling the lever").build(),
            CommonNodes.walkTo(EASTERN_ROOM, "the eastern room"),
            CommonNodes.pickupItem(ItemId.SPADE),
            CommonNodes.walkTo(COMPOST, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Compost heap", "Search"))
                    .condition(Condition.hasItem(ItemId.KEY))
                    .usePrecondition()
                    .timeout(6000)
                    .status("Searching for the key").build(),
            CommonNodes.walkTo(FOUNTAIN, 5),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.POISONED_FISH_FOOD, () -> SceneObjects.getNearest("Fountain")))
                    .condition(Condition.doesntHaveItem(ItemId.POISONED_FISH_FOOD))
                    .usePrecondition()
                    .wait(4000)
                    .timeout(8000)
                    .status("Using the poisoned fish food").build(),
            new SearchForItemNode("Fountain", "Search", ItemId.PRESSURE_GAUGE),
            CommonNodes.walkTo(RUBBER_TUBE, "the rubber tube"),
            CommonNodes.pickupItem(ItemId.RUBBER_TUBE),
            CommonNodes.walkToNpc(PROFESSOR_ODDENSTEIN, UPSTRAIRS),
            AnonUnaryNode.builder(new DialogAction(PROFESSOR_ODDENSTEIN))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .timeout(30000)
                    .status("Talking to " + PROFESSOR_ODDENSTEIN).build()
    );

    private static UnaryNode createPullLevelNode(String lever, boolean up){
        return AnonUnaryNode.builder(Action.interactWithObject("Lever " + lever, "Pull"))
                .condition(Condition.varbitHasValue(LEVER_TO_VARBIT.get(lever), up ? 0 : 1))
                .usePrecondition()
                .timeout(5000)
                .status("Pulling the lever " + lever).build();
    }

    private static UnaryNode passThroughDoor(Position doorPosition, Position goalPosition) {
        return AnonUnaryNode.sequence(
                CommonNodes.walkTo(doorPosition, 6),
                AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(doorPosition, "Door"), 0))
                        .condition(Condition.isReachable(goalPosition))
                        .usePrecondition()
                        .wait(1000)
                        .timeout(6000)
                        .build()
        );
    }

    public ErnestTheChickenQuestBuilder() {
        super(NODES);
    }
}
