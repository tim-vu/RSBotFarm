package net.rlbot.script.api.quests.runemysteries;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class RuneMysteriesQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.RUNE_MYSTERIES;

    private static final Area LUMBRIDGE_CASTLE = Area.rectangular(3208, 3225, 3213, 3218, 1);

    private static final String DUKE = "Duke Horacio";

    private static final Area WIZARD_TOWER_BASEMENT = Area.rectangular(3096, 9574, 3107, 9566);

    private static final String SEDRIDOR = "Archmage Sedridor";

    private static final Area RUNE_SHOP = Area.polygonal(
            new Position(3252, 3405, 0),
            new Position(3254, 3405, 0),
            new Position(3256, 3403, 0),
            new Position(3256, 3401, 0),
            new Position(3254, 3399, 0),
            new Position(3252, 3399, 0),
            new Position(3250, 3401, 0),
            new Position(3250, 3403, 0),
            new Position(3252, 3403, 0)
    );

    private static final String AUBURY = "Aubury";

    private static final List<UnaryNode> NODES = List.of(
            //CommonNodes.walkToNpc(DUKE, LUMBRIDGE_CASTLE),
            //AnonUnaryNode.builder(new DialogAction(DUKE, 1, 1))
            //        .condition(Condition.isQuestStarted(QUEST))
            //        .usePrecondition()
            //        .status("Talking to " + DUKE)
            //        .build(),
            //CommonNodes.walkToNpc(SEDRIDOR, WIZARD_TOWER_BASEMENT),
            //AnonUnaryNode.builder(new DialogAction(SEDRIDOR, 1, 1, 1))
            //        .condition(Condition.hasItem(ItemId.RESEARCH_PACKAGE))
            //        .usePrecondition()
            //        .status("Talking to " + SEDRIDOR)
            //        .build(),
            //CommonNodes.walkToNpc(AUBURY, RUNE_SHOP),
            AnonUnaryNode.builder(new DialogAction(AUBURY, 2))
                    .condition(Condition.doesntHaveItem(ItemId.RESEARCH_PACKAGE))
                    .usePrecondition()
                    .status("Talking to " + AUBURY)
                    .build(),
            CommonNodes.walkToNpc(SEDRIDOR, WIZARD_TOWER_BASEMENT),
            AnonUnaryNode.builder(new DialogAction(SEDRIDOR))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .status("Talking to " + SEDRIDOR)
                    .build()
    );

    public RuneMysteriesQuestBuilder() {
        super(NODES);
    }
}
