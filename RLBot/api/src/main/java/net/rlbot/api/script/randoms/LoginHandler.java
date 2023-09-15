package net.rlbot.api.script.randoms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.internal.managers.AccountManager;
import net.rlbot.api.Game;
import net.rlbot.api.GameState;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Worlds;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.input.Mouse;
import net.rlbot.internal.managers.AccountStatus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import static net.logstash.logback.argument.StructuredArguments.kv;


@Slf4j
public class LoginHandler extends RandomEventHandler {

    private static final Rectangle ACCEPT_EULA_BUTTON = new Rectangle(236, 297, 134, 31);

    private static final int LOGIN_FIELD_USERNAME = 0;

    private static final int LOGIN_FIELD_PASSWORD = 1;

    private static BufferedImage lastFrame;

    @Override
    public boolean shouldActivate() {
        var state = Game.getState();
        return state == GameState.LOGIN_SCREEN || state == GameState.LOGGING_IN || state == GameState.UNKNOWN;
    }

    @Override
    public int loop() {

        if(!AccountManager.isAccountAvailable()) {
            stopScript();
            return 0;
        }

        if(Game.getState() == GameState.LOGGING_IN) {
            log.debug("Logging in");

            if(!Time.sleepUntil(() -> Game.getState() != GameState.LOGGING_IN, 10000)) {
                log.debug("Failed to login");
                return 500;
            }

            if(!Time.sleepUntil(WelcomeScreenHandler::shouldActive, 10000)) {
                log.debug("Failed to reach welcome screen");
                return 500;
            }

            return 500;
        }

        var loginState = getLoginState();

        if(loginState == LoginState.WELCOME_TO_RUNESCAPE) {
            Keyboard.sendText(System.lineSeparator(), false);
            Time.sleepUntil(() -> getLoginState() == LoginState.ENTER_CREDENTIALS, 2000);
            return 500;
        }

        if(loginState == LoginState.ENTER_CREDENTIALS) {

            if(lastFrame == null) {
                Game.getDrawManager().requestNextFrameListener(this::onNextFrame);
                return 100;
            }

            var loginResponse = LoginResponse.getLoginResponse(lastFrame);

            if(loginResponse == LoginResponse.ENTER_USERNAME) {

                var currentField = Game.getCurrentLoginField();

                Game.setUsername(AccountManager.getUsername());
                Game.setPassword(AccountManager.getPassword());

                if (currentField == LOGIN_FIELD_USERNAME) {

                    Keyboard.sendText("\n", false);

                    if(!Time.sleepUntil(() -> Game.getCurrentLoginField() == LOGIN_FIELD_PASSWORD, 1000)) {
                        return 0;
                    }

                }

                Keyboard.sendText("\n", false);

                if(!Time.sleepUntil(() -> Game.getState() == GameState.LOGGING_IN, 1000)) {
                    log.debug("Failed to initiate login");
                    return 2000;
                }

                return 200;
            }

            if(loginResponse == LoginResponse.MEMBERS_REQUIRED) {
                log.info("Members required, changing worlds");

                var otherWorld = Worlds.getLoaded()
                        .stream()
                        .filter(w -> !w.isMembers())
                        .findFirst()
                        .orElse(null);

                if(otherWorld == null) {
                    log.error("Failed to find a non members world, stopping script");
                    stopScript();
                    return 0;
                }

                Game.setWorld(otherWorld);
                Time.sleepUntil(() -> Game.getWorld() == otherWorld, 2000);
                return 200;
            }

            if(loginResponse == LoginResponse.RULE_BREAKING) {
                log.info("Account disabled for rule breaking, stopping script");
                AccountManager.setAccountStatus(AccountStatus.BANNED);
                stopScript();
                return 200;
            }
        }

        if(loginState == LoginState.ACCEPT_EULA) {
            log.debug("Accepting EULA");
            Mouse.click((int)ACCEPT_EULA_BUTTON.getCenterX(), (int)ACCEPT_EULA_BUTTON.getCenterY(), true);
            Time.sleepUntil(() -> getLoginState() == LoginState.WELCOME_TO_RUNESCAPE, 1000);
            return 200;
        }

        if(loginState == LoginState.RULE_BREAKING) {
            //TODO: Verify if this is not a duplicate case
            log.info("Account banned, stopping scripts");
            AccountManager.setAccountStatus(AccountStatus.BANNED);
            stopScript();
            return 200;
        }

        if(loginState == LoginState.INCORRECT_CREDENTIALS) {
            log.info("Incorrect credentials, stopping script");
            AccountManager.setAccountStatus(AccountStatus.INVALID_CREDENTIALS);
            stopScript();
            return 200;
        }

        log.warn("Unknown {}, stopping script", kv("loginState", loginState));
        stopScript();
        return 0;
    }

    private void onNextFrame(Image image) {
        if(!(image instanceof BufferedImage bufferedImage)) {
            log.warn("Frame is not a BufferedImage");
            return;
        }

        lastFrame = bufferedImage;
    }

    private static LoginState getLoginState() {
        return LoginState.fromIndex(Game.getLoginIndex());
    }

    @AllArgsConstructor
    private enum LoginState {

        UNKNOWN(-1),
        WELCOME_TO_RUNESCAPE(0),
        ENTER_CREDENTIALS(2),
        INCORRECT_CREDENTIALS(3),
        ACCEPT_EULA(12),
        EULA_MUST_BE_ACCEPTED(13),
        RULE_BREAKING(14);

        @Getter
        private final int index;

        public static LoginState fromIndex(int index) {
            for(var i = 1; i < LoginState.values().length; i++) {

                var loginState = LoginState.values()[i];

                if(loginState.getIndex() != index) {
                    continue;
                }

                return loginState;
            }

            return LoginState.UNKNOWN;
        }

    }

    @AllArgsConstructor
    public enum LoginResponse {

        UNKNOWN((String)null),
        ENTER_USERNAME("enter_username.png"),
//        INCORRECT_CREDENTIALS("incorrect_credentials.png"),
        MEMBERS_REQUIRED("members_required.png"),
        RULE_BREAKING("rule_breaking.png");

        private static final Point TOP_LEFT = new Point(213,184);
        private static final Point BOTTOM_RIGHT = new Point(549,234);

        private final BufferedImage image;

        @SneakyThrows
        LoginResponse(String imageFileName) {

            if(imageFileName == null) {
                image = null;
                return;
            }

            var classLoader = Thread.currentThread().getContextClassLoader();
            this.image = ImageIO.read(classLoader.getResourceAsStream(imageFileName));
        }

        public static LoginResponse getLoginResponse(@NonNull BufferedImage image) {

            var subImage = image.getSubimage(
                    (int)TOP_LEFT.getX(),
                    (int)TOP_LEFT.getY(),
                    (int)BOTTOM_RIGHT.getX() - (int)TOP_LEFT.getX(),
                    (int)BOTTOM_RIGHT.getY() - (int)TOP_LEFT.getY()
            );

            for(var i = 1; i < LoginResponse.values().length; i++) {

                var response = LoginResponse.values()[i];

                if(!compareImages(subImage, response.image)) {
                    continue;
                }

                return response;
            }

            return null;
        }

        private static boolean compareImages(BufferedImage one, BufferedImage two) {
            for(var x = 0; x < one.getWidth(); x++) {
                for(var y = 0; y < one.getHeight(); y++){

                    if(one.getRGB(x, y) != two.getRGB(x, y)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
