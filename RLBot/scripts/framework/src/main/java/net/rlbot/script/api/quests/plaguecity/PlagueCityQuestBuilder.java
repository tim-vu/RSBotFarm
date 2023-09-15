package net.rlbot.script.api.quests.plaguecity;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.SceneObjects;
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
import net.rlbot.script.api.quest.nodes.common.SearchForItemNode;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.Arrays;
import java.util.List;

public class PlagueCityQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.PLAGUE_CITY;

    public PlagueCityQuestBuilder() {
        super(QUESTNODES);
    }

    private static final Loadout LOADOUT = Loadout.builder()
            .withItem(ItemId.DWELLBERRIES).build()
            .withItem(ItemId.ROPE).build()
            .withItem(ItemId.BUCKET_OF_WATER).amount(4).build()
            .withItem(ItemId.BUCKET_OF_MILK).build()
            .withItem(ItemId.CHOCOLATE_DUST).build()
            .withItem(ItemId.SNAPE_GRASS).build()
            .withItem(ItemId.STAMINA_POTION4).build()
            .withItem(ItemId.SPADE).build()
            .withItem(ItemId.VARROCK_TELEPORT).build()
            .build();

    private static final List<Position> BRAVEK_TO_PLAGUE_HOUSE = Arrays.asList(
            new Position(2534, 3314, 0),
            new Position(2533, 3314, 0),
            new Position(2532, 3314, 0),
            new Position(2531, 3314, 0),
            new Position(2530, 3314, 0),
            new Position(2529, 3314, 0),
            new Position(2529, 3314, 0),
            new Position(2529, 3314, 0),
            new Position(2528, 3314, 0),
            new Position(2527, 3314, 0),
            new Position(2526, 3313, 0),
            new Position(2526, 3312, 0),
            new Position(2526, 3311, 0),
            new Position(2527, 3310, 0),
            new Position(2527, 3309, 0),
            new Position(2527, 3308, 0),
            new Position(2528, 3307, 0),
            new Position(2528, 3306, 0),
            new Position(2528, 3305, 0),
            new Position(2528, 3304, 0),
            new Position(2528, 3303, 0),
            new Position(2528, 3302, 0),
            new Position(2529, 3301, 0),
            new Position(2529, 3300, 0),
            new Position(2530, 3299, 0),
            new Position(2531, 3298, 0),
            new Position(2532, 3297, 0),
            new Position(2533, 3296, 0),
            new Position(2533, 3295, 0),
            new Position(2533, 3294, 0),
            new Position(2533, 3293, 0),
            new Position(2533, 3292, 0),
            new Position(2533, 3291, 0),
            new Position(2533, 3290, 0),
            new Position(2533, 3289, 0),
            new Position(2533, 3288, 0),
            new Position(2533, 3287, 0),
            new Position(2533, 3286, 0),
            new Position(2533, 3285, 0),
            new Position(2533, 3284, 0),
            new Position(2533, 3283, 0),
            new Position(2533, 3282, 0),
            new Position(2533, 3281, 0),
            new Position(2533, 3280, 0),
            new Position(2533, 3279, 0),
            new Position(2533, 3278, 0),
            new Position(2533, 3277, 0),
            new Position(2533, 3276, 0),
            new Position(2532, 3275, 0),
            new Position(2532, 3274, 0),
            new Position(2532, 3273, 0),
            new Position(2532, 3272, 0));

    private static final Area GARDEN = Area.rectangular(2562, 3338, 2570, 3329);
    ;
    private static final Area HOUSE = Area.rectangular(2571, 3335, 2577, 3331);
    ;
    private static final Area LIVING_ROOM = Area.rectangular(2574, 3335, 2577, 3331);
    ;

    private static final Area HOUSE_2_ENTRANCE = Area.rectangular(2528, 3328, 2535, 3324);
    private static final Area HOUSE_2 = Area.polygonal(
            new Position(2527, 3334, 0),
            new Position(2534, 3334, 0),
            new Position(2534, 3329, 0),
            new Position(2530, 3329, 0),
            new Position(2530, 3331, 0),
            new Position(2527, 3331, 0));

    private static final Area HOUSE_3_ENTRANCE = Area.rectangular(2530, 3276, 2537, 3272);

    private static final Area TOWN_HALL = Area.rectangular(2522, 3319, 2529, 3312);
    private static final Area BRAVEKS_ROOM = Area.rectangular(2530, 3316, 2539, 3312);
    private static final Area CELL = Area.rectangular(2540, 9673, 2542, 9669);
    private static final Area TOWN_SQUARE = Area.rectangular(2524, 3307, 2535, 3297);

    private static final Position PLAGUE_HOUSE_DOOR = new Position(2533, 3272, 0);
    private static final Area PLAGUE_HOUSE = Area.polygonal(
            new Position(2535, 3273, 0),
            new Position(2542, 3273, 0),
            new Position(2542, 3268, 0),
            new Position(2532, 3268, 0),
            new Position(2532, 3272, 0),
            new Position(2535, 3272, 0));

    private static final Position MUD_PATCH = new Position(2566, 3332, 0);
    private static final Position PIPE = new Position(2514, 9739, 0);
    private static final Position EDMOND_SEWERS = new Position(2519, 9757, 0);
    private static final Position MANHOLE = new Position(2529, 3301, 0);

    private static final String EDMOND = "Edmond";
    private static final String ALRENA = "Alrena";
    private static final String JETHICK = "Jethick";
    private static final String TED = "Ted Rehnison";
    private static final String MILLI = "Milli Rehnison";
    private static final String BRAVEK = "Bravek";

    private static final List<UnaryNode> QUESTNODES = Arrays.asList(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkTo(LIVING_ROOM, "the living room"),
            CommonNodes.pickupItem(ItemId.PICTURE),
            CommonNodes.walkToNpc(EDMOND, GARDEN),
            AnonUnaryNode.builder(new DialogAction(EDMOND, 1, 1))
                .condition(Condition.isQuestStarted(QUEST))
                .usePrecondition()
                .status("Talking to Edmond")
                    .build(),
            CommonNodes.walkToNpc(ALRENA, HOUSE),
            AnonUnaryNode.builder(new DialogAction(ALRENA))
                .condition(Condition.hasItem(ItemId.GAS_MASK))
                .usePrecondition()
                .status("Talking to Alrena").build(),
            CommonNodes.walkToNpc(EDMOND, GARDEN),
            AnonUnaryNode.builder(new DialogAction(EDMOND))
                .condition(Condition.isAtStage(QUEST, 3))
                .usePrecondition()
                .status("Talking to Edmon").build(),
            createUseBucketNode(4),
            CommonNodes.wait(500),
            createUseBucketNode(5),
            CommonNodes.wait(500),
            createUseBucketNode(6),
            CommonNodes.wait(500),
            createUseBucketNode(7),
            CommonNodes.wait(1000),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.SPADE, "Dig"))
                    .condition(Condition.isUnderground())
                    .usePrecondition()
                    .timeout(6000)
                    .wait(600)
                    .status("Digging")
                    .build(),
            CommonNodes.walkTo(PIPE, 7),
            //TODO: Other condition?
            AnonUnaryNode.builder(Action.interactWithObject("Grill", "Open"))
                    .condition(Dialog::isOpen)
                    .status("Trying to open the grill")
                    .build(),
            CommonNodes.wait(1000),
            CommonNodes.dismissDialogue(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.ROPE, () -> SceneObjects.getNearest("Grill")))
                    .condition(Condition.isAtStage(QUEST, 9))
                    .usePrecondition()
                    .status("Using the rope").build(),
            CommonNodes.wait(1000),
            CommonNodes.dismissDialogue(),
            AnonUnaryNode.builder(new DialogAction(EDMOND))
                    .condition(Condition.isAtStage(QUEST, 10))
                    .timeout(25000)
                    .status("Talking to Edmon").build(),
            CommonNodes.wait(2000),
            CommonNodes.equipItems(ItemId.GAS_MASK),
            AnonUnaryNode.builder(Action.interactWithObject("Pipe", "Climb-up"))
                    .condition(Condition.isAboveGround())
                    .usePrecondition()
                    .status("Climbin up the pipe")
                    .timeout(5000).build(),

            CommonNodes.wait(1000),

            CommonNodes.dismissDialogue(),

            AnonUnaryNode.builder(new DialogAction(JETHICK, 1))
                    .condition(Condition.isAtStage(QUEST, 20))
                    .usePrecondition()
                    .status("Talking to Jethick").build(),
            CommonNodes.walkTo(HOUSE_2_ENTRANCE, "the hut"),

            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Opening the door")
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isInArea(HOUSE_2))
                            .usePrecondition()
                            .timeout(5000)
                            .status("Entering the hut")
                            .build()
            ),
            AnonUnaryNode.builder(new DialogAction(TED))
                    .condition(Condition.isAtStage(QUEST, 22))
                    .usePrecondition()
                    .status("Talking to Ted Rehnison").build(),
            CommonNodes.useStairs(true, 1),
            AnonUnaryNode.builder(new DialogAction(MILLI))
                    .condition(Condition.isAtStage(QUEST, 23))
                    .usePrecondition()
                    .status("Talking to Milli Rehnison").build(),
            CommonNodes.useStairs(false, 0),
            CommonNodes.walkTo(HOUSE_3_ENTRANCE, "the house"),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject(PLAGUE_HOUSE_DOOR, "Door", "Open"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Opening the door").build(),
                    AnonUnaryNode.builder(new DialogAction(1, 2))
                            .condition(Condition.isAtStage(QUEST, 24))
                            .usePrecondition()
                            .status("Talking")
                            .build()
            ),
            CommonNodes.walkToNpc("Clerk", TOWN_HALL),
            AnonUnaryNode.builder(new DialogAction(() -> Npcs.query().within(TOWN_HALL).results().nearest(), 2, 1))
                    .condition(Condition.isAtStage(QUEST, 25))
                    .usePrecondition()
                    .status("Talking to the clerk").build(),
            CommonNodes.walkTo(BRAVEKS_ROOM, "Bravek"),
            AnonUnaryNode.builder(new DialogAction(BRAVEK, 1, 3))
                    .condition(Condition.isAtStage(QUEST, 26))
                    .usePrecondition()
                    .status("Talking to Bravek").build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.CHOCOLATE_DUST, () -> Inventory.getFirst(ItemId.BUCKET_OF_MILK)))
                    .condition(Condition.hasItem(ItemId.CHOCOLATEY_MILK))
                    .usePrecondition()
                    .status("Using the chocolate dust").build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.SNAPE_GRASS, () -> Inventory.getFirst(ItemId.CHOCOLATEY_MILK)))
                    .condition(Condition.hasItem(ItemId.HANGOVER_CURE))
                    .usePrecondition()
                    .wait(1200)
                    .status("Using the snape grass")
                    .build(),
            CommonNodes.dismissDialogue(),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.useItemOn(ItemId.HANGOVER_CURE, () -> Npcs.getNearest(BRAVEK)))
                            .condition(Dialog::isOpen)
                            .usePrecondition(Condition.doesntHaveItem(ItemId.HANGOVER_CURE))
                            .status("Using the hangover cure")
                            .build(),
                    AnonUnaryNode.builder(new DialogAction(BRAVEK, 3))
                            .condition(Condition.hasItem(ItemId.WARRANT))
                            .usePrecondition()
                            .status("Talking to Bravek")
                            .build()
            ),
            CommonNodes.walkPath(BRAVEK_TO_PLAGUE_HOUSE, 5),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject(PLAGUE_HOUSE_DOOR, "Door", "Open"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Opening the door").build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isInArea(PLAGUE_HOUSE))
                            .timeout(8000)
                            .wait(1800)
                            .status("Talking").build()
            ),
            new SearchForItemNode("Barrel", "Search", ItemId.A_SMALL_KEY),
            CommonNodes.changeFloorLevel("Spooky stairs", "Walk-down", -1),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Opening the door")
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isInArea(CELL))
                            .timeout(10000)
                            .wait(1200)
                            .status("Talking").build()
            ),
            AnonUnaryNode.builder(new DialogAction("Elena"))
                    .condition(Condition.isAtStage(QUEST, 28))
                    .usePrecondition()
                    .timeout(8000)
                    .status("Talking to Elena").build(),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Condition.isNotInArea(CELL))
                    .usePrecondition()
                    .status("Opening the door").build(),
            CommonNodes.changeFloorLevel("Spooky stairs", "Walk-up", 0),
            CommonNodes.wait(500),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Condition.isNotInArea(PLAGUE_HOUSE))
                    .usePrecondition()
                    .status("Exiting the plague house")
                    .build(),
            CommonNodes.walkTo(MANHOLE, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Manhole", "Open"))
                    .condition(Condition.objectHasFirstAction("Manhole", "Climb-down"))
                    .usePrecondition()
                    .status("Opening the manhole").build(),
            CommonNodes.changeFloorLevel("Manhole", "Climb-down", -1),
            CommonNodes.wait(2000),
            CommonNodes.walkTo(EDMOND_SEWERS, 7),
            AnonUnaryNode.builder(Action.interactWithObject("Mud pile", "Climb"))
                    .condition(Condition.isAboveGround())
                    .usePrecondition()
                    .timeout(4000)
                    .status("Climbing the mud pile").build(),
            AnonUnaryNode.builder(new DialogAction(EDMOND))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .status("Talking to Edmond")
                    .build()
    );

    private static UnaryNode createUseBucketNode(int value) {
        return AnonUnaryNode.builder(Action.useItemOn(ItemId.BUCKET_OF_WATER, () -> SceneObjects.query().locations(MUD_PATCH).names("Mud patch").results().first()))
                .condition(Condition.isAtStage(QUEST, value))
                .usePrecondition()
                .wait(600)
                .status("Using the bucket of water").build();
    }
}
