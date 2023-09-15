package net.rlbot.script.api.quests.cooksassistant;

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
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class CooksAssistantQuestBuilder extends UnaryNodeQuestBuilder {

    private static final Area KITCHEN = Area.rectangular(3205, 3217, 3212, 3212);;

    private static final String COOK = "Cook";

    private static final Area DAIRY_COWS = Area.polygonal(
            new Position(3249, 3278, 0),
            new Position(3257, 3278, 0),
            new Position(3257, 3270, 0),
            new Position(3253, 3270, 0),
            new Position(3253, 3272, 0),
            new Position(3251, 3274, 0),
            new Position(3251, 3276, 0)
    );

    private static final String DAIRY_COW = "Dairy cow";

    private static final Area BARN = Area.polygonal(
            new Position(3225, 3295, 0),
            new Position(3231, 3295, 0),
            new Position(3231, 3287, 0),
            new Position(3237, 3287, 0),
            new Position(3237, 3290, 0),
            new Position(3238, 3291, 0),
            new Position(3238, 3293, 0),
            new Position(3237, 3294, 0),
            new Position(3237, 3297, 0),
            new Position(3238, 3298, 0),
            new Position(3238, 3300, 0),
            new Position(3236, 3302, 0),
            new Position(3226, 3302, 0),
            new Position(3225, 3301, 0)
    );

    private static final Area WHEAT_FIELD = Area.polygonal(
            new Position(3165, 3299, 0),
            new Position(3164, 3296, 0),
            new Position(3164, 3293, 0),
            new Position(3165, 3292, 0),
            new Position(3165, 3291, 0),
            new Position(3164, 3290, 0),
            new Position(3162, 3290, 0),
            new Position(3157, 3295, 0),
            new Position(3157, 3299, 0)
    );

    private static final Area MILL_TOP_FLOOR = Area.polygonal(
            new Position(3168, 3310, 2),
            new Position(3170, 3308, 2),
            new Position(3170, 3306, 2),
            new Position(3168, 3304, 2),
            new Position(3166, 3304, 2),
            new Position(3164, 3306, 2),
            new Position(3164, 3308, 2),
            new Position(3166, 3310, 2)
    );

    public static final Area MILL_GROUND_FLOOR = Area.polygonal(
            new Position(3168, 3311, 0),
            new Position(3171, 3308, 0),
            new Position(3171, 3306, 0),
            new Position(3168, 3303, 0),
            new Position(3166, 3303, 0),
            new Position(3163, 3306, 0),
            new Position(3163, 3308, 0),
            new Position(3166, 3311, 0)
    );

    private static final List<UnaryNode> NODES = List.of(
            CommonNodes.walkToNpc(COOK, KITCHEN),
            AnonUnaryNode.builder(new DialogAction(COOK, 1, 1, 4))
                    .condition(Condition.isQuestStarted(Quest.COOKS_ASSISTANT))
                    .usePrecondition()
                    .status("Talking to " + COOK)
                    .build(),
            AnonUnaryNode.builder(Action.pickup(ItemId.POT))
                    .condition(Condition.hasItem(ItemId.POT))
                    .usePrecondition()
                    .reset(Condition.isMoving())
                    .status("Picking up pot")
                    .build(),
            CommonNodes.useTrapdoor(false, -1),
            CommonNodes.walkToPickableLocal(ItemId.BUCKET, 7),
            CommonNodes.pickupItem(ItemId.BUCKET),
            CommonNodes.useLadder(true, 0),
            CommonNodes.walkToObject("Dairy cow", DAIRY_COWS),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.BUCKET, () -> SceneObjects.getNearest(DAIRY_COW)))
                    .condition(Condition.hasItem(ItemId.BUCKET_OF_MILK))
                    .reset(Condition.isAnimating())
                    .usePrecondition()
                    .wait(5000)
                    .status("Milking the cow")
                    .build(),
            CommonNodes.walkTo(BARN),
            CommonNodes.pickupItem(ItemId.EGG),
            CommonNodes.walkTo(WHEAT_FIELD),
            AnonUnaryNode.builder(Action.interactWithObject("Wheat", "Pick"))
                    .condition(Condition.hasItem(ItemId.GRAIN))
                    .reset(Condition.isMoving())
                    .usePrecondition()
                    .status("Picking wheat")
                    .build(),
            CommonNodes.walkTo(MILL_TOP_FLOOR),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.GRAIN, () -> SceneObjects.getNearest("Hopper")))
                    .condition(Condition.doesntHaveItem(ItemId.GRAIN))
                    .reset(Condition.isMoving())
                    .usePrecondition()
                    .status("Using grain on hopper")
                    .build(),
            AnonUnaryNode.builder(Action.interactWithObject("Hopper controls", "Operate"))
                    .condition(Condition.varbitHasValue(4920, 1))
                    .reset(Condition.isMoving())
                    .usePrecondition()
                    .build(),
            CommonNodes.walkTo(MILL_GROUND_FLOOR),
            AnonUnaryNode.builder(Action.interactWithObject("Flour bin", "Empty"))
                    .condition(Condition.hasItem(ItemId.POT_OF_FLOUR))
                    .usePrecondition()
                    .timeout(5000)
                    .status("Emptying the flour bin")
                    .build(),
            CommonNodes.walkToNpc(COOK, KITCHEN),
            AnonUnaryNode.builder(new DialogAction(COOK))
                    .condition(Condition.isQuestDone(Quest.COOKS_ASSISTANT))
                    .usePrecondition()
                    .status("Talking to " + COOK)
                    .build()
    );

    public CooksAssistantQuestBuilder() {
        super(NODES);
    }


}
