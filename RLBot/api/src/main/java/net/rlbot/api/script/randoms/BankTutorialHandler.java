package net.rlbot.api.script.randoms;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.widgets.WidgetAddress;

@Slf4j
public class BankTutorialHandler extends RandomEventHandler{

    private static final WidgetAddress BANK_TUTORIAL = new WidgetAddress(664, 8);
    private static final WidgetAddress BANK_TUTORIAL_CLOSE_BUTTON = new WidgetAddress(664, 29, 0);

    @Override
    public boolean shouldActivate() {
        return BANK_TUTORIAL.isWidgetVisible();
    }

    @Override
    public int loop() {

        var closeButton = BANK_TUTORIAL_CLOSE_BUTTON.resolve();

        if(closeButton == null) {
            log.warn("Unable to find close button for bank tutorial");
            return 1000;
        }

        if(!closeButton.interact("Close") || !Time.sleepUntil(() -> !BANK_TUTORIAL.isWidgetVisible(), 1200)) {
            log.warn("Failed to close bank tutorial");
            return 1000;
        }

        return Random.between(300, 1000);
    }
}
