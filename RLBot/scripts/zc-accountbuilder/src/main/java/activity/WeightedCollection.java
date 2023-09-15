package activity;

import net.rlbot.api.common.math.Random;

import java.util.List;

public class WeightedCollection {

    public static <T extends Weighted> T pickRandom(List<T> elements) {

        var sum = elements.stream().mapToDouble(Weighted::getWeight).sum();

        var random = Random.between(0, sum);

        var curr = 0;
        for (T weighted : elements) {

            if (random >= curr + weighted.getWeight()) {
                curr += weighted.getWeight();
                continue;
            }

            return weighted;
        }

        return elements.get(0);
    }
}
