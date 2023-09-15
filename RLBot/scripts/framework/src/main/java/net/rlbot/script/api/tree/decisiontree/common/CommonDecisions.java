package net.rlbot.script.api.tree.decisiontree.common;

import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class CommonDecisions {

    public static Decision isAtGrandExchange() {
        return c -> Areas.GRAND_EXCHANGE.contains(Players.getLocal());
    }

}
