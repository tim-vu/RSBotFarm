package net.rlbot.api.common.math;

import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public class Random {

    public static final java.util.Random RANDOM = new java.util.Random();

    public static int between(int min, int max) {
        int diff = Math.abs(max - min);
        return Math.min(min, max) + (diff == 0 ? 0 : RANDOM.nextInt(diff));
    }

    public static double between(double min, double max) {
        double diff = Math.abs(max - min);
        return Math.min(min, max) + RANDOM.nextDouble() * diff;
    }

    public static int guassian(int min, int max, int sd) {
        int mean = min + (max - min) / 2;
        int rand;
        do {
            rand = (int) (RANDOM.nextGaussian() * sd + mean);
        } while (rand < min || rand >= max);
        return rand;
    }

    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    @SafeVarargs
    public static <T> T nextElement(T... array) {
        var index = (Random.between(0, array.length));
        return array[index];
    }

    @NonNull
    public static <T> T nextElement(Collection<T> collection) {
        var index = (Random.between(0, collection.size()));

        if(collection instanceof List<T> list) {
            return list.get(index);
        }

        var i = 0;
        for(var element : collection) {

            if(index != i) {
                i++;
                continue;
            }

            return element;
        }

        return null;
    }

    public static <T extends Enum<T>> T nextElement(Class<T> clazz) {
        var values = clazz.getEnumConstants();
        return values[Random.between(0, values.length)];
    }

}
