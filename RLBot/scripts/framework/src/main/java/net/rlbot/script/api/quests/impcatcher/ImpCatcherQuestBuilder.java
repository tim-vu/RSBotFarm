package net.rlbot.script.api.quests.impcatcher;

import net.rlbot.api.Game;
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

public class ImpCatcherQuestBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.IMP_CATCHER;

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.RED_BEAD).build()
                .withItem(ItemId.YELLOW_BEAD).build()
                .withItem(ItemId.BLACK_BEAD).build()
                .withItem(ItemId.WHITE_BEAD).build()
            .build();

    private static final Area WIZARD_TOWER = Area.rectangular(3101, 3166, 3114, 3154, 2);

    private static final String MIZGOG = "Wizard Mizgog";

    private static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(MIZGOG, WIZARD_TOWER),

            //TODO: Verify
            AnonUnaryNode.builder(new DialogAction(MIZGOG, 1, 1))
                    .condition(Game::isInCutscene)
                    .usePrecondition()
                    .status("Talking to " + MIZGOG)
                    .build(),
            CommonNodes.watchCutScene(Condition.isQuestDone(QUEST))
    );

    public ImpCatcherQuestBuilder() {
        super(NODES);
    }
}
