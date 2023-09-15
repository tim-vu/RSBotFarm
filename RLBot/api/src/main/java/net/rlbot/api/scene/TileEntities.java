package net.rlbot.api.scene;

import net.rlbot.api.adapter.common.TileEntity;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.movement.position.Area;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

abstract class TileEntities<T extends TileEntity> extends SceneEntities<T> {

    @Override
    protected List<T> all(Predicate<T> filter) {
        var result = new ArrayList<T>();

        for(var position : Tiles.getAll()) {
            result.addAll(at(position, filter));
        }

        return result;
    }

    protected abstract List<T> at(Tile tile, Predicate<T> filter);

    public List<T> at(Tile tile, String... names) {
        return at(tile, Predicates.names(names));
    }

    protected List<T> at(Tile tile, int... ids) {
        return at(tile, Predicates.ids(ids));
    }

    protected List<T> within(Area area, Predicate<T> filter) {

        var result = new ArrayList<T>();

        for(var position : area.getPositions()) {
            result.addAll(at(Tiles.getAt(position), filter));
        }

        return result;
    }

    protected List<T> within(Area area, String ...names) {
        return within(area, Predicates.names(names));
    }

    protected List<T> within(Area area, int... ids) {
        return within(area, Predicates.ids(ids));
    }
}
