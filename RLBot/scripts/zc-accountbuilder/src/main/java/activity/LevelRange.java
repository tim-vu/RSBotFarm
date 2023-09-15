package activity;

import lombok.Value;

@Value
public class LevelRange {

    int from;

    int to;

    public boolean isInRange(int level) {
        return this.from <= level && this.to > level;
    }
}
