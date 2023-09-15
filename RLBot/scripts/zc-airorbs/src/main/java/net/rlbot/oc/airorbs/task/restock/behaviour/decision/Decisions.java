package net.rlbot.oc.airorbs.task.restock.behaviour.decision;

import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class Decisions {

    public static Decision isRestocking() {
        return c-> c.get(RestockingKeys.IS_RESTOCKING);
    }

}
