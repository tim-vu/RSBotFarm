package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;

@Slf4j
public class AbortOfferAction implements Action {
    @Override
    public void execute(Blackboard context) {

        if (!GrandExchange.isOpen())
        {
            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
            }

            Reaction.REGULAR.sleep();
            return;
        }

        var tradeables = context.get(RestockingKeys.SETTINGS).getTradeables();
        var toAbort = tradeables.stream()
                .filter(Tradeable::offerExists)
                .filter(t -> t.getOfferTracker() != null)
                .filter(t -> t.getOfferTracker().shouldAbort())
                .findFirst();

        if(toAbort.isEmpty()) {
            log.debug("No offer to abort");
            return;
        }

        if (!GrandExchangeEx.abortOffer(toAbort.get())) {
            log.warn("Failed to abort offer: " + toAbort.get());
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
