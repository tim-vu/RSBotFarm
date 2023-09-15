package net.rlbot.script.api.component;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.script.api.reaction.Reaction;

@Slf4j
public class DialogEx {

    private static final WidgetAddress NPC_DIALOG_MESSAGE = new WidgetAddress(231, 4);

    public static boolean continueDialog() {

        while (Dialog.canContinue()) {

            //TODO: Verify if this works
            var text = Dialog.getText();

            Dialog.continueSpace();
            if (!Time.sleepUntil(() -> !text.equals(Dialog.getText()) || !Dialog.isOpen(), 3000)) {
                log.info("Continue dialogue failed");
                return false;
            }

            Time.sleepUntil(() -> Dialog.canContinue() || !Dialog.isOpen() || Dialog.isViewingOptions(), 25000);
            Reaction.REGULAR.sleep();
        }

        return true;
    }

    public static boolean processResponse(int responseIndex) {

        //TODO: Verify if this works
        var text = Dialog.getText();
        if (!Dialog.chooseOption(responseIndex) || !Time.sleepUntil(() -> !text.equals(Dialog.getText()), 3000))
            return false;

        Reaction.PREDICTABLE.sleep();
        return true;
    }

}
