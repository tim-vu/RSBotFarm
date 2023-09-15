package net.rlbot.script.api.task.common.basicactivitytask.behaviour.decision;

import net.rlbot.api.common.BankLocation;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class Decisions {
    public static Decision hasLoadout() {
        return b -> b.get(BasicActivityKeys.HAS_LOADOUT).get();
    }

    public static Decision equipItems() {
        return b -> !LoadoutManager.isLoadoutSetup(b.get(BasicActivityKeys.LOADOUT));
    }

    public static Decision isAtGrandExchange(){
        return b -> Areas.GRAND_EXCHANGE.contains(Players.getLocal());
    }

    public static Decision isAtTrainingArea(){
        return b -> {
            var trainingArea = b.get(BasicActivityKeys.TRAINING_AREA);
            return trainingArea == null || trainingArea.contains(Players.getLocal());
        };
    }

    public static Decision isRestocking(){
        return b -> b.get(RestockingKeys.IS_RESTOCKING);
    }

    public static Decision isAtBank() {
        return b -> BankLocation.getNearest().getArea().contains(Players.getLocal());
    }
}
