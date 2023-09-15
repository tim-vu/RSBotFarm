package net.rlbot.script.api.quests.sheepshearer;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.condition.Condition;
import net.rlbot.script.api.quests.sheepshearer.nodes.ShearSheepNode;
import net.rlbot.script.api.quests.sheepshearer.nodes.SpinWoolNode;

import java.util.List;

public class SheepShearerQuestBuilder extends UnaryNodeQuestBuilder {

    private static final Area FREDS_FARM = Area.polygonal(
            new Position(3185, 3280, 0),
            new Position(3192, 3280, 0),
            new Position(3193, 3279, 0),
            new Position(3193, 3270, 0),
            new Position(3184, 3270, 0),
            new Position(3184, 3279, 0)
    );

    private static final String FRED = "Fred the Farmer";

    private static final Area SHEEP_FIELD = Area.polygonal(
            new Position(3193, 3277, 0),
            new Position(3205, 3277, 0),
            new Position(3206, 3276, 0),
            new Position(3210, 3276, 0),
            new Position(3212, 3274, 0),
            new Position(3212, 3270, 0),
            new Position(3213, 3269, 0),
            new Position(3213, 3257, 0),
            new Position(3194, 3257, 0),
            new Position(3193, 3258, 0),
            new Position(3193, 3259, 0),
            new Position(3192, 3260, 0),
            new Position(3192, 3261, 0),
            new Position(3193, 3262, 0)
    );

    public static final Area PERSIAN_BEDROOM = Area.rectangular(3208, 3217, 3213, 3212, 1);

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(Loadout.empty(), false),
            CommonNodes.walkToNpc(FRED, FREDS_FARM),
            AnonUnaryNode.builder(new DialogAction(FRED, 1, 1))
                    .condition(Condition.isQuestStarted(Quest.SHEEP_SHEARER))
                    .usePrecondition()
                    .status("Talking to " + FRED)
                    .build(),
            CommonNodes.walkTo(SHEEP_FIELD),
            new ShearSheepNode(),
            CommonNodes.walkTo(PERSIAN_BEDROOM),
            new SpinWoolNode(),
            CommonNodes.walkToNpc(FRED, FREDS_FARM),
            AnonUnaryNode.builder(new DialogAction(FRED, 1))
                    .condition(Condition.isQuestDone(Quest.SHEEP_SHEARER))
                    .usePrecondition()
                    .status("Talking to " + FRED)
                    .build()
    );

    public SheepShearerQuestBuilder() {
        super(NODES);
    }
}
