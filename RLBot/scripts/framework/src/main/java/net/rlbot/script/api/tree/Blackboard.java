package net.rlbot.script.api.tree;

import java.util.HashMap;
import java.util.Map;

public class Blackboard {

    private final Map<Key<?>, Object> map = new HashMap<>();

    public <T> T get(Key<T> key) {
        //noinspection unchecked
        return (T)map.get(key);
    }

    public <T> void put(Key<T> key, T value) {
        this.map.put(key, value);
    }

}
