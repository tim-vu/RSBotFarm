package net.rlbot.oc.airorbs.task.airorbs.behaviour.decision;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.items.Bank;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Decision;
import net.rlbot.script.farm.api.model.CreateMulingRequest;
import net.rlbot.script.farm.api.model.MulingRequestType;
import net.rlbot.script.farm.api.model.MulingRequestVm;

@Slf4j
public class ShouldMule implements Decision {

    private static final int MINIMUM_COINS_TO_ACTIVATE_MULING = 700000;

    private static final int DELAY_BETWEEN_MULE_REQUESTS = 60000;

    private long lastCall = Long.MIN_VALUE;

    @SneakyThrows
    @Override
    public boolean isValid(Blackboard blackboard) {

        MulingRequestVm mulingRequest = blackboard.get(Keys.MULING_REQUEST);

        if(mulingRequest != null) {
            return true;
        }

        if(Bank.getCount(true, ItemId.COINS_995) < MINIMUM_COINS_TO_ACTIVATE_MULING) {
            return false;
        }

        if(System.currentTimeMillis() - lastCall < DELAY_BETWEEN_MULE_REQUESTS) {
            log.trace("Previous muling request was too recent");
            return false;
        }

        var mulingApi = blackboard.get(Keys.MULING_API);

        if(mulingApi == null) {
            log.warn("Muling api is null");
            return false;
        }

        var command = new CreateMulingRequest();
        command.setAmount(Constants.COINS_TO_MULE);
        command.setType(MulingRequestType.DEPOSIT);
        var mulingRequests = mulingApi.createMulingRequest(command);

        lastCall = System.currentTimeMillis();

        if(mulingRequests == null) {
            log.warn("Muling request is null");
            return false;
        }

        blackboard.put(Keys.MULING_REQUEST, mulingRequests);
        log.info("Muling request created: {}", mulingRequests);
        return true;
    }
}
