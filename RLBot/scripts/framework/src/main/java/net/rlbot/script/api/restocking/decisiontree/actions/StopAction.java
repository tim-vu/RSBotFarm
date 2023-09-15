package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;

@Slf4j
public class StopAction implements Action {

    @Override
    public void execute(Blackboard context) {

        if(GrandExchange.isOpen()) {

            log.debug("Closing Grand Exchange");
            if(!GrandExchangeEx.close()) {
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        context.put(RestockingKeys.IS_RESTOCKING, false);
    }
}
