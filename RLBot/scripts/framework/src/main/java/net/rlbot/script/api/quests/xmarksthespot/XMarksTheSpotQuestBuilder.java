package net.rlbot.script.api.quests.xmarksthespot;

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

public class XMarksTheSpotQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.X_MARKS_THE_SPOT;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.SPADE).build()
            .build();

    private static final Area LUMBRIDGE_PUB = Area.polygonal(
            new Position(3226, 3243, 0),
            new Position(3234, 3243, 0),
            new Position(3234, 3236, 0),
            new Position(3227, 3236, 0),
            new Position(3227, 3239, 0),
            new Position(3226, 3239, 0)
    );

    private static final String VEOS = "Veos";

    private static final Position DIG_SPOT1 = new Position(3230, 3209, 0);

    private static final Position DIG_SPOT2 = new Position(3203, 3212, 0);

    private static final Position DIG_SPOT3 = new Position(3109, 3264, 0);

    private static final Position DIG_SPOT4 = new Position(3078, 3259, 0);

    private static final Area PORT_SARIM = Area.rectangular(3045, 3252, 3055, 3245);

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(VEOS, LUMBRIDGE_PUB),
            AnonUnaryNode.builder(new DialogAction(VEOS, 1, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to Veos")
                    .build(),
            CommonNodes.walkTo(DIG_SPOT1, "the dig spot"),
            createDigNode(ItemId.TREASURE_SCROLL_23068),
            CommonNodes.walkTo(DIG_SPOT2, "the second dig spot"),
            createDigNode(ItemId.MYSTERIOUS_ORB_23069),
            CommonNodes.walkTo(DIG_SPOT3, "the third dig spot"),
            createDigNode(ItemId.TREASURE_SCROLL_23070),
            CommonNodes.walkTo(DIG_SPOT4, "the fourth dig spot"),
            createDigNode(ItemId.ANCIENT_CASKET),
            CommonNodes.walkToNpc(VEOS, PORT_SARIM),
            AnonUnaryNode.sequence(
                    AnonUnaryNode.builder(Action.useItemOn(ItemId.ANCIENT_CASKET, () -> Npcs.getNearest(VEOS)))
                            .condition(Dialog::isOpen)
                            .usePrecondition()
                            .status("Using the ancient casket on " + VEOS)
                            .build(),
                    AnonUnaryNode.builder(new DialogAction())
                            .condition(Condition.isQuestDone(QUEST))
                            .usePrecondition()
                            .status("Talking to " + VEOS)
                            .build()
            )
    );

    private static UnaryNode createDigNode(int itemId) {
        return AnonUnaryNode.builder(Action.interactWithItem(ItemId.SPADE, "Dig"))
                .condition(Condition.hasItem(itemId))
                .usePrecondition()
                .status("Digging")
                .build();
    }
    public XMarksTheSpotQuestBuilder() {
        super(NODES);
    }
}
