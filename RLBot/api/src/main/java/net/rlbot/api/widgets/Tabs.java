package net.rlbot.api.widgets;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.Game;
import net.rlbot.api.GameState;
import net.rlbot.api.game.Vars;
import net.runelite.api.VarClientInt;

public class Tabs {

    private static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static void open(@NonNull Tab tab) {
        if(Game.getState() != GameState.LOGGED_IN) {
            return;
        }

        API_CONTEXT.getClient().runScript(915, tab.getIndex());
    }

    public static boolean isOpen(@NonNull Tab tab) {
        return Vars.getVarcInt(VarClientInt.INVENTORY_TAB) == tab.getIndex();
    }

}
