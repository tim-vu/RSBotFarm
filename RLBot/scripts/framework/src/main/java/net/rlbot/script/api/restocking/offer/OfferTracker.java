package net.rlbot.script.api.restocking.offer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
public class OfferTracker {

    private static final int OFFER_TIMEOUT_SECONDS = 30;

    private static final double PRICE_INCREASE_PER_INTERVAL = 0.1;

    private static final int MAX_PRICE_INCREASES = 15;


    @Getter
    private final LocalDateTime dateTimeCreated;

    @Getter
    private int initialPriceIncreases;

    @Getter
    @Setter
    private int currentPriceIncreases;

    private final boolean buying;

    public OfferTracker(int initialPriceIncreases, boolean buying) {
        this.initialPriceIncreases = initialPriceIncreases;
        this.currentPriceIncreases = initialPriceIncreases;
        this.dateTimeCreated = LocalDateTime.now();
        this.buying = buying;
    }

    public int getGoalPrice(int guidePrice) {

        var priceDiffPerInterval = Math.max(3, guidePrice * PRICE_INCREASE_PER_INTERVAL);

        var sign = this.buying ? 1 : -1;

        return Math.max(1, guidePrice + sign * (int)(getGoalPriceIncreases() * priceDiffPerInterval));
    }

    public int getGoalPriceIncreases() {
        var secondsPast = this.dateTimeCreated.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        return Math.min((int)(secondsPast / OFFER_TIMEOUT_SECONDS) + this.initialPriceIncreases, MAX_PRICE_INCREASES);
    }

    public boolean shouldAbort() {
        var goalPriceIncreases = getGoalPriceIncreases();
        return getGoalPriceIncreases() != this.currentPriceIncreases;
    }

}
