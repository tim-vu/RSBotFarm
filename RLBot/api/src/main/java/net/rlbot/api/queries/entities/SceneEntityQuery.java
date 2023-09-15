package net.rlbot.api.queries.entities;

import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.queries.Query;
import net.rlbot.api.queries.results.SceneEntityQueryResults;
import net.rlbot.api.scene.Players;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class SceneEntityQuery<T extends SceneEntity, Q extends SceneEntityQuery<T, Q>>
		extends Query<T, Q, SceneEntityQueryResults<T>>
{
	private Integer maxDistance = null;

	private Position distanceSrc = null;

	private Set<Integer> ids = null;

	private Set<String> names = null;

	private Set<String> nameContains = null;

	private Set<String> actions = null;

	private Set<Position> locations = null;

	private Area area = null;

	private Boolean reachable = null;

	protected SceneEntityQuery(Supplier<List<T>> supplier)
	{
		super(supplier);
	}

	public Q ids(int... ids)
	{
		this.ids = Arrays.stream(ids).boxed().collect(Collectors.toSet());
		return (Q) this;
	}

	public Q ids(Set<Integer> ids) {
		this.ids = new HashSet<>(ids);
		return (Q) this;
	}

	public Q names(String... names)
	{
		this.names = Arrays.stream(names).collect(Collectors.toSet());;
		return (Q) this;
	}

	public Q nameContains(String... strings) {
		this.nameContains = Arrays.stream(strings).collect(Collectors.toSet());
		return (Q) this;
	}

	public Q names(Collection<String> names) {
		this.names = new HashSet<>(names);
		return (Q) this;
	}

	public Q actions(String... actions)
	{
		this.actions = Arrays.stream(actions).collect(Collectors.toSet());
		return (Q) this;
	}

	public Q locations(Position... locations) {
		this.locations = Arrays.stream(locations).collect(Collectors.toSet());
		return (Q) this;
	}

	public Q within(Area area) {
		this.area = area;
		return (Q) this;
	}

	public Q within(int distance) {
		this.area = Area.surrounding(Players.getLocal().getPosition(), distance);
		return (Q) this;
	}

	public Q distance(Positionable source, int maxDistance) {
		this.distance(source.getPosition(), maxDistance);
		return (Q) this;
	}

	public Q distance(Position source, int maxDistance)
	{
		this.distanceSrc = source;
		this.maxDistance = maxDistance;
		return (Q) this;
	}

	public Q distance(int maxDistance)
	{
		this.maxDistance = maxDistance;
		return (Q) this;
	}

	public Q reachable() {
		this.reachable = true;
		return (Q) this;
	}

	@Override
	public boolean test(T t)
	{
		if (ids != null && !ids.contains(t.getId()))
		{
			return false;
		}

		if (names != null && !names.contains(t.getName()))
		{
			return false;
		}

		if(nameContains != null && nameContains.stream().noneMatch(c -> t.getName().contains(c))) {
			return false;
		}

		if (actions != null && actions.stream().noneMatch(Predicates.texts(t.getActions())))
		{
			return false;
		}

		if(area != null && !area.contains(t.getPosition())) {
			return false;
		}

		if (maxDistance != null)
		{
			if (distanceSrc == null)
			{
				distanceSrc = Players.getLocal().getPosition();
			}

			if (distanceSrc.distanceTo(t) > maxDistance)
			{
				return false;
			}
		}

		if(locations != null) {

			if(!locations.contains(t.getPosition())) {
				return false;
			}

		}

		if(reachable != null) {

			if(Reachable.isReachable(t.getPosition()) == reachable) {
				return false;
			}
		}

		return super.test(t);
	}
}
