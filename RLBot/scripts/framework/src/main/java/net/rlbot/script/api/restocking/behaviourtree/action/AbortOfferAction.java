package net.rlbot.script.api.restocking.behaviourtree.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.behaviourtree.ActionNodeBase;
import net.rlbot.script.api.tree.behaviourtree.Result;

@Slf4j
public class AbortOfferAction extends ActionNodeBase {

    @Override
    public Result tick() {

        log.debug("Aborting offer");

        if (!GrandExchange.isOpen())
        {
            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        var tradeables = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables();
        var toAbort = tradeables.stream()
                .filter(Tradeable::isOfferInProgress)
                .filter(t -> t.getOfferTracker() != null)
                .filter(t -> t.getOfferTracker().shouldAbort())
                .findFirst();

        if(toAbort.isEmpty()) {
            log.debug("No offer to abort");
            return Result.SUCCESS;
        }

        if (!GrandExchangeEx.abortOffer(toAbort.get())) {
            log.warn("Failed to abort offer: " + toAbort.get());
            Time.sleepTick();
            return Result.IN_PROGRESS;
        }

        Reaction.REGULAR.sleep();
        return Result.SUCCESS;
    }
}
