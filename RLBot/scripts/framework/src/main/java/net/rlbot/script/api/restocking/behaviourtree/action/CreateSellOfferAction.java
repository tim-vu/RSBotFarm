package net.rlbot.script.api.restocking.behaviourtree.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.restocking.GrandExchangeEx;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.behaviourtree.ActionNodeBase;
import net.rlbot.script.api.tree.behaviourtree.Result;

@Slf4j
public class CreateSellOfferAction extends ActionNodeBase {
    @Override
    public Result tick() {

        log.debug("Creating sell offer");

        if (!GrandExchange.isOpen())
        {
            log.debug("Opening GrandExchange");

            if(!GrandExchange.open()) {
                log.warn("Failed to open the GrandExchange");
                Time.sleepTick();
                return Result.IN_PROGRESS;
            }

            Reaction.REGULAR.sleep();
            return Result.IN_PROGRESS;
        }

        var toSell = getBlackboard().get(RestockingKeys.SETTINGS).getTradeables().stream()
                .filter(Tradeable::needsSelling)
                .filter(t -> Inventory.contains(t.getItemId(), ItemDefinition.getNotedId(t.getItemId())))
                .findFirst();

        if(toSell.isEmpty()) {
            log.debug("No item to sell");
            return Result.SUCCESS;
        }

        GrandExchangeEx.createSellOffer(toSell.get());
        return Result.IN_PROGRESS;
    }
}
