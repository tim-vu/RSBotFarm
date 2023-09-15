package net.rlbot.script.api.quests.witchspotion;

import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.Action;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CombatNode;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class WitchsPotionQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.WITCHS_POTION;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.ONION).build()
                .withItem(ItemId.EYE_OF_NEWT).build()
                .withItem(ItemId.COOKED_MEAT).build()
            .build();
    private static final Area HETTY_HUT = Area.rectangular(2965, 3208, 2970, 3203);

    private static final String HETTY = "Hetty";

    private static final Area RAT_HUT = Area.rectangular(2953, 3205, 2960, 3202);

    private static final Area RESTAURANT = Area.rectangular(2963, 3216, 2970, 3210);

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(HETTY, HETTY_HUT),
            AnonUnaryNode.builder(new DialogAction(HETTY, 1, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to " + HETTY)
                    .build(),
            CommonNodes.walkToNpc("Rat", RAT_HUT),
            CombatNode.builder("Rat")
                    .condition(Condition.pickableExists(ItemId.RATS_TAIL))
                    .build(),
            CommonNodes.pickupItem(ItemId.RATS_TAIL),
            CommonNodes.walkTo(RESTAURANT),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.COOKED_MEAT, () -> SceneObjects.getNearest("Range")))
                    .condition(Condition.hasItem(ItemId.BURNT_MEAT))
                    .usePrecondition()
                    .status("Using the cooked meat on the stove")
                    .build(),
            CommonNodes.walkToNpc(HETTY, HETTY_HUT),
            AnonUnaryNode.builder(new DialogAction(HETTY))
                    .condition(Condition.doesntHaveItem(ItemId.BURNT_MEAT))
                    .usePrecondition()
                    .status("Talking to " + HETTY)
                    .build(),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.interactWithObject("Cauldron", "Drink From"))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Drinking from the dialog")
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isQuestDone(QUEST))
                            .usePrecondition()
                            .status("Dismissing the dialogue")
                            .build()
            )
    );

    public WitchsPotionQuestBuilder() {
        super(NODES);
    }
}
