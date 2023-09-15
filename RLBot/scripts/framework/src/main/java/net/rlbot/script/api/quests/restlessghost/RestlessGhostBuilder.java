package net.rlbot.script.api.quests.restlessghost;

import net.rlbot.api.Game;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RestlessGhostBuilder extends UnaryNodeQuestBuilder {

    public static final Quest QUEST = Quest.THE_RESTLESS_GHOST;

    private static final String AERECK = "Father Aereck";
    private static final String UHRNEY = "Father Urhney";
    private static final String GHOST = "Restless ghost";

    private static final Area CHURCH = Area.rectangular(3240, 3215, 3247, 3204);
    private static final Area SWAMP_HUT = Area.rectangular(3144, 3177, 3151, 3173);
    private static final Area COFFIN_BUILDING = Area.rectangular(3247, 3195, 3252, 3190);
    private static final Area ALTAR = Area.rectangular(3116, 9569, 3121, 9564);

    public RestlessGhostBuilder() {
        super(QUESTNODES);
    }

    private static final List<UnaryNode> QUESTNODES = List.of(
            CommonNodes.walkToNpc(AERECK, CHURCH),
            AnonUnaryNode.builder(new DialogAction(AERECK, 3, 1))
                    .condition(Condition.isQuestStarted(QUEST))
                    .usePrecondition()
                    .status("Talking to " + AERECK).build(),
            CommonNodes.walkToNpc(UHRNEY, SWAMP_HUT),
            AnonUnaryNode.builder(new DialogAction(UHRNEY, 2, 1))
                    .condition(Condition.hasItem(ItemId.GHOSTSPEAK_AMULET))
                    .usePrecondition()
                    .status("Talking to " + UHRNEY).build(),
            AnonUnaryNode.builder(Action.interactWithItem(ItemId.GHOSTSPEAK_AMULET, "Wear"))
                    .condition(Condition.isWearing(ItemId.GHOSTSPEAK_AMULET))
                    .usePrecondition()
                    .status("Wearing ghostspeak amulet").build(),
            CommonNodes.walkTo(COFFIN_BUILDING, "the coffin"),
            createOpenCoffinAction(),
            CommonNodes.wait(1000),
            AnonUnaryNode.builder(new DialogAction(GHOST, 1))
                    .condition(Condition.isAtStage(QUEST, 3))
                    .usePrecondition()
                    .status("Talking to " + GHOST).build(),
            CommonNodes.walkTo(ALTAR, "the altar"),
            AnonUnaryNode.builder(Action.interactWithObject("Altar", "Search"))
                    .condition(Condition.hasItem(ItemId.GHOSTS_SKULL))
                    .usePrecondition()
                    .timeout(5000)
                    .status("Searching the altar").build(),
            CommonNodes.walkTo(COFFIN_BUILDING, "the coffin"),
            createOpenCoffinAction(),
            CommonNodes.wait(1000),
            AnonUnaryNode.builder(Action.useItemOn(ItemId.GHOSTS_SKULL, () -> SceneObjects.getNearest("Coffin")))
                    .condition(Game::isInCutscene)
                    .timeout(6000)
                    .status("Using the skull").build(),
            CommonNodes.watchCutScene(Condition.isQuestDone(QUEST))
    );

    private static UnaryNode createOpenCoffinAction(){
        return AnonUnaryNode.builder(Action.interactWithObject("Coffin", "Open"))
                .condition(Condition.isNpcPresent("Restless ghost"))
                .usePrecondition(Condition.objectHasFirstAction("Coffin", "Close"))
                .timeout(6000)
                .status("Opening the coffin").build();
    }


}
