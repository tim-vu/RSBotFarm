package net.rlbot.script.api.quests.romeoandjuliet;

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

public class RomeoAndJulietQuestBuilder extends UnaryNodeQuestBuilder {

    private static final Loadout LOADOUT = Loadout.builder()
                .withItem(ItemId.CADAVA_BERRIES).build()
            .build();

    public static final Quest QUEST = Quest.ROMEO__JULIET;

    private static final Area HOUSE_GROUNDFLOOR = Area.rectangular(3156, 3436, 3164, 3432);
    private static final Area JULIETS_ROOM = Area.rectangular(3153, 3436, 3164, 3425, 1);

    private static final Area VARROCK = Area.rectangular(3205, 3438, 3222, 3421);
    private static final Area ROMEO_AREA = Area.rectangular(3205, 3438, 3221, 3410);
    private static final Area CHURCH = Area.rectangular(3252, 3488, 3259, 3471);
    private static final Area APOTHECARY_AREA = Area.rectangular(3192, 3406, 3198, 3402);

    private static final String JULIET = "Juliet";
    private static final String ROMEO = "Romeo";
    private static final String LAWRENCE = "Father Lawrence";
    private static final String APOTHECARY = "Apothecary";

    public static final List<UnaryNode> NODES = List.of(
            new SetupLoadout(LOADOUT),
            CommonNodes.walkToNpc(ROMEO, VARROCK),
            AnonUnaryNode.builder(new DialogAction(ROMEO,  3, 1, 3))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to " + ROMEO)
                    .build(),
            CommonNodes.walkToNpc(JULIET, JULIETS_ROOM),
            AnonUnaryNode.builder(new DialogAction(JULIET))
                .condition(Condition.isAtStage(QUEST, 20))
                .usePrecondition()
                .status("Talking to Juliet").build(),
            CommonNodes.walkToNpc(ROMEO, ROMEO_AREA),
            AnonUnaryNode.builder(new DialogAction(ROMEO, 4))
                .condition(Condition.isAtStage(QUEST, 30))
                .usePrecondition()
                .status("Talking to Romeo").build(),
            CommonNodes.walkToNpc(LAWRENCE, CHURCH),
            AnonUnaryNode.builder(new DialogAction(LAWRENCE))
                .condition(Condition.isAtStage(QUEST, 40))
                .usePrecondition()
                .status("Talking to Father Lawrence").build(),
            CommonNodes.walkToNpc(APOTHECARY, APOTHECARY_AREA),
            AnonUnaryNode.builder(new DialogAction(APOTHECARY,2, 1))
                .condition(Condition.hasItem(ItemId.CADAVA_POTION))
                .usePrecondition()
                .status("Talking to the apothecary").build(),
            CommonNodes.walkToNpc(JULIET, JULIETS_ROOM),
            AnonUnaryNode.builder(new DialogAction(JULIET))
                .condition(Condition.isAtStage(QUEST, 60))
                .usePrecondition()
                .status("Talking to Juliet").build(),
            CommonNodes.wait(600),
            CommonNodes.walkToNpc(ROMEO, ROMEO_AREA),
            AnonUnaryNode.builder(new DialogAction(ROMEO))
                    .condition(Condition.isAtStage(QUEST, 100))
                    .usePrecondition()
                    .status("Talking to Romeo").build()
    );

    public RomeoAndJulietQuestBuilder() {
        super(NODES);
    }

}
