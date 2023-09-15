package net.rlbot.script.api.quests.tutorialisland;

import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.widgets.*;
import net.rlbot.api.game.HintArrow;
import net.rlbot.api.game.HintArrowType;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CombatNode;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class TutorialIslandQuestBuilder extends UnaryNodeQuestBuilder {

    private static final Position IN_FRONT_OF_GATE = new Position(3090, 3092, 0);

    private static final Area BEHIND_GATE = Area.rectangular(3086, 3093, 3089, 3090);

    private static final Position IN_FRONT_OF_BAKERY = new Position(3079, 3084, 0);
    private static final Area INSIDE_BAKERY = Area.rectangular(3073, 3086, 3078, 3083);

    private static final Position BAKERY_EXIT = new Position(3073, 3090, 0);

    private static final Area BEHIND_BAKERY = Area.rectangular(3070, 3091, 3072, 3089);

    private static final Position IN_FRONT_OF_QUEST_BUILDING = new Position(3086, 3126, 0);

    private static final Area QUEST_BUILDING = Area.rectangular(3083, 3125, 3089, 3119);

    private static final WidgetAddress MAKE_BRONZE_DAGGER = new WidgetAddress(312, 9);

    private static final Area BEHIND_FIGHT_AREA_ENTRANCE = Area.rectangular(3095, 9505, 3100, 9500);

    private static final WidgetAddress EQUIPMENT_STATS_BUTTON = new WidgetAddress(387, 1);

    private static final WidgetAddress EQUIP_YOUR_CHARACTER = new WidgetAddress(84, 1);

    private static final Position IN_FRONT_OF_FIGHT_AREA = new Position(3111, 9518, 0);

    private static final Area FIGHT_AREA = Area.polygonal(
            new Position(3110, 9521, 0),
            new Position(3111, 9520, 0),
            new Position(3111, 9518, 0),
            new Position(3110, 9517, 0),
            new Position(3107, 9517, 0),
            new Position(3107, 9521, 0)
    );

    public static final Area OUTSIDE_FIGHT_AREA = Area.rectangular(3111, 9520, 3113, 9516);

    private static final WidgetAddress POLL_HISTORY = new WidgetAddress(310, 1);

    private static final Area ACCOUNT_AREA = Area.rectangular(3125, 3125, 3129, 3123);

    private static final Position EXIT_ACCOUNT_AREA_DOOR = new Position(3130, 3124, 0);

    private static final Area OUTSIDE_ACCOUNT_AREA = Area.rectangular(3130, 3126, 3133, 3122);

    private static final Area CHURCH = Area.rectangular(3120, 3110, 3128, 3103);

    private static final Area OUTSIDE_CHURCH = Area.rectangular(3120, 3102, 3124, 3099);

    private static final Area MAGIC_AREA = Area.polygonal(
            new Position(3137, 3092, 0),
            new Position(3142, 3092, 0),
            new Position(3144, 3090, 0),
            new Position(3144, 3084, 0),
            new Position(3142, 3082, 0),
            new Position(3137, 3082, 0),
            new Position(3140, 3085, 0),
            new Position(3140, 3089, 0)
    );

    private static final Area LUMBRIDGE = Area.rectangular(3228, 3234, 3244, 3210);

    private static final List<UnaryNode> NODES = List.of(
            new ChooseDisplayNameNode(),
            new CharacterDesignNode(),
            new SetViewportFixed(),
            AnonUnaryNode.builder(new DialogAction("Gielinor Guide",2))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB))
                    .status("Talking to the guide")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB))
                    .condition(() -> Tabs.isOpen(Tab.OPTIONS))
                    .usePrecondition()
                    .status("Click the settings tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Gielinor Guide"))
                    .condition(() -> HintArrow.getType() == HintArrowType.COORDINATE)
                    .status("Talking to Gielinor Guide")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Opening the door")
                    .build(),
            CommonNodes.walkToNpcLocal("Survival Expert", 5),
            AnonUnaryNode.builder(new DialogAction("Survival Expert"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB))
                    .usePrecondition()
                    .status("Talking to Survival Expert")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Clicking the inventory icon")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithNpc("Fishing spot", "Net"))
                    .condition(Condition.hasItem(ItemId.RAW_SHRIMPS))
                    .usePrecondition()
                    .timeout(10000)
                    .status("Fishing")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_STATS_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Clicking the stats tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Survival Expert"))
                    .condition(Condition.hasItem(ItemId.BRONZE_AXE))
                    .usePrecondition()
                    .status("Talking to Survival Expert")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Tree", "Chop down"))
                    .condition(Condition.hasItem(ItemId.LOGS))
                    .usePrecondition()
                    .status("Chopping down tree")
                    .timeout(15000)
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.TINDERBOX, () -> Inventory.getFirst(ItemId.LOGS)))
                    .condition(() -> Skills.getExperience(Skill.FIREMAKING) > 0)
                    .usePrecondition(() -> Skills.getExperience(Skill.FIREMAKING) > 0 && SceneObjects.getNearest("Fire") != null)
                    .status("Lighting the log")
                    .timeout(10000)
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.RAW_SHRIMPS, () -> SceneObjects.getNearest("Fire")))
                    .condition(Condition.hasItem(ItemId.SHRIMPS))
                    .usePrecondition()
                    .status("Cooking the shrimp")
                    .build(),
            CommonNodes.walkTo(IN_FRONT_OF_GATE, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Gate", "Open"))
                    .condition(Condition.isInArea(BEHIND_GATE))
                    .usePrecondition()
                    .status("Opening the gate")
                    .timeout(5000)
                    .build(),
            CommonNodes.walkTo(IN_FRONT_OF_BAKERY, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Condition.isInArea(INSIDE_BAKERY))
                    .usePrecondition()
                    .timeout(5000)
                    .status("Opening the door")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Master Chef"))
                    .condition(Condition.hasItem(ItemId.BUCKET_OF_WATER))
                    .usePrecondition()
                    .status("Talking to Master Cef")
                    .build(),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.POT_OF_FLOUR, () -> Inventory.getFirst(ItemId.BUCKET_OF_WATER)))
                    .condition(Condition.hasItem(ItemId.BREAD_DOUGH))
                    .usePrecondition()
                    .status("Using the Pot of flour on the Bucket of water")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Range", "Cook"))
                    .condition(Condition.hasItem(ItemId.BREAD))
                    .usePrecondition()
                    .timeout(8000)
                    .status("Cooking the dough")
                    .build(),
            CommonNodes.walkTo(BAKERY_EXIT, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Condition.isInArea(BEHIND_BAKERY))
                    .usePrecondition()
                    .timeout(8000)
                    .status("Opening the door")
                    .build(),
            CommonNodes.walkTo(IN_FRONT_OF_QUEST_BUILDING, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Condition.isInArea(QUEST_BUILDING))
                    .usePrecondition()
                    .status("Opening the door")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Quest Guide"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB))
                    .usePrecondition()
                    .status("Talking to the Quest Guide")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB))
                    .condition(() -> Tabs.isOpen(Tab.QUESTS))
                    .usePrecondition()
                    .status("Opening the quest tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Quest Guide"))
                    .condition(() -> HintArrow.getType() == HintArrowType.COORDINATE)
                    .usePrecondition()
                    .status("Talking to the Quest Guide")
                    .build(),
            CommonNodes.useLadder(false, -1),
            CommonNodes.walkToNpcLocal("Mining Instructor", 5),
            AnonUnaryNode.builder(new DialogAction("Mining Instructor"))
                    .condition(Condition.hasItem(ItemId.BRONZE_PICKAXE))
                    .usePrecondition()
                    .status("Talking to the Mining Instructor")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Tin rocks", "Mine"))
                    .condition(Condition.hasItem(ItemId.TIN_ORE))
                    .usePrecondition()
                    .timeout(10000)
                    .status("Mining the tin rocks")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Copper rocks", "Mine"))
                    .condition(Condition.hasItem(ItemId.COPPER_ORE))
                    .usePrecondition()
                    .timeout(12000)
                    .status("Mining the copper rocks")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Furnace", "Use"))
                    .condition(Condition.hasItem(ItemId.BRONZE_BAR))
                    .usePrecondition()
                    .timeout(10000)
                    .status("Using the furnace")
                    .build(),
            CommonNodes.walkToNpcLocal("Mining Instructor", 5),
            AnonUnaryNode.builder(new DialogAction("Mining Instructor"))
                    .condition(Condition.hasItem(ItemId.HAMMER))
                    .usePrecondition()
                    .status("Talking to the Mining Instructor")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Anvil", "Smith"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.SMITHING_INVENTORY_ITEMS_CONTAINER))
                    .usePrecondition()
                    .status("Using the Anvil")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(MAKE_BRONZE_DAGGER))
                    .condition(Condition.hasItem(ItemId.BRONZE_DAGGER))
                    .usePrecondition()
                    .status("Making a Bronze dagger")
                    .build(),
            CommonNodes.walkToObjectLocal("Gate", 5),
            AnonUnaryNode.builder(Action.interactWithObject("Gate", "Open"))
                    .condition(Condition.isInArea(BEHIND_FIGHT_AREA_ENTRANCE))
                    .usePrecondition()
                    .status("Opening the gate")
                    .build(),
            CommonNodes.walkToNpcLocal("Combat Instructor", 5),
            AnonUnaryNode.builder(new DialogAction("Combat Instructor"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB))
                    .usePrecondition()
                    .status("Talking to the Combat Instructor")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB))
                    .condition(Condition.isWidgetVisible(EQUIPMENT_STATS_BUTTON))
                    .usePrecondition()
                    .status("Opening the equipment tab")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(EQUIPMENT_STATS_BUTTON))
                    .condition(Condition.isWidgetVisible(EQUIP_YOUR_CHARACTER))
                    .usePrecondition()
                    .status("Opening the equipment stats overview")
                    .build(),
            AnonUnaryNode.builder(() -> {

                        var equipmentInventory = Widgets.get(WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER);

                        if (equipmentInventory == null) {
                            return false;
                        }

                        for (var child : equipmentInventory.getDynamicChildren()) {

                            if (child.getItemId() != ItemId.BRONZE_DAGGER) {
                                continue;
                            }

                            return child.interact("Equip");
                        }

                        return false;
                    })
                    .condition(Condition.isWearing(ItemId.BRONZE_DAGGER))
                    .usePrecondition()
                    .status("Equipping the bronze dagger")
                    .build(),
            AnonUnaryNode.builder(Action.closeWidget())
                    .condition(() -> !EQUIP_YOUR_CHARACTER.isWidgetVisible())
                    .usePrecondition()
                    .status("Closing the overview")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Combat Instructor"))
                    .condition(Condition.hasItem(ItemId.BRONZE_SWORD))
                    .usePrecondition()
                    .status("Talking to the Combat Instructor")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.BRONZE_SWORD, "Wield"))
                    .condition(Condition.isWearing(ItemId.BRONZE_SWORD))
                    .usePrecondition()
                    .status("Wielding the Bronze sword")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.WOODEN_SHIELD, "Wield"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB))
                    .usePrecondition()
                    .status("Wield the Wooden shield")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.COORDINATE)
                    .usePrecondition()
                    .status("Opening the combat tab")
                    .build(),
            CommonNodes.walkTo(IN_FRONT_OF_FIGHT_AREA, 5),
            AnonUnaryNode.builder(Action.interactWithObject("Gate", "Open"))
                    .condition(Condition.isInArea(FIGHT_AREA))
                    .usePrecondition()
                    .timeout(7000)
                    .status("Opening the gate")
                    .build(),
            CombatNode.builder(() -> {
                        var target = HintArrow.getTargetActor();
                        return target instanceof Npc npc ? npc : null;
                    }, "Giant rat")
                    .condition(() -> {
                        var target = HintArrow.getTargetActor();
                        return target != null && target.getName().equals("Combat Instructor");
                    })
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Gate", "Open"))
                    .condition(Condition.isInArea(OUTSIDE_FIGHT_AREA))
                    .usePrecondition()
                    .timeout(8000)
                    .status("Opening the gate")
                    .build(),
            CommonNodes.walkToNpcLocal("Combat Instructor", 5),
            AnonUnaryNode.builder(new DialogAction("Combat Instructor"))
                    .condition(Condition.hasItem(ItemId.SHORTBOW))
                    .usePrecondition()
                    .status("Talking to Combat Instructor")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.SHORTBOW, "Wield"))
                    .condition(Condition.isWearing(ItemId.SHORTBOW))
                    .usePrecondition()
                    .status("Wielding shortbow")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.BRONZE_ARROW, "Wield"))
                    .condition(Condition.isWearing(ItemId.BRONZE_ARROW))
                    .usePrecondition()
                    .status("Wielding bronze arrows")
                    .build(),
            CombatNode.builder(() -> {
                        var target = HintArrow.getTargetActor();
                        return target instanceof Npc npc ? npc : null;
                    }, "Giant rat")
                    .condition(() -> HintArrow.getType() == HintArrowType.COORDINATE)
                    .build(),
            CommonNodes.walkToObjectLocal("Ladder", 5),
            CommonNodes.useLadder(true, 0),
            CommonNodes.walkToObjectLocal("Bank booth", 5),
            AnonUnaryNode.builder(Action.interactWithObject("Bank booth", "Use"))
                    .condition(Bank::isOpen)
                    .usePrecondition()
                    .status("Opening the bank")
                    .build(),
            AnonUnaryNode.builder(Action.closeWidget())
                    .condition(() -> !Bank.isOpen())
                    .usePrecondition()
                    .status("Closing the bank")
                    .build(),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(26815), "Use"))
                            .condition(Dialog::canContinue)
                            .usePrecondition()
                            .status("Opening the poll booth")
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isWidgetVisible(POLL_HISTORY))
                            .usePrecondition()
                            .status("Continuing the dialog")
                            .build()
            ),
            AnonUnaryNode.builder(Action.closeWidget())
                    .condition(() -> !POLL_HISTORY.isWidgetVisible())
                    .usePrecondition()
                    .status("Closing the poll booth")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getNearest(HintArrow.getTargetPosition(), "Door"), "Open"))
                    .condition(Condition.isInArea(ACCOUNT_AREA))
                    .usePrecondition()
                    .status("Opening the door")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Account Guide"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_IGNORES_TAB))
                    .usePrecondition()
                    .status("Talking to the Account Guide")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_IGNORES_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Opening the ignores tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Account Guide"))
                    .condition(() -> HintArrow.getType() == HintArrowType.COORDINATE)
                    .usePrecondition()
                    .status("Talking to the Account Guide")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject(() -> SceneObjects.getAt(EXIT_ACCOUNT_AREA_DOOR, "Door").stream().findFirst().orElse(null), "Open"))
                    .condition(Condition.isInArea(OUTSIDE_ACCOUNT_AREA))
                    .usePrecondition()
                    .status("Opening the door")
                    .build(),
            CommonNodes.walkToNpc("Brother Brace", CHURCH),
            AnonUnaryNode.builder(new DialogAction("Brother Brace"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB))
                    .usePrecondition()
                    .status("Talking to Brother Brace")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Opening the prayer tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Brother Brace"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB))
                    .usePrecondition()
                    .status("Talking to Brother Brace")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Opening the friends tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Brother Brace"))
                    .condition(() -> HintArrow.getType() == HintArrowType.COORDINATE)
                    .usePrecondition()
                    .status("Talking to Brother Brace")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Door", "Open"))
                    .condition(Condition.isInArea(OUTSIDE_CHURCH))
                    .usePrecondition()
                    .timeout(5000)
                    .status("Opening the door")
                    .build(),
            CommonNodes.walkToNpc("Magic Instructor", MAGIC_AREA),
            AnonUnaryNode.builder(new DialogAction("Magic Instructor"))
                    .condition(Condition.isWidgetVisible(WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB))
                    .usePrecondition()
                    .status("Talking to the Magic Instructor")
                    .build(),
            AnonUnaryNode.builder(Action.clickInterface(WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB))
                    .condition(() -> HintArrow.getType() == HintArrowType.NPC)
                    .usePrecondition()
                    .status("Opening the magic tab")
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Magic Instructor"))
                    .condition(Condition.hasItem(ItemId.AIR_RUNE))
                    .usePrecondition()
                    .status("Talking to Magic Instructor")
                    .build(),
            CombatNode.builder("Chicken")
                    .castSpell(SpellBook.Standard.WIND_STRIKE)
                    .condition(() -> Skills.getExperience(Skill.MAGIC) > 0)
                    .build(),
            AnonUnaryNode.builder(new DialogAction("Magic Instructor",1, 3))
                    .condition(Condition.isInArea(LUMBRIDGE))
                    .usePrecondition()
                    .timeout(10000)
                    .status("Talking to Magic Instructor")
                    .build()
    );

    public TutorialIslandQuestBuilder() {
        super(NODES);
    }

}
