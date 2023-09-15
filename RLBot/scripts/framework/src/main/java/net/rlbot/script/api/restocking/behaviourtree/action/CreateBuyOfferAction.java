package net.rlbot.script.api.restocking.behaviourtree.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.offer.OfferResult;
import net.rlbot.script.api.tree.behaviourtree.ActionNodeBase;
import net.rlbot.script.api.tree.behaviourtree.Result;

import java.util.Set;

@Slf4j
public class CreateBuyOfferAction extends ActionNodeBase {

    @Override
    public Result tick() {

        log.debug("Creating buy offer");

        Set<Tradeable> tradeables = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables();
        var toBuy = tradeables.stream().filter(t -> t.needsBuying() && !t.offerExists()).findFirst();

        if(toBuy.isEmpty()) {
            log.debug("Nothing to buy");
            return Result.SUCCESS;
        }

        if (!GrandExchange.isOpen())
        {
            log.debug("Opening the Grand Exchange");
            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
                return Result.IN_PROGRESS;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        log.info("Creating buy offer");

        var result = GrandExchangeEx.createBuyOffer(toBuy.get());

        if(result == OfferResult.NOT_ENOUGH_COINS) {
            log.error("Player is out of coins");
            getBlackboard().put(RestockingKeys.IS_OUT_OF_COINS, true);
            return Result.FAILURE;
        }

        return Result.IN_PROGRESS;
    }
}
