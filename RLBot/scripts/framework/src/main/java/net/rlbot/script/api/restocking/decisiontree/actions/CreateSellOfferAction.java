package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;

import java.util.Set;

@Slf4j
public class CreateSellOfferAction implements Action {

    @Override
    public void execute(Blackboard context) {

        if (!GrandExchange.isOpen())
        {
            log.debug("Opening GrandExchange");

            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
            }

            Reaction.REGULAR.sleep();
            return;
        }

        Set<Tradeable> tradeables = context.get(RestockingKeys.SETTINGS).getTradeables();
        var toSell = tradeables.stream()
                .filter(Tradeable::needsSelling)
                .filter(t -> Inventory.contains(t.getItemId(), ItemDefinition.getNotedId(t.getItemId())))
                .findFirst();

        if(toSell.isEmpty()) {
            log.debug("No item to sell");
            return;
        }

        if(GrandExchangeEx.createSellOffer(toSell.get()) == ActionResult.FAILURE) {
            Time.sleepTick();
        }
    }
}
