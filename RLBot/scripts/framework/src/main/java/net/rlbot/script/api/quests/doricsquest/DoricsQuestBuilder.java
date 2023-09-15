package net.rlbot.script.api.quests.doricsquest;

import net.rlbot.api.movement.position.Area;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.quest.UnaryNodeQuestBuilder;
import net.rlbot.script.api.quest.nodes.AnonUnaryNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.action.DialogAction;
import net.rlbot.script.api.quest.nodes.common.CommonNodes;
import net.rlbot.script.api.quest.nodes.common.SetupLoadout;
import net.rlbot.script.api.quest.nodes.condition.Condition;

import java.util.List;

public class DoricsQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.DORICS_QUEST;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.CLAY).amount(6).build()
                .withItem(ItemId.IRON_ORE).amount(2).build()
                .withItem(ItemId.COPPER_ORE).amount(4).build()
            .build();

    public static final Area DORICS_HUT = Area.rectangular(2950, 3454, 2953, 3449);

    public static final String DORIC = "Doric";

    public static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(DORIC, DORICS_HUT),
            AnonUnaryNode.builder(new DialogAction(DORIC, 1, 1))
                    .condition(Condition.isQuestDone(QUEST))
                    .usePrecondition()
                    .status("Talking to " + DORIC)
                    .build()
    );

    public DoricsQuestBuilder() {
        super(NODES);
    }
}
