package net.rlbot.api.scene;

import lombok.NonNull;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.entities.PickableQuery;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.movement.position.Area;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Pickables extends TileEntities<Pickable>{

	private static Pickables instance;

	private static ApiContext apiContext;

	private Pickables(){

	}

	private static void init(@NonNull ApiContext apiContext) {

		Pickables.apiContext = apiContext;
		instance = new Pickables();
	}

	@Override
	protected List<Pickable> at(Tile tile, Predicate<Pickable> filter) {
		return Arrays.stream(tile.getPickables()).filter(filter).collect(Collectors.toList());
	}

	public static PickableQuery query() {
		return new PickableQuery(Pickables::getAll);
	}

	public static List<Pickable> getAll(){
		return instance.all(Predicates.always());
	}

	public static List<Pickable> getAll(Predicate<Pickable> predicate) {
		return instance.all(predicate);
	}

	public static List<Pickable> getAll(String... names) {

		return instance.all(names);
	}

	public static List<Pickable> getAll(int... ids) {

		return instance.all(ids);
	}

	public static Pickable getNearest(Positionable to, Predicate<Pickable> filter) {

		return instance.nearest(to, filter);
	}

	public static Pickable getNearest(Positionable to, String... names) {

		return instance.nearest(to, names);
	}

	public static Pickable getNearest(Positionable to, int... ids) {

		return instance.nearest(to, ids);
	}

	public static Pickable getNearest(Predicate<Pickable> filter) {

		return instance.nearest(Players.getLocal(), filter);
	}

	public static Pickable getNearest(String... names) {

		return instance.nearest(Players.getLocal(), names);
	}

	public static Pickable getNearest(int... ids) {

		return instance.nearest(Players.getLocal(), ids);
	}

	public static List<Pickable> getAt(Positionable positionable, Predicate<Pickable> filter) {

		return instance.at(Tiles.getAt(positionable.getPosition()), filter);
	}

	public static List<Pickable> getAt(Positionable positionable, String... names) {
		return instance.at(Tiles.getAt(positionable.getPosition()), names);
	}

	public static List<Pickable> getAt(Positionable positionable, int... ids) {
		return instance.at(Tiles.getAt(positionable.getPosition()), ids);
	}

	public static List<Pickable> getWithin(Area area, Predicate<Pickable> filter) {

		return instance.within(area, filter);
	}

	public static List<Pickable> getWithin(Area area, String... names) {

		return instance.within(area, names);
	}

	public static List<Pickable> getWithin(Area area, int... ids) {

		return instance.within(area, ids);
	}
}
