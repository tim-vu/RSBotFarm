package net.rlbot.muler.farm;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.muler.api.MuleApi;
import net.rlbot.muler.api.invoker.ApiClient;
import net.rlbot.muler.api.model.ActiveMulingRequestVm;
import net.rlbot.muler.api.model.CreateMule;
import net.rlbot.muler.api.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class FarmConnector {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final MuleApi mulesApi;

    public FarmConnector(String botId) {

        var apiClient = new ApiClient();
        apiClient.setApiKey(botId);

        mulesApi = new MuleApi(apiClient);
    }

    public void init(String displayName, Position position, int world) {

        try {
            var command = new CreateMule();
            command.setPosition(position);
            command.setWorld(world);
            mulesApi.createMule(command);
        } catch(Exception ex) {
            log.error("Failed to create mule", ex);
            return;
        }

        scheduler.scheduleAtFixedRate(this::fetchMuleRequests, 0, 10, java.util.concurrent.TimeUnit.SECONDS);
        initialized = true;
    }

    @Getter
    private boolean initialized = false;

    public List<ActiveMulingRequestVm> getMulingRequests() {
        return Collections.unmodifiableList(mulingRequests);
    }

    private List<ActiveMulingRequestVm> mulingRequests = new ArrayList<>();

    @SneakyThrows
    private void fetchMuleRequests() {
        mulingRequests = mulesApi.getMulingRequests();
    }

}
