package net.rlbot.script.api.quest.questexecution.behaviour;

import net.rlbot.api.common.BankLocation;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.quest.questexecution.data.Keys;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class Decisions {

    public static Decision isRestocking() {
        return context -> context.get(RestockingKeys.IS_RESTOCKING);
    }

    public static Decision isAtBank() {
        return context -> BankLocation.getNearest().getArea().contains(Players.getLocal());
    }

    public static Decision isReady() {
        return context -> context.get(Keys.IS_READY);
    }
}
