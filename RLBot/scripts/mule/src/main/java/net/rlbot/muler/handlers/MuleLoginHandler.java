package net.rlbot.muler.handlers;

import lombok.Getter;
import lombok.Setter;
import net.rlbot.api.script.randoms.LoginHandler;
import net.rlbot.api.script.randoms.RandomEventHandler;
import net.rlbot.muler.farm.FarmConnector;

public class MuleLoginHandler extends RandomEventHandler {

    private final LoginHandler loginHandler;

    @Getter
    @Setter
    private boolean disabled;

    public MuleLoginHandler() {
        loginHandler = new LoginHandler();
    }



    @Override
    public boolean shouldActivate() {
        return !disabled && loginHandler.shouldActivate();
    }

    @Override
    public int loop() {
        return loginHandler.loop();
    }

}
