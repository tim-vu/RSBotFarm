package net.rlbot.script.api.reaction;

import java.util.Random;

public class ReactionGenerator {

    private static final Random RANDOM = new Random();

    public static double nextGaussian(double mu, double sigma) {

        return mu + RANDOM.nextGaussian() * sigma;
    }

    public static double nextExponential(double lambda) {

        return -Math.log(1 - RANDOM.nextDouble()) / lambda;
    }

    public static double nextExGaussian(double mu, double sigma, double lambda) {

        return nextGaussian(mu, sigma) + nextExponential(lambda);
    }

    public static double nextUniform(double minimum, double maximum) {

        return RANDOM.nextDouble() * (maximum - minimum) + minimum;
    }

    public static int getReactionTime(Reaction reaction) {

        return nextReactionTime(reaction.getMean(), reaction.getStandardDeviation(), reaction.getExponentialDecay(), reaction.getUniformProbability(), reaction.getMinimumReactionTime(), reaction.getMaximumReactionTime());
    }

    public static int nextReactionTime(double mu, double sigma, double lambda, double p, double minimum, double maximum) {

        if (RANDOM.nextDouble() < p) {
            return (int) Math.round(nextUniform(minimum, maximum));
        }

        double exGaussian;
        do {
            exGaussian = nextExGaussian(mu, sigma, lambda);
        } while (exGaussian < minimum || exGaussian > maximum);

        return (int) Math.round(exGaussian);
    }

    public static double nextDouble(double min, double max) {

        return RANDOM.nextDouble() * (max - min) + min;
    }

}
