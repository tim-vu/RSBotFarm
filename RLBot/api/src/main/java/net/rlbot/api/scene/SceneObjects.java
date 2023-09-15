package net.rlbot.api.scene;

import lombok.NonNull;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.entities.SceneObjectQuery;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.movement.position.Area;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SceneObjects extends TileEntities<SceneObject>{

    private static SceneObjects instance;

    private static ApiContext apiContext;

    private SceneObjects(){
    }

    private static void init(@NonNull ApiContext apiContext) {
        SceneObjects.apiContext = apiContext;
        instance = new SceneObjects();
    }

    @Override
    protected List<SceneObject> at(Tile tile, Predicate<SceneObject> filter) {
        return Arrays.stream(tile.getSceneObjects()).filter(filter).collect(Collectors.toList());
    }

    public static SceneObjectQuery query() {
        return new SceneObjectQuery(SceneObjects::getAll);
    }

    public static List<SceneObject> getAll() {
        return instance.all(Predicates.always());
    }

    public static List<SceneObject> getAll(Predicate<SceneObject> predicate) {
        return instance.all(predicate);
    }

    public static List<SceneObject> getAll(String... names) {
        return instance.all(names);
    }

    public static List<SceneObject> getAll(int... ids) {
        return instance.all(ids);
    }

    public static SceneObject getNearest(Positionable to, Predicate<SceneObject> filter) {
        return instance.nearest(to, filter);
    }

    public static SceneObject getNearest(Positionable to, String... names) {
        return instance.nearest(to, names);
    }

    public static SceneObject getNearest(Positionable to, int... ids) {
        return instance.nearest(to, ids);
    }

    public static SceneObject getNearest(Predicate<SceneObject> filter) {
        return instance.nearest(Players.getLocal(), filter);
    }

    public static SceneObject getNearest(String... names) {
        return instance.nearest(Players.getLocal(), names);
    }

    public static SceneObject getNearest(int... ids) {
        return instance.nearest(Players.getLocal(), ids);
    }

    public static List<SceneObject> getAt(Positionable positionable, Predicate<SceneObject> filter) {
        return instance.at(Tiles.getAt(positionable.getPosition()), filter);
    }

    public static List<SceneObject> getAt(Positionable positionable, String... names) {
        return instance.at(Tiles.getAt(positionable.getPosition()), names);
    }

    public static List<SceneObject> getAt(Positionable positionable, int... ids) {
        return instance.at(Tiles.getAt(positionable.getPosition()), ids);
    }

    public static SceneObject getFirstAt(Positionable positionable, Predicate<SceneObject> filter) {
        return instance.at(Tiles.getAt(positionable.getPosition()), filter).stream().findFirst().orElse(null);
    }

    public static SceneObject getFirstAt(Positionable positionable, String... names) {
        return instance.at(Tiles.getAt(positionable.getPosition()), names).stream().findFirst().orElse(null);
    }

    public static SceneObject getFirstAt(Positionable positionable, int... ids) {
        return instance.at(Tiles.getAt(positionable.getPosition()), ids).stream().findFirst().orElse(null);
    }

    public static SceneObject getFirstAt(Positionable positionable) {
        return instance.at(Tiles.getAt(positionable.getPosition()), Predicates.always()).stream().findFirst().orElse(null);
    }

    public static List<SceneObject> getWithin(Area area, Predicate<SceneObject> filter) {
        return instance.within(area, filter);
    }

    public static List<SceneObject> getWithin(Area area, String... names) {
        return instance.within(area, names);
    }

    public static List<SceneObject> getWithin(Area area, int... ids) {
        return instance.within(area, ids);
    }
}
