package net.rlbot.script.api.quests.misthalinmystery.nodes;

import net.rlbot.api.Game;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

public class WatchKillCutscene extends UnaryNode {

    public WatchKillCutscene() {
        super("Watching cutscene");
    }

    @Override
    protected ActionResult doExecute() {

        if(Dialog.canContinue()) {
            Dialog.continueSpace();
            Time.sleepTick();
            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(Dialog.isViewingOptions()) {
            Dialog.chooseOption(1);
            Time.sleepTick();
            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        if(Game.isInCutscene()) {

            if(!Time.sleepUntil(() -> !Game.isInCutscene() || Dialog.canContinue(), 2000)) {
                return ActionResult.FAILURE;
            }

            Reaction.REGULAR.sleep();
            return ActionResult.IN_PROGRESS;
        }

        return Quests.getStage(Quest.MISTHALIN_MYSTERY) == 70 ? ActionResult.SUCCESS : ActionResult.IN_PROGRESS;
    }
}
