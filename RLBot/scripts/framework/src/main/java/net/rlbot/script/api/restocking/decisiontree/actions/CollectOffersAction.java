package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.GrandExchangeSetup;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;

import java.util.Set;

@Slf4j
public class CollectOffersAction implements Action {
    @Override
    public void execute(Blackboard context) {

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

        if(GrandExchange.getView() != GrandExchange.View.OFFERS) {

            if(!GrandExchangeSetup.goBack()) {
                log.warn("Failed to go back");
                Time.sleepTick();
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        Set<Tradeable> tradeables = context.get(RestockingKeys.SETTINGS).getTradeables();
        if(!GrandExchangeEx.collectFinishedOffers(tradeables)) {
            log.warn("Failed to collect offers");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
