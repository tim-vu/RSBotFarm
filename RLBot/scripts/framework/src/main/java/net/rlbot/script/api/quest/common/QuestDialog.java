package net.rlbot.script.api.quest.common;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.Dialog;

@Slf4j
public class QuestDialog {

    public static boolean doContinue() {

        while(true) {

            if(!Dialog.isOpen() && Game.isInCutscene()) {
                Time.sleepTick();
                continue;
            }

            if(!Dialog.isOpen()) {
                return true;
            }

            if(!Dialog.canContinue() && !Dialog.isViewingOptions()) {
                Time.sleepTick();
                continue;
            }

            if(!Dialog.canContinue()) {
                return true;
            }

            if(!Dialog.continueSpace()) {
                return false;
            }
        }
    }
}
