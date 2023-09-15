package net.rlbot.script.api.restocking.behaviourtree.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.GrandExchangeSetup;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.behaviourtree.ActionNodeBase;
import net.rlbot.script.api.tree.behaviourtree.Result;

import java.util.Set;

@Slf4j
public class CollectOffersAction extends ActionNodeBase {
    @Override
    public Result tick() {

        log.debug("Collecting offers");

        if (!GrandExchange.isOpen())
        {
            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
                return Result.IN_PROGRESS;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        if(GrandExchange.getView() != GrandExchange.View.OFFERS) {

            if(!GrandExchangeSetup.goBack()) {
                log.warn("Failed to go back");
                Time.sleepTick();
                return Result.IN_PROGRESS;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        Set<Tradeable> tradeables = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables();
        if(!GrandExchangeEx.collectFinishedOffers(tradeables)) {
            log.warn("Failed to collect offers");
            Time.sleepTick();
            return Result.IN_PROGRESS;
        }

        Reaction.REGULAR.sleep();
        return Result.SUCCESS;
    }
}
