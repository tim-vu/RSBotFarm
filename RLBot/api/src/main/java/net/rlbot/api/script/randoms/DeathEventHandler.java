package net.rlbot.api.script.randoms;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;

@Slf4j
public class DeathEventHandler extends RandomEventHandler {

    @Override
    public boolean shouldActivate() {
        return Game.isInInstancedRegion() && Npcs.getNearest("Death") != null;
    }

    @Override
    public int loop() {

        if(!Dialog.isOpen()) {

            var death = Npcs.getNearest("Death");

            if(death == null) {
                log.warn("Unable to find Death");
                return 1000;
            }

            if(!death.interact("Talk-to") || !Time.sleepUntil(Dialog::isOpen, () -> Players.getLocal().isMoving(), 1200)) {
                log.warn("Unable to talk to Death");
                return 1000;
            }

            return Random.between(1000, 2000);
        }

        if(Dialog.canContinue()) {
            Dialog.continueSpace();
            return Random.between(300, 1000);
        }

        if(Dialog.isViewingOptions()) {

            var completedDialogs = Dialog.getOptions().stream()
                    .filter(o -> o.contains("<str>"))
                    .toList();

            var options = Dialog.getOptions();

            for(var i = 0; i < options.size(); i++) {

                var text = options.get(i);

                if(text.contains("<str>")) {
                    continue;
                }

                Dialog.chooseOption(i);
                return 1000;
            }

            var portal = SceneObjects.getNearest("Portal");

            if (portal == null) {
                log.warn("Unable to find portal");
                return 1000;
            }

            if(!portal.interact("Use") || !Time.sleepUntil(() -> !Game.isInInstancedRegion(), () -> Players.getLocal().isMoving(), 2400)) {
                log.warn("Failed to use portal");
                return 1000;
            }

            return 1000;
        }

        return 1000;

    }
}
