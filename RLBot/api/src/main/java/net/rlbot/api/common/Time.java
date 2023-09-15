package net.rlbot.api.common;

import net.rlbot.api.Game;
import net.rlbot.api.GameState;
import net.rlbot.api.common.math.Random;
import net.rlbot.internal.ApiContext;

import java.util.function.BooleanSupplier;

public class Time {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final int DEFAULT_POLLING_DELAY = 100;

    public static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(int min, int max) {
        sleep(Random.between(min, max));
    }


    public static boolean sleepUntil(BooleanSupplier awaitedCondition, int awaitTimeout) {
        return sleepUntil(awaitedCondition, () -> false, awaitTimeout);
    }

    public static boolean sleepUntil(BooleanSupplier awaitedCondition, BooleanSupplier resetTimeoutSupplier, int awaitTimeout) {
        boolean done;
        long startTime = System.currentTimeMillis();
        do {
            done = awaitedCondition.getAsBoolean();
            Time.sleep(DEFAULT_POLLING_DELAY);

            if(resetTimeoutSupplier.getAsBoolean()) {
                startTime = System.currentTimeMillis();
            }

        } while (!done && System.currentTimeMillis() - startTime < awaitTimeout);

        return done;
    }
    public static boolean sleepWhile(BooleanSupplier breakCondition, int timeout) {
        long startTime = System.currentTimeMillis();
        do {

            if(!breakCondition.getAsBoolean()) {
                return false;
            }

            Time.sleep(DEFAULT_POLLING_DELAY);

        } while (System.currentTimeMillis() - startTime < timeout);

        return true;
    }

    public static boolean sleepTick() {
        return sleepTicks(1);
    }

    public static boolean sleepTicks(int ticks)
    {
        if (Game.getState() == GameState.LOGIN_SCREEN || Game.getState() == GameState.LOGIN_SCREEN_AUTHENTICATOR)
        {
            return false;
        }

        for (int i = 0; i < ticks; i++)
        {
            long start = API_CONTEXT.getClient().getTickCount();

            while (API_CONTEXT.getClient().getTickCount() == start)
            {
                try
                {
                    Thread.sleep(DEFAULT_POLLING_DELAY);
                }
                catch (InterruptedException e)
                {
                    return false;
                }
            }
        }

        return true;
    }
}
