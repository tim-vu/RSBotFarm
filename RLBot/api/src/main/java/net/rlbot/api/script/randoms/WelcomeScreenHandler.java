package net.rlbot.api.script.randoms;

import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.internal.menu.Menu;
import net.runelite.api.MenuAction;

public class WelcomeScreenHandler extends RandomEventHandler {

    public static boolean shouldActive() {
        return Widgets.isVisible(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN);
    }

    @Override
    public boolean shouldActivate() {
        return shouldActive();
    }

    @Override
    public int loop() {

        if(!Menu.invokeWidgetDefaultMenuAction(1, -1, 24772680, -1) || !Time.sleepUntil(() -> !shouldActivate(), 2000)) {
            return 1000;
        }

        return 500;
    }

}
