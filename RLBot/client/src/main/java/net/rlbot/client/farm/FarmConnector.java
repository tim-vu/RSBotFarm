package net.rlbot.client.farm;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.failsafe.Failsafe;
import dev.failsafe.Fallback;
import dev.failsafe.RetryPolicy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.GameState;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.scene.Players;
import net.rlbot.client.api.AccountsApi;
import net.rlbot.client.api.ClientsApi;
import net.rlbot.client.api.invoker.ApiClient;
import net.rlbot.client.api.invoker.ApiException;
import net.rlbot.client.api.model.ActiveSessionVm;
import net.rlbot.client.api.model.UpdateAccount;
import net.rlbot.client.api.model.UpdatePlayerDetails;
import net.rlbot.client.script.handler.ScriptHandler;
import net.rlbot.client.script.loader.ScriptLoader;
import net.rlbot.client.script.loader.ScriptWrapper;
import net.rlbot.client.ui.BotUI;
import net.rlbot.internal.managers.AccountManager;
import net.rlbot.internal.managers.AccountStatus;
import net.runelite.api.events.GameStateChanged;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class FarmConnector {

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    private static final Fallback<ActiveSessionVm> ACTIVE_SESSION_FALLBACK = Fallback.of(() -> null);
    private static final RetryPolicy<ActiveSessionVm> ACTIVE_SESSION_RETRY_POLICY = RetryPolicy.<ActiveSessionVm>builder()
            .withMaxAttempts(10)
            .withBackoff(1, 100, ChronoUnit.SECONDS)
            .onFailedAttempt(e -> log.error("Failed to get active session, retrying", e.getLastException()))
            .onFailure(e -> log.error("Failed to get active session, giving up"))
            .handle(ApiException.class)
            .handleResult(null)
            .build();

    private static final RetryPolicy<Void> UPDATE_PLAYER_DETAILS_RETRY_POLICY = RetryPolicy.<Void>builder()
            .withMaxAttempts(10)
            .withDelay(Duration.ofSeconds(1))
            .onFailedAttempt(e -> log.warn("Failed to update player details, retrying"))
            .onFailure(e -> log.error("Failed to update player details, giving up"))
            .handle(ApiException.class)
            .handleResult(null)
            .build();

    private final BotUI botUi;

    private final ScriptLoader scriptLoader;

    private final ScriptHandler scriptHandler;

    private final ClientsApi clientApi;
    private final AccountsApi accountsApi;

    private ActiveSessionVm activeSession;

    private boolean firstLogin = false;

    @Inject
    public FarmConnector(
            BotUI botUi,
            ScriptLoader scriptLoader,
            ScriptHandler scriptHandler,
            @Named("botId") String botId) {
        this.botUi = botUi;
        this.scriptLoader = scriptLoader;
        this.scriptHandler = scriptHandler;

        var apiClient = new ApiClient();
        apiClient.setApiKey(botId);
        log.debug("Base path" + apiClient.getBasePath());

        this.clientApi = new ClientsApi(apiClient);
        this.accountsApi = new AccountsApi(apiClient);
    }

    @SneakyThrows
    public boolean init() {

        var activeSession = Failsafe.with(ACTIVE_SESSION_FALLBACK, ACTIVE_SESSION_RETRY_POLICY).get(clientApi::getActiveSession);

        if(activeSession == null) {
            return false;
        }

        this.activeSession = activeSession;

        var script = getScript(this.activeSession.getScript());

        if(script == null) {
            log.error("Script {} does not exist, stopping client", this.activeSession.getScript());
            stopClient();
            return false;
        }

        AccountManager.setAccount(this.activeSession.getAccount().getUsername(), this.activeSession.getAccount().getPassword());

        try {
            log.debug("Starting script {}", script.getName());
            scriptHandler.startScript(script.newInstance());
        }catch(Exception ex) {
            log.error("Failed to start script", ex);
            stopClient();
            return false;
        }

        SCHEDULER.scheduleAtFixedRate(this::sendHeartbeat, 0, 10, TimeUnit.SECONDS);
        SCHEDULER.scheduleAtFixedRate(this::updateAccount, 60, 30, TimeUnit.SECONDS);
        SCHEDULER.scheduleAtFixedRate(this::updatePlayerDetails, 2, 5, TimeUnit.MINUTES);
        return true;
    }

    private ScriptWrapper getScript(String name) {

        if(!scriptLoader.isLoaded()) {
            scriptLoader.loadScripts();
        }

        return scriptLoader.getScripts().stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private void sendHeartbeat() {

        try {
            log.debug("Sending heartbeat");
            clientApi.sendHeartbeat();
        }catch(Exception ex) {
            log.error("Failed to send heartbeat", ex);
        }
    }

    private void updateAccount() {

        var status = AccountManager.getAccountStatus();

        if(status == AccountStatus.UNKNOWN || status == AccountStatus.VALID) {
            return;
        }

        var accountStatus = switch(status) {
            case BANNED -> net.rlbot.client.api.model.AccountStatus.BANNED;
            case INVALID_CREDENTIALS -> net.rlbot.client.api.model.AccountStatus.INVALIDCREDENTIALS;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };

        log.debug("Updating account status to {}", accountStatus);
        var command = new UpdateAccount();
        command.setId(this.activeSession.getAccount().getId());
        command.setStatus(accountStatus);

        try {
            accountsApi.updateAccount(this.activeSession.getAccount().getId(), command);
            stopClient();
        }catch(Exception ex) {
            log.error("Failed to update account status", ex);
        }
    }

    public void onGameStateChanged(GameStateChanged gameStateChanged) {

        if (gameStateChanged.getGameState() == net.runelite.api.GameState.LOGGED_IN) {

            if (!firstLogin) {
                firstLogin = true;
            }

            SCHEDULER.schedule(
                    () -> {
                        Failsafe.with(UPDATE_PLAYER_DETAILS_RETRY_POLICY).run(this::updatePlayerDetails);
                        firstLogin = true;
                    },
                    0,
                    TimeUnit.SECONDS
            );
        }
    }

    private void updatePlayerDetails() {

        if(Game.getState() != GameState.LOGGED_IN) {
            return;
        }

        var command = new UpdatePlayerDetails();
        command.setId(this.activeSession.getAccount().getId());
        command.setDisplayName(Players.getLocal().getName());
        command.setSkills(Arrays.stream(Skill.values()).collect(Collectors.toMap(Enum::name, Skills::getExperience)));

        try {
            accountsApi.updatePlayerDetails(this.activeSession.getAccount().getId(), command);
        } catch (ApiException e) {
            log.error("Failed to update player details", e);
        }
    }

    @SneakyThrows
    private void stopClient() {
        log.debug("Stopping client");
        clientApi.signalStop();
        botUi.shutdownClient();
    }
}
