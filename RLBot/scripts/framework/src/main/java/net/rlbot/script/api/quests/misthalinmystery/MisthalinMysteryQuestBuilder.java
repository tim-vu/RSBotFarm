package net.rlbot.script.api.quests.misthalinmystery;

import net.rlbot.api.Game;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.condition.Condition;
import net.rlbot.script.api.quests.misthalinmystery.nodes.FightBossNode;
import net.rlbot.script.api.quests.misthalinmystery.nodes.PlayPianoNode;
import net.rlbot.script.api.quests.misthalinmystery.nodes.SearchFireplace;
import net.rlbot.script.api.quests.misthalinmystery.nodes.WatchKillCutscene;

import java.util.List;
import java.util.function.Supplier;

public class MisthalinMysteryQuestBuilder extends UnaryNodeQuestBuilder {

    private static final Area LUMBRIDGE_SWAP = Area.polygonal(
            new Position(3245, 3159, 0),
            new Position(3232, 3159, 0),
            new Position(3232, 3142, 0),
            new Position(3237, 3142, 0),
            new Position(3239, 3146, 0),
            new Position(3239, 3149, 0),
            new Position(3242, 3149, 0),
            new Position(3245, 3154, 0)
    );

    private static final String ABIGALE = "Abigale";

    private static final Position ROWBOAT = new Position(3238, 3143, 0);

    private static final Area ISLAND_DOCK = Area.rectangular(1630, 4808, 1642, 4798);

    private static final Area FOUNTAIN = Area.surrounding(new Position(1620, 4817, 0), 8);

    private static final String BARREL_OF_RAINWATER = "A barrel of rainwater";

    private static final Position DOOR_1 = new Position(1635, 4838, 0);

    private static final Supplier<SceneObject> TABLE_WITH_KNIFE = () -> SceneObjects.query().names("Table").actions("Take-knife").results().nearest();

    private static final WidgetAddress NOTE_CLOSE_BUTTON = new WidgetAddress(70, 104);

    private static final Position PAINTING = new Position(1632, 4833, 0);

    private static final Area CANDLE_ROOM = Area.polygonal(
            new Position(1641, 4833, 0),
            new Position(1648, 4833, 0),
            new Position(1648, 4826, 0),
            new Position(1645, 4826, 0),
            new Position(1645, 4825, 0),
            new Position(1644, 4824, 0),
            new Position(1643, 4824, 0),
            new Position(1642, 4825, 0),
            new Position(1641, 4825, 0)
    );

    private static final Position CANDLE_1 = new Position(1647, 4828, 0);

    private static final Position CANDLE_2 = new Position(1646, 4832, 0);

    private static final Position CANDLE_3 = new Position(1641, 4826, 0);

    private static final Position CANDLE_4 = new Position(1641, 4831, 0);

    private static final List<Position> PATH_TO_PAINTING = List.of(
            new Position(1634, 4838, 0),
            new Position(1634, 4837, 0),
            new Position(1634, 4836, 0),
            new Position(1634, 4835, 0),
            new Position(1634, 4834, 0),
            new Position(1634, 4833, 0),
            new Position(1634, 4832, 0),
            new Position(1633, 4831, 0),
            new Position(1632, 4831, 0),
            new Position(1631, 4831, 0),
            new Position(1630, 4832, 0),
            new Position(1630, 4833, 0)
    );

    private static final Area HALL = Area.rectangular(1633, 4834, 1640, 4825);

    private static final Area OUTSIDE = Area.rectangular(1648, 4832, 1651, 4827);

    private static final Position LACEY = new Position(1633, 4849, 0);

    private static final Position PIANO = new Position(1646, 4842, 0);

    private static final Position GODSWORD_DOOR = new Position(1630, 4842, 0);

    private static final Area GODSWORD_ROOM = Area.rectangular(1625, 4844, 1629, 4838);

    private static final List<Position> PATH_TO_FIREPLACE = List.of(
            new Position(1631, 4842, 0),
            new Position(1631, 4841, 0),
            new Position(1631, 4840, 0),
            new Position(1631, 4839, 0),
            new Position(1631, 4838, 0),
            new Position(1632, 4837, 0),
            new Position(1633, 4837, 0),
            new Position(1634, 4836, 0),
            new Position(1634, 4835, 0),
            new Position(1634, 4834, 0),
            new Position(1635, 4833, 0),
            new Position(1635, 4833, 0),
            new Position(1636, 4832, 0),
            new Position(1637, 4831, 0),
            new Position(1638, 4830, 0),
            new Position(1638, 4829, 0),
            new Position(1639, 4828, 0),
            new Position(1640, 4828, 0),
            new Position(1641, 4828, 0),
            new Position(1642, 4829, 0),
            new Position(1643, 4830, 0),
            new Position(1643, 4831, 0),
            new Position(1643, 4832, 0),
            new Position(1643, 4833, 0),
            new Position(1643, 4834, 0),
            new Position(1644, 4835, 0),
            new Position(1645, 4836, 0),
            new Position(1646, 4836, 0)
    );

    private static final Position BOSS_ROOM_DOOR = new Position(1628, 4829, 0);
    private static final String MANDY = "Mandy";

    private static final Area IN_FRONT_OF_MANSION = Area.rectangular(1635, 4821, 1639, 4814);

    private static final List<UnaryNode> NODES = List.of(
            CommonNodes.walkToNpc(ABIGALE, LUMBRIDGE_SWAP),
            AnonUnaryNode.builder(new DialogAction(ABIGALE, 1, 1))
                    .condition(Condition.isQuestStarted(Quest.MISTHALIN_MYSTERY))
                    .usePrecondition()
                    .status("Talking to " + ABIGALE)
                    .build(),
            CommonNodes.walkTo(ROWBOAT, 7),
            AnonUnaryNode.builder(Action.interactWithObject("Rowboat", "Board"))
                    .condition(Condition.isInArea(ISLAND_DOCK))
                    .usePrecondition()
                    .timeout(8000)
                    .status("Boarding the rowboat")
                    .build(),
            CommonNodes.wait(600),
            CommonNodes.walkTo(FOUNTAIN),
            AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(o -> o.getName().contains("Bucket")), "Take"))
                    .condition(Condition.hasItem(ItemId.BUCKET))
                    .usePrecondition()
                    .status("Picking up the bucket")
                    .build(),
            CommonNodes.walkToObjectLocal(BARREL_OF_RAINWATER, 3),
            CommonNodes.watchCutScene(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 20)),
            CommonNodes.wait(600),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.BUCKET, () -> SceneObjects.getNearest(BARREL_OF_RAINWATER)))
                    .condition(Condition.hasItem(ItemId.BUCKET_OF_WATER))
                    .usePrecondition()
                    .status("Using the bucket of the barrel")
                    .timeout(5000)
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Barrel", "Search"))
                    .condition(Condition.hasItem(ItemId.MANOR_KEY_21052))
                    .usePrecondition()
                    .status("Search the barrel")
                    .build(),
            CommonNodes.walkToObjectLocal("Large door", 7),
            AnonUnaryNode.builder(Action.interactWithObject("Large door", "Open"))
                    .condition(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 30))
                    .usePrecondition()
                    .status("Opening the door")
                    .wait(600)
                    .build(),
            CommonNodes.walkToSceneEntityLocal(TABLE_WITH_KNIFE, "Table", 7),
            AnonUnaryNode.builder(Action.interactWithObject(TABLE_WITH_KNIFE, "Take-knife"))
                    .condition(Condition.hasItem(ItemId.KNIFE))
                    .usePrecondition()
                    .timeout(5000)
                    .status("Taking knife")
                    .build(),
            CommonNodes.walkTo(DOOR_1, 7),
            AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(DOOR_1, "Door"), "Open"))
                    .condition(Game::isInCutscene)
                    .usePrecondition(() -> Game.isInCutscene() || Quests.getStage(Quest.MISTHALIN_MYSTERY) == 35)
                    .status("Opening the door")
                    .build(),
            CommonNodes.watchCutScene(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 35)),
            CommonNodes.wait(600),
            AnonUnaryNode.builder(Action.interactWithObject("Note", "Take"))
                    .condition(Condition.hasItem(ItemId.NOTES_21056))
                    .usePrecondition()
                    .status("Picking up notes")
                    .build(),
            UnaryNode.sequence(
                      Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 40),
                      AnonUnaryNode.builder(Action.interactWithItem(ItemId.NOTES_21056, "Read"))
                              .condition(Condition.isWidgetVisible(NOTE_CLOSE_BUTTON))
                              .usePrecondition()
                              .status("Reading the note")
                              .build(),
                      AnonUnaryNode.builder(Action.clickInterface(NOTE_CLOSE_BUTTON))
                              .condition(Condition.isWidgetHidden(NOTE_CLOSE_BUTTON))
                              .usePrecondition()
                              .status("Closing the note")
                              .build()
              ),
            CommonNodes.walkPath(PATH_TO_PAINTING, 2),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.KNIFE, () -> SceneObjects.getNearest("Painting")))
                    .condition(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 45))
                    .usePrecondition()
                    .status("Slashing the painting")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Painting", "Search"))
                    .condition(Condition.hasItem(ItemId.RUBY_KEY_21053))
                    .usePrecondition()
                    .timeout(6000)
                    .status("Searching the painting")
                    .build(),
            CommonNodes.walkTo(CANDLE_ROOM),
            AnonUnaryNode.builder(Action.interactWithObject("Shelves", "Take-tinderbox"))
                    .condition(Condition.hasItem(ItemId.TINDERBOX))
                    .usePrecondition()
                    .status("Taking tinderbox")
                    .build(),
            createLightCandle(CANDLE_2),
            createLightCandle(CANDLE_3),
            createLightCandle(CANDLE_4),
            createLightCandle(CANDLE_1),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.TINDERBOX, () -> SceneObjects.getNearest("Barrel")))
                    .condition(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 60))
                    .usePrecondition()
                    .status("Using tinderbox on Barrel")
                    .build(),
            CommonNodes.walkTo(HALL),
            CommonNodes.wait(5000),
            CommonNodes.walkTo(CANDLE_ROOM),
            AnonUnaryNode.builder(Action.interactWithObject("Damaged wall", "Climb"))
                    .condition(Condition.isInArea(OUTSIDE))
                    .usePrecondition()
                    .status("Climbing over damaged wall")
                    .timeout(5000)
                    .wait(2000)
                    .build(),
            CommonNodes.walkTo(LACEY, 7),
            new WatchKillCutscene(),
            CommonNodes.wait(600),
            AnonUnaryNode.builder(Action.interactWithObject("Note", "Take"))
                    .condition(Condition.hasItem(ItemId.NOTES_21057))
                    .usePrecondition()
                    .status("Picking up notes")
                    .build(),
            UnaryNode.sequence(
                    Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 75),
                    AnonUnaryNode.builder(Action.interactWithItem(ItemId.NOTES_21057, "Read"))
                            .condition(Condition.isWidgetVisible(NOTE_CLOSE_BUTTON))
                            .usePrecondition()
                            .status("Reading the note")
                            .build(),
                    AnonUnaryNode.builder(Action.clickInterface(NOTE_CLOSE_BUTTON))
                            .condition(Condition.isWidgetHidden(NOTE_CLOSE_BUTTON))
                            .usePrecondition()
                            .status("Closing the note")
                            .build()
            ),
            CommonNodes.walkTo(PIANO, 7),
            new PlayPianoNode(),
            AnonUnaryNode.builder(Action.interactWithObject("Piano", "Search"))
                    .condition(Condition.hasItem(ItemId.EMERALD_KEY_21054))
                    .usePrecondition()
                    .status("Searching piano")
                    .build(),
            CommonNodes.walkTo(OUTSIDE),
            AnonUnaryNode.builder(Action.interactWithObject("Damaged wall", "Climb"))
                    .condition(Condition.isInArea(CANDLE_ROOM))
                    .usePrecondition()
                    .timeout(5000)
                    .wait(2000)
                    .status("Climbing over damaged wall")
                    .build(),
            CommonNodes.walkTo(GODSWORD_DOOR, 7),
            AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(GODSWORD_DOOR, "Door"), "Open"))
                    .condition(Game::isInCutscene)
                    .usePrecondition(() -> Game.isInCutscene() || Quests.getStage(Quest.MISTHALIN_MYSTERY) == 90)
                    .status("Opening the door")
                    .wait(8000)
                    .build(),
            CommonNodes.watchCutScene(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 90)),
            CommonNodes.wait(600),
            AnonUnaryNode.builder(Action.interactWithObject("Note", "Take"))
                    .condition(Condition.hasItem(ItemId.NOTES_21058))
                    .usePrecondition()
                    .status("Taking notes")
                    .build(),
            UnaryNode.sequence(
                    Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 95),
                    AnonUnaryNode.builder(Action.interactWithItem(ItemId.NOTES_21058, "Read"))
                            .condition(Condition.isWidgetVisible(NOTE_CLOSE_BUTTON))
                            .usePrecondition()
                            .status("Reading the note")
                            .build(),
                    AnonUnaryNode.builder(Action.clickInterface(NOTE_CLOSE_BUTTON))
                            .condition(Condition.isWidgetHidden(NOTE_CLOSE_BUTTON))
                            .usePrecondition()
                            .status("Closing the note")
                            .build()
            ),
            CommonNodes.walkPath(PATH_TO_FIREPLACE, 7),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.KNIFE, () -> SceneObjects.getNearest("Fireplace")))
                    .condition(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 100))
                    .usePrecondition()
                    .status("Slashing the fireplace")
                    .timeout(8000)
                    .build(),
            new SearchFireplace(),
            AnonUnaryNode.builder(Action.interactWithObject("Fireplace", "Search"))
                    .condition(Condition.hasItem(ItemId.SAPPHIRE_KEY_21055))
                    .usePrecondition()
                    .status("Searching fireplace for key")
                    .build(),
            CommonNodes.walkTo(BOSS_ROOM_DOOR, 7),
            AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(BOSS_ROOM_DOOR, "Door"), "Open"))
                    .condition(Game::isInCutscene)
                    .usePrecondition(() -> Game.isInCutscene() || Quests.getStage(Quest.MISTHALIN_MYSTERY) == 111)
                    .status("Opening the door")
                    .timeout(6000)
                    .build(),
            CommonNodes.watchCutScene(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 111)),
            new FightBossNode(),
            CommonNodes.watchCutScene(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 120)),
            CommonNodes.wait(600),
            CommonNodes.pickupItem(ItemId.KILLERS_KNIFE_21059),
            CommonNodes.equipItems(ItemId.KILLERS_KNIFE_21059),
            UnaryNode.sequence(
                    Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 125),
                    AnonUnaryNode.builder(Action.interactWithNpc("Abigale", "Fight"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Fighting Abigale")
                            .timeout(5000)
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(() -> !Dialog.isOpen())
                            .usePrecondition()
                            .status("Continuing dialogue")
                            .build()
            ),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Game::isInCutscene)
                    .usePrecondition(() -> Game.isInCutscene() || Quests.getStage(Quest.MISTHALIN_MYSTERY) == 130)
                    .status("Opening the door")
                    .build(),
            CommonNodes.watchCutScene(Condition.isAtStage(Quest.MISTHALIN_MYSTERY, 130)),
            CommonNodes.walkToNpc(MANDY, IN_FRONT_OF_MANSION),
            AnonUnaryNode.builder(new DialogAction(MANDY))
                    .condition(Condition.isQuestDone(Quest.MISTHALIN_MYSTERY))
                    .usePrecondition()
                    .status("Talking to " + MANDY)
                    .build()
    );

    private static UnaryNode createLightCandle(Position candlePosition) {
        return AnonUnaryNode.builder(Action.useItemOn(ItemId.TINDERBOX, () -> SceneObjects.getNearest(candlePosition, "Unlit candle")))
                .condition(() -> SceneObjects.query().distance(candlePosition, 2).names("Unlit candle").results().isEmpty())
                .usePrecondition()
                .timeout(3000)
                .status("Lighting candle")
                .build();
    }

    public MisthalinMysteryQuestBuilder() {
        super(NODES);
    }
}
