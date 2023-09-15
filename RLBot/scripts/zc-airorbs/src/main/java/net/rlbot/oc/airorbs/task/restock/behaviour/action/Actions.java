package net.rlbot.oc.airorbs.task.restock.behaviour.action;

import net.rlbot.api.movement.Movement;
import net.rlbot.oc.airorbs.task.restock.data.Constants;
import net.rlbot.script.api.tree.decisiontree.Action;

public class Actions {

    public static Action goToGrandExchange(){
        return c -> Movement.walkTo(Constants.GRAND_EXCHANGE);
    }

}
