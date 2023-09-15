package net.rlbot.script.api.quests.biohazard;

import net.rlbot.api.common.BankLocation;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
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
import net.rlbot.script.api.quests.biohazard.nodes.BuyGownNode;

import java.util.List;

public class BiohazardQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.BIOHAZARD;

    public static final int FOOD = ItemId.SALMON;

    private static final Loadout LOADOUT = Loadout.builder()
            .withItem(ItemId.COINS_995).amount(560).build()
            .withItem(ItemId.STAMINA_POTION4).amount(2).build()
            .withItem(FOOD).amount(2).build()
            .withItem(ItemId.MIND_RUNE).amount(100).build()
            .withItem(ItemId.AIR_RUNE).amount(100).build()
            .withItem(ItemId.EARTH_RUNE).amount(100).build()
            .withItem(ItemId.SKILLS_NECKLACE4).build()
            .withItem(ItemId.VARROCK_TELEPORT).amount(2).build()
            .withEquipmentSet()
            .with(ItemId.GAS_MASK).build()
            .build()
            .build();

    private static final Area ELENAS_HOUSE = Area.rectangular(2590, 3338, 2594, 3334);

    private static final String ELENA = "Elena";

    private static final Area JERICOS_HOUSE = Area.rectangular(2611, 3326, 2617, 3323);

    private static final String JERICO = "Jerico";

    private static final Area BEHIND_HOUSE = Area.rectangular(2618, 3327, 2621, 3322);

    private static final Area WEST_OF_MANSION = Area.rectangular(2559, 3273, 2560, 3264);

    private static final String OMART = "Omart";

    private static final Position SPEAR_FENCE_POSITION = new Position(2563, 3301, 0);

    private static final Area OVER_WALL = Area.rectangular(2549, 3281, 2557, 3265);
    ;

    private static final Position ROTTEN_APPLE = new Position(2535, 3333, 0);

    private static final Area BACKYARD = Area.rectangular(2542, 3333, 2555, 3328);

    private static final Area SOUTH_WESTERN_HOUSE = Area.polygonal(
            new Position(2515, 3277, 0),
            new Position(2519, 3277, 0),
            new Position(2519, 3273, 0),
            new Position(2518, 3273, 0),
            new Position(2518, 3270, 0),
            new Position(2515, 3270, 0)
    );

    private static final Position MOURNERS_ENTRANCE = new Position(2551, 3319, 0);

    private static final Area MOURNER_GROUND_FLOOR = Area.rectangular(2547, 3327, 2555, 3321, 0);

    private static final Area MOURNER_AREA = Area.rectangular(2547, 3327, 2555, 3321, 1);

    private static final String MOURNER = "Mourner";

    private static final Area STORAGE = Area.rectangular(2552, 3327, 2555, 3324, 1);

    private static final Position CRATE_1 = new Position(2554, 3327, 1);
    private static final Position CRATE_2 = new Position(2555, 3326, 1);

    private static final Area FISHING_GUILD = Area.rectangular(2595, 3393, 2618, 3382);

    private static final String CHEMIST = "Chemist";

    private static final Area CHEMIST_HOUSE = Area.polygonal(
            new Position(2929, 3214, 0),
            new Position(2937, 3214, 0),
            new Position(2937, 3211, 0),
            new Position(2939, 3211, 0),
            new Position(2940, 3210, 0),
            new Position(2940, 3208, 0),
            new Position(2939, 3207, 0),
            new Position(2929, 3207, 0)
    );

    private static final Area OUTSIDE_CHEMIST = Area.rectangular(2924, 3224, 2936, 3214);

    private static final String HOPS = "Hops";

    private static final String DA_VINCI = "Da Vinci";

    private static final String CHANCY = "Chancy";

    private static final Area MONKS_OF_ENTRANA = Area.rectangular(3040, 3237, 3049, 3234);

    public static final String THESSALIA = "Thessalia";

    private static final Area SHOP = Area.polygonal(
            new Position(3208, 3420, 0),
            new Position(3210, 3418, 0),
            new Position(3209, 3417, 0),
            new Position(3209, 3412, 0),
            new Position(3208, 3411, 0),
            new Position(3207, 3411, 0),
            new Position(3201, 3417, 0),
            new Position(3204, 3420, 0)
    );

    private static final Loadout LOADOUT2 = Loadout.builder()
            .withItem(ItemId.COINS_995).amount(10).build()
            .withItem(ItemId.STAMINA_POTION4).amount(0, 2).build()
            .withItem(FOOD).amount(2).build()
            .withItem(ItemIds.SKILLS_NECKLACE).build()
            .withItem(ItemId.VARROCK_TELEPORT).amount(1).build()
            .withItem(ItemId.PRIEST_GOWN).build()
            .withItem(ItemId.PRIEST_GOWN_428).build()
            .withItem(ItemId.TOUCH_PAPER).build()
            .withItem(ItemId.PLAGUE_SAMPLE).build()
            .build();

    private static final Area DONKEY_INN = Area.polygonal(
            new Position(3266, 3395, 0),
            new Position(3270, 3395, 0),
            new Position(3273, 3392, 0),
            new Position(3275, 3392, 0),
            new Position(3275, 3388, 0),
            new Position(3266, 3388, 0)
    );

    private static final Area LIVING_ROOM = Area.rectangular(3279, 3384, 3282, 3380);

    private static final Area BEDROOM = Area.rectangular(3283, 3384, 3285, 3380);

    private static final String GUIDOR = "Guidor";

    private static final Area THRONE_ROOM = Area.rectangular(2575, 3294, 2580, 3292, 1);

    private static final String KING_LATHAS = "King Lathas";

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(ELENA, ELENAS_HOUSE),
            AnonUnaryNode.builder(new DialogAction(ELENA, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to " + ELENA)
                    .build(),
            CommonNodes.walkToNpc(JERICO, JERICOS_HOUSE),
            AnonUnaryNode.builder(new DialogAction(JERICO))
                    .condition(Condition.isAtStage(QUEST, 2))
                    .usePrecondition()
                    .status("Talking to " + JERICO)
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Cupboard", "Open"))
                    .condition(Condition.objectHasFirstAction("Cupboard", "Search"))
                    .usePrecondition()
                    .status("Opening the cupboard")
                    .build(),
            new SearchForItemNode("Cupboard", "Search", ItemId.BIRD_FEED),
            CommonNodes.walkTo(BEHIND_HOUSE),
            CommonNodes.pickupItem(ItemId.PIGEON_CAGE),
//            TODO: Find condition
            CommonNodes.walkToNpc(OMART, WEST_OF_MANSION),
            AnonUnaryNode.builder(new DialogAction(OMART))
                    .status("Talking to " + OMART)
                    .build(),
            CommonNodes.walkTo(SPEAR_FENCE_POSITION, 0),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.BIRD_FEED, () -> SceneObjects.getNearest("Watchtower")))
                    .condition(Condition.doesntHaveItem(ItemId.BIRD_FEED))
                    .usePrecondition()
                    .status("Using the bird feed on the watchtower")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.PIGEON_CAGE, "Open"))
                    .condition(Condition.doesntHaveItem(ItemId.PIGEON_CAGE))
                    .usePrecondition()
                    .wait(2000)
                    .status("Opening the pigeon case")
                    .build(),
            CommonNodes.walkToNpc(OMART, WEST_OF_MANSION),
            AnonUnaryNode.builder(new DialogAction(OMART, 1))
                    .condition(Condition.isInArea(OVER_WALL))
                    .usePrecondition()
                    .timeout(10000)
                    .wait(3000)
                    .reset(Condition.isAnimating())
                    .status("Talking to " + OMART)
                    .build(),
            CommonNodes.walkTo(ROTTEN_APPLE, 7),
            CommonNodes.pickupItem(ItemId.ROTTEN_APPLE),
            AnonUnaryNode.builder(Action.interactWithObject("Fence", "Squeeze-through"))
                    .condition(Condition.isInArea(BACKYARD))
                    .usePrecondition()
                    .wait(600)
                    .timeout(6000)
                    .status("Squeezing through the fence")
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.ROTTEN_APPLE, () -> SceneObjects.getNearest("Cauldron")))
                    .condition(Condition.doesntHaveItem(ItemId.ROTTEN_APPLE))
                    .usePrecondition()
                    .status("Using the rotten apple on the cauldron")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Fence", "Squeeze-through"))
                    .condition(Condition.isNotInArea(BACKYARD))
                    .usePrecondition()
                    .wait(600)
                    .timeout(6000)
                    .status("Squeezing through the fence")
                    .build(),
            CommonNodes.walkTo(SOUTH_WESTERN_HOUSE),
            AnonUnaryNode.builder(Action.interactWithObject("Cupboard", "Open"))
                    .condition(Condition.objectHasFirstAction("Cupboard", "Search"))
                    .usePrecondition()
                    .status("Opening the cupboard")
                    .build(),
            new SearchForItemNode("Cupboard", "Search", ItemId.MEDICAL_GOWN),
            CommonNodes.equipItems(ItemId.MEDICAL_GOWN),
            CommonNodes.walkTo(MOURNERS_ENTRANCE, 7),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Opening the door")
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isInArea(MOURNER_GROUND_FLOOR))
                            .usePrecondition()
                            .status("Continuing the dialogue")
                            .timeout(5000)
                            .wait(600)
                            .build()
            ),
            CommonNodes.walkToNpc(MOURNER, MOURNER_AREA),
            CombatNode.builder(MOURNER)
                    .castSpell(SpellBook.Standard.EARTH_STRIKE)
                    .condition(Condition.hasItem(ItemId.KEY_423))
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.KEY_423, () -> SceneObjects.getNearest("Gate")))
                    .condition(Condition.isInArea(STORAGE))
                    .usePrecondition()
                    .timeout(8000)
                    .wait(600)
                    .status("Entering the storage area")
                    .build(),
            new SearchForItemNode(() -> SceneObjects.getFirstAt(CRATE_1, "Crate"), "Search", ItemId.DISTILLATOR),
//            TODO: Necessary?
//            new SearchForItemNode(() -> SceneObjects.getFirstAt(CRATE_2, "Crate"), "Search", ItemId.DISTILLATOR),
            CommonNodes.useTeleport(ItemIds.SKILLS_NECKLACE, "Fishing Guild", FISHING_GUILD),
            CommonNodes.walkToNpc(ELENA, ELENAS_HOUSE),
            AnonUnaryNode.builder(new DialogAction(ELENA))
                    .condition(Condition.hasItem(ItemId.PLAGUE_SAMPLE))
                    .usePrecondition()
                    .status("Talking to Elena")
                    .build(),
            CommonNodes.walkToNpc(CHEMIST, CHEMIST_HOUSE),
            AnonUnaryNode.builder(new DialogAction(CHEMIST, 2, 2))
                    .condition(Condition.hasItem(ItemId.TOUCH_PAPER))
                    .usePrecondition()
                    .status("Talking to " + CHEMIST)
                    .build(),
            CommonNodes.walkToNpc(HOPS, OUTSIDE_CHEMIST),
            AnonUnaryNode.builder(new DialogAction(HOPS, 3))
                    .condition(Condition.doesntHaveItem(ItemId.SULPHURIC_BROLINE))
                    .usePrecondition()
                    .status("Talking to " + HOPS)
                    .build(),
            AnonUnaryNode.builder(new DialogAction(DA_VINCI, 1))
                    .condition(Condition.doesntHaveItem(ItemId.ETHENEA))
                    .usePrecondition()
                    .status("Talking to " + DA_VINCI)
                    .build(),
            AnonUnaryNode.builder(new DialogAction(CHANCY, 2))
                    .condition(Condition.doesntHaveItem(ItemId.LIQUID_HONEY))
                    .usePrecondition()
                    .status("Talking to " + CHANCY)
                    .build(),
            CommonNodes.walkTo(MONKS_OF_ENTRANA),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.TOUCH_PAPER, () -> SceneObjects.getNearest("Bank deposit box")))
                    .condition(Condition.doesntHaveItem(ItemId.TOUCH_PAPER))
                    .usePrecondition()
                    .status("Using the touch paper on the bank deposit box")
                    .build(),
            CommonNodes.walkToNpc(THESSALIA, SHOP),
            new BuyGownNode(),
            CommonNodes.walkTo(BankLocation.VARROCK_EAST_BANK.getArea()),
            new SetupLoadout(LOADOUT2),
            CommonNodes.walkToNpc(HOPS, DONKEY_INN),
            AnonUnaryNode.builder(new DialogAction(DA_VINCI, 1))
                    .condition(Condition.hasItem(ItemId.ETHENEA))
                    .usePrecondition()
                    .status("Talking to " + DA_VINCI)
                    .build(),
            AnonUnaryNode.builder(new DialogAction(HOPS))
                    .condition(Condition.hasItem(ItemId.SULPHURIC_BROLINE))
                    .usePrecondition()
                    .status("Talking to " + HOPS)
                    .build(),
            AnonUnaryNode.builder(new DialogAction(CHANCY))
                    .condition(Condition.hasItem(ItemId.LIQUID_HONEY))
                    .usePrecondition()
                    .status("Talking to " + CHANCY)
                    .build(),
            CommonNodes.equipItems(ItemId.PRIEST_GOWN, ItemId.PRIEST_GOWN_428),
            CommonNodes.walkTo(LIVING_ROOM),
            AnonUnaryNode.builder(Action.interactWithObject("Bedroom door", "Open"))
                    .condition(Condition.isInArea(BEDROOM))
                    .usePrecondition()
                    .status("Entering the bedroom")
                    .build(),
            AnonUnaryNode.builder(new DialogAction(GUIDOR, 1, 2, 1))
                    .condition(Condition.isAtStage(QUEST, 14))
                    .usePrecondition()
                    .status("Talking to " + GUIDOR)
                    .build(),
            CommonNodes.useTeleport(ItemIds.SKILLS_NECKLACE, "Fishing Guild", FISHING_GUILD),
            CommonNodes.walkToNpc(ELENA, ELENAS_HOUSE),
            AnonUnaryNode.builder(new DialogAction(ELENA))
                    .condition(Condition.isAtStage(QUEST, 15))
                    .usePrecondition()
                    .status("Talking to " + ELENA)
                    .build(),
            CommonNodes.walkToNpc(KING_LATHAS, THRONE_ROOM),
            AnonUnaryNode.builder(new DialogAction(KING_LATHAS))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .status("Talking to " + KING_LATHAS)
                    .build()
    );


    public BiohazardQuestBuilder() {
        super(NODES);
    }
}
