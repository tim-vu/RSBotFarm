package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.offer.OfferResult;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;

import java.util.Set;

@Slf4j
public class CreateBuyOfferAction implements Action {

    @Override
    public void execute(Blackboard blackboard) {

        if (!GrandExchange.isOpen())
        {
            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        blackboard.put(RestockingKeys.IS_BUYING, true);

        Set<Tradeable> tradeables = blackboard.get(RestockingKeys.SETTINGS).getTradeables();
        var toBuy = tradeables.stream().filter(t -> t.needsBuying() && !t.offerExists()).findFirst();

        if(toBuy.isEmpty()) {
            blackboard.put(RestockingKeys.IS_BUYING, false);
            log.warn("Nothing to buy when trying to buy item");
            return;
        }

        log.info("Creating buy offer");
        var result = GrandExchangeEx.createBuyOffer(toBuy.get());
        if (result == OfferResult.NOT_ENOUGH_COINS) {
            log.warn("Not enough coins");
            blackboard.put(RestockingKeys.IS_OUT_OF_COINS, true);
            return;
        }

        if(result == OfferResult.SUCCESS) {
            blackboard.put(RestockingKeys.IS_BUYING, false);
        }

    }
}
