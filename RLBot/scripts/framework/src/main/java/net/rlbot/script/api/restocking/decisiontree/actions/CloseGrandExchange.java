package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.GrandExchange;
import net.rlbot.script.api.tree.behaviourtree.ActionNodeBase;
import net.rlbot.script.api.tree.behaviourtree.Result;

@Slf4j
public class CloseGrandExchange extends ActionNodeBase {
    @Override
    public Result tick() {

        if(GrandExchange.isOpen()) {

            log.debug("Closing the Grand Exchange");
            if(!GrandExchange.close()) {
                log.debug("Failed to close the Grand Exchange");
                Time.sleepTick();
                return Result.FAILURE;
            }

            return Result.SUCCESS;
        }

        return Result.SUCCESS;
    }
}
