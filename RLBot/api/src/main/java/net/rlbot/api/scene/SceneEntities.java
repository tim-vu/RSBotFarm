package net.rlbot.api.scene;

import net.rlbot.api.common.Predicates;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.common.SceneEntity;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

abstract class SceneEntities<T extends SceneEntity> {

    protected abstract List<T> all(Predicate<T> filter);

    protected List<T> all(String... names)
    {
        return all(Predicates.names(names));
    }

    protected List<T> all(int... ids)
    {
        return all(Predicates.ids(ids));
    }

    protected T nearest(Positionable to, Predicate<? super T> filter)
    {
        return all(x -> x.getId() != -1 && filter.test(x)).stream()
                .min(Comparator.comparingDouble(t -> t.distanceTo(to)))
                .orElse(null);
    }

    protected T nearest(Positionable to, String... names)
    {
        return nearest(to, Predicates.names(names));
    }

    protected T nearest(Positionable to, int... ids)
    {
        return nearest(to, Predicates.ids(ids));
    }
}
