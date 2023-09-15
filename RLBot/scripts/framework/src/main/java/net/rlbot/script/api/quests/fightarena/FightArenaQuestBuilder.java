package net.rlbot.script.api.quests.fightarena;

import net.rlbot.api.Game;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CombatNode;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.common.SearchForItemNode;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.Arrays;
import java.util.List;

public class FightArenaQuestBuilder extends UnaryNodeQuestBuilder {

    public static final int FOOD = ItemId.SALMON;
    public static final Quest QUEST = Quest.FIGHT_ARENA;
    private static final Loadout LOADOUT = Loadout.builder()
            .withItem(ItemId.COINS_995).amount(5).build()
            .withItem(ItemId.MIND_RUNE).amount(300).build()
            .withItem(ItemId.AIR_RUNE).amount(500).build()
            .withItem(ItemId.EARTH_RUNE).amount(300).build()
            .withItem(FOOD).amount(6).build()
            .withItem(ItemIds.STAMINA_POTION).minimumDose(false).build()
            .withItem(ItemId.VARROCK_TELEPORT).build()
            .withEquipmentSet()
            .with(ItemId.STAFF_OF_FIRE).build()
            .build()
            .build();

    private static final Area QUEST_START = Area.rectangular(2560, 3204, 2572, 3192);

    private static final Position HOUSE_ENTRANCE = new Position(2608, 3189, 0);

    private static final Area HOUSE = Area.polygonal(
            new Position(2612, 3194, 0),
            new Position(2614, 3194, 0),
            new Position(2615, 3193, 0),
            new Position(2615, 3190, 0),
            new Position(2614, 3189, 0),
            new Position(2613, 3189, 0),
            new Position(2612, 3190, 0),
            new Position(2612, 3191, 0),
            new Position(2608, 3191, 0),
            new Position(2607, 3192, 0),
            new Position(2607, 3194, 0));

    private static final Area CORNER_ROOM = Area.polygonal(
            new Position(2613, 3147, 0),
            new Position(2620, 3147, 0),
            new Position(2620, 3139, 0),
            new Position(2609, 3139, 0),
            new Position(2609, 3146, 0),
            new Position(2612, 3146, 0)
    );

    private static final Area BAR = Area.polygonal(
            new Position(2573, 3139, 0),
            new Position(2573, 3145, 0),
            new Position(2571, 3145, 0),
            new Position(2571, 3151, 0),
            new Position(2563, 3151, 0),
            new Position(2563, 3139, 0));

    private static final Area FIGHT_ARENA = Area.polygonal(
            new Position(2605, 3151, 0),
            new Position(2608, 3154, 0),
            new Position(2608, 3168, 0),
            new Position(2603, 3173, 0),
            new Position(2586, 3173, 0),
            new Position(2581, 3168, 0),
            new Position(2581, 3156, 0),
            new Position(2586, 3151, 0));

    private static final Area CELL = Area.rectangular(2597, 3144, 2601, 3142);

    private static final Position NORTH_JAIL_ENTRACE = new Position(2617, 3171, 0);

    private static final String HEAD_GUARD = "Head Guard";

    private static final Position WEST_JAIL_ENTRANCE = new Position(2585, 3141, 0);
    private static final Area JAIL_NORTH = Area.rectangular(2617, 3171, 2619, 3155);
    private static final Area JAIL_WEST = Area.rectangular(2585, 3144, 2603, 3139);

    private static final Area SAMMY_CELL = Area.rectangular(2617, 3170, 2619, 3164);
    private static final Position SAMMY_PRISON_DOOR = new Position(2617, 3167, 0);
    private static final Position SAFE_SPOT = new Position(2598, 3161, 0);

    private static final String LADY_SERVIL = "Lady Servil";
    private static final String SAMMY_SERVIL = "Sammy Servil";
    private static final String KHAZARD_GUARD = "Khazard Guard";
    private static final String KHAZARD_BARMAN = "Khazard Barman";

    public static final String HENGRAD = "Hengrad";
    public static final int SAFESPOT_DISTANCE = 5;
    private static final List<UnaryNode> NODES = Arrays.asList(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(LADY_SERVIL, QUEST_START),
            AnonUnaryNode.builder(new DialogAction(LADY_SERVIL, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to Lady Servil")
                    .build(),
//            CommonNodes.walkTo(HOUSE, "the house"),
//            CommonNodes.walkPath(LADY_SERVIL_TO_HOUSE, 5),
            CommonNodes.walkTo(HOUSE_ENTRANCE, 7),
            AnonUnaryNode.builder(Action.interactWithObject("Chest", "Open"))
                    .condition(Condition.objectHasFirstAction("Chest", "Search"))
                    .usePrecondition()
                    .status("Opening the chest").build(),
            new SearchForItemNode("Chest", "Search", ItemId.KHAZARD_HELMET),
            CommonNodes.equipItems(ItemId.KHAZARD_HELMET, ItemId.KHAZARD_ARMOUR),
            CommonNodes.walkTo(NORTH_JAIL_ENTRACE, 5),
            createEnterJailNode(NORTH_JAIL_ENTRACE, JAIL_NORTH),
            AnonUnaryNode.builder(new DialogAction(SAMMY_SERVIL))
                    .condition(Condition.isAtStage(QUEST, 2))
                    .usePrecondition()
                    .status("Talking to Sammy Servil").build(),
            CommonNodes.walkToNpc(KHAZARD_GUARD, CORNER_ROOM),
            AnonUnaryNode.builder(new DialogAction(HEAD_GUARD))
                    .condition(Condition.isAtStage(QUEST, 3))
                    .status("Talking to the " + HEAD_GUARD)
                    .build(),
            CommonNodes.walkToNpc(KHAZARD_BARMAN, BAR),
            AnonUnaryNode.builder(new DialogAction(KHAZARD_BARMAN, 2))
                    .condition(Condition.hasItem(ItemId.KHALI_BREW))
                    .usePrecondition()
                    .status("Buying a khali brew").build(),
            CommonNodes.walkTo(WEST_JAIL_ENTRANCE, 7),
            createEnterJailNode(WEST_JAIL_ENTRANCE, JAIL_WEST),
            CommonNodes.walkToNpc(KHAZARD_GUARD, CORNER_ROOM),
            AnonUnaryNode.builder(new DialogAction(HEAD_GUARD))
                    .condition(Condition.hasItem(ItemId.KHAZARD_CELL_KEYS))
                    .usePrecondition()
                    .status("Giving the brew to the " + HEAD_GUARD)
                    .build(),
            CommonNodes.walkTo(SAMMY_PRISON_DOOR, 5),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.useItemOn(ItemId.KHAZARD_CELL_KEYS, () -> SceneObjects.query().locations(SAMMY_PRISON_DOOR).names("Prison Gate").results().first()))
                            .condition(Dialog::isOpen)
                            .usePrecondition().build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(() -> Game.isInCutscene() || Quests.getStage(QUEST) == 6)
                            .status("Dismissing the dialogue")
                            .build()
            ),
            //CUTSCENE
            CommonNodes.watchCutScene(Condition.isAtStage(QUEST, 6)),
            CommonNodes.enableAutoCast(SpellBook.Standard.FIRE_STRIKE, Magic.Autocast.Mode.OFFENSIVE),
            CombatNode.builder("Khazard Ogre")
                    .condition(Condition.isAtStage(QUEST, 8))
                    .safespot(SAFE_SPOT, 5).build(),
            CommonNodes.watchCutScene(Condition.isAtStage(QUEST, 9)),
            CombatNode.builder("Khazard Scorpion")
                    .condition(Condition.isAtStage(QUEST, 10))
                    .safespot(SAFE_SPOT, SAFESPOT_DISTANCE).build(),
            //CUTSCENE
            CommonNodes.watchCutScene(() -> true),
            CombatNode.builder("Bouncer")
                    .condition(Condition.isAtStage(QUEST, 11))
                    .safespot(SAFE_SPOT, SAFESPOT_DISTANCE).build(),
            //CUTSCENE
            CommonNodes.watchCutScene(Condition.isAtStage(QUEST, 12)),
            CommonNodes.walkToNpc(LADY_SERVIL, QUEST_START),
            AnonUnaryNode.builder(new DialogAction(LADY_SERVIL))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .status("Taling to Lady Servil").build()
    );

    private static UnaryNode createDismissDialog() {
        return AnonUnaryNode.builder(new DialogAction())
                .condition(() -> !Dialog.isOpen())
                .usePrecondition()
                .status("Dismissing the dialogue").build();
    }

    private static UnaryNode createEnterJailNode(Position position, Area jailArea) {
        return AnonUnaryNode.sequence(
                AnonUnaryNode.builder(Action.interactWithObject(position, "Door", "Open"))
                        .condition(Dialog::isOpen)
                        .usePrecondition()
                        .status("Opening the door").build(),
                AnonUnaryNode.builder(new DialogAction())
                        .condition(Condition.isInArea(jailArea))
                        .usePrecondition()
                        .status("Dismissing the dialogue").build()
        );
    }

    public FightArenaQuestBuilder() {
        super(NODES);
    }
}
