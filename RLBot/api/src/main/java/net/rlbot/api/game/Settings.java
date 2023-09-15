package net.rlbot.api.game;

import net.rlbot.api.Game;
import net.rlbot.api.common.Time;

public class Settings {

    public static class DisableLevelUpInterface {

        public static boolean isEnabled() {
            return Vars.getBit(Varbits.DISABLE_LEVEL_UP_INTERFACE) == 1;
        }

        public static boolean toggleEnabled() {
            var enabled = isEnabled();
            Game.runScript(3847, 4493, 315, 8781842, 33, 0, 0);
            return Time.sleepUntil(() -> isEnabled() != enabled, 1000);
        }
    }

    public static class AcceptTradeDelay {

        public static boolean isEnabled() {
            return Vars.getBit(Varbits.TRADE_DELAY_ENABLED) == 0;
        }

        public static boolean toggleEnabled() {
            var enabled = isEnabled();
            Game.runScript(3847, 3761, 205, 8781842, 30, 1, 0);
            return Time.sleepUntil(() -> isEnabled() != enabled, 1000);
        }
    }

}
