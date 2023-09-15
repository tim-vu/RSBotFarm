package net.rlbot.script.api.reaction;

import lombok.Getter;
import net.rlbot.api.common.Time;

import java.util.function.BooleanSupplier;

public enum Reaction {

    REGULAR(220d, 50d, 0.007d, 0.2d, 120d, 2000),
    PREDICTABLE(30d, 10d, 0.02d, 0.01d, 30d, 500d),
    AFK(13000d, 3500d, 5d, 0.1d, 2000d, 30000);

    private static final double MEAN_VARIANCE = 0.1d;

    @Getter
    private final double mean;


    @Getter
    private final double standardDeviation;

    @Getter
    private final double exponentialDecay;

    @Getter
    private final double uniformProbability;

    @Getter
    private final double minimumReactionTime;

    @Getter
    private final double maximumReactionTime;

    Reaction(double mean, double standardDeviation, double exponentialDecay, double uniformProbability, double minimumReactionTime, double maximumReactionTime) {

        this.mean = mean * ReactionGenerator.nextDouble(1d - MEAN_VARIANCE, 1 + MEAN_VARIANCE);
        this.standardDeviation = standardDeviation;
        this.exponentialDecay = exponentialDecay;
        this.uniformProbability = uniformProbability;
        this.minimumReactionTime = minimumReactionTime;
        this.maximumReactionTime = maximumReactionTime;
    }

    public void sleep() {

        Time.sleep(ReactionGenerator.getReactionTime(this));
    }

    public void sleepWhile(BooleanSupplier breakCondition) {
        Time.sleepWhile(breakCondition, ReactionGenerator.getReactionTime(this));
    }

    public int generate() {
        return ReactionGenerator.getReactionTime(this);
    }
}
