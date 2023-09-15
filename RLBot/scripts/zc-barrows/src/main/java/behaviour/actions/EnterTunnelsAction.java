package behaviour.actions;

import data.Areas;
import data.Keys;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.common.QuestDialog;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class EnterTunnelsAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Entering tunnels";
    }

    @Override
    public void execute() {

        if (!Dialog.isOpen()) {

            var sarcophagus = SceneObjects.getNearest("Sarcophagus");

            if (sarcophagus == null) {
                log.warn("Unable to find the sarcophagus");
                Time.sleepTick();
                return;
            }

            Action.logPerform("OPEN_SARCOPHAGUS");
            if (!sarcophagus.interact("Search")) {
                log.warn("Failed to open the sarcophagus");
                Time.sleepTick();
                return;
            }

            if(!Time.sleepUntil(Dialog::isOpen, 4000)) {
                log.warn("Opening the sarcophagus timed out");
                Time.sleepTick();
                return;
            }
        }


        Action.logPerform("CONTINUE_DIALOG");
        if(!QuestDialog.doContinue()) {
            Action.logFail("CONTINUE_DIALOG");
            Time.sleepTick();
            return;
        }

        Action.logPerform("ENTER_TUNNELS");
        if(!Dialog.chooseOption(1)) {
            Action.logFail("ENTER_TUNNELS");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Areas.TUNNELS.contains(Players.getLocal()), 1800)) {
            Action.logTimeout("ENTER_TUNNELS");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
