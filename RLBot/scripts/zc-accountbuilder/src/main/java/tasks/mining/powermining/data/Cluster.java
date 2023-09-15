package tasks.mining.powermining.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.movement.position.Area;
import tasks.mining.powermining.enums.Rock;

@AllArgsConstructor
public class Cluster implements Comparable<Cluster> {

    @Getter
    private final Area area;

    @Getter
    private int rockCount;

    @Getter
    private final Rock rock;

    @Override
    public int compareTo(Cluster o) {

        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if(getRockCount() < o.getRockCount())
        {
            return BEFORE;
        }

        if(getRockCount() > o.getRockCount())
        {
            return AFTER;
        }

        return EQUAL;
    }
}
