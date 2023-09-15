package net.rlbot.api.queries.results;

import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.scene.Players;

import java.util.Comparator;
import java.util.List;

public class SceneEntityQueryResults<T extends SceneEntity> extends QueryResults<T, SceneEntityQueryResults<T>>
{
	public SceneEntityQueryResults(List<T> results)
	{
		super(results);
	}

	public SceneEntityQueryResults<T> sortedByDistance(Positionable to)
	{
		return sorted(Comparator.comparingDouble(entity -> entity.distanceTo(to)));
	}

	public SceneEntityQueryResults<T> sortedByDistance()
	{
		return sortedByDistance(Players.getLocal());
	}

	public T nearest()
	{
		return nearest(Distance.EUCLIDEAN);
	}

	public T nearest(Distance distance) {
		return nearest(Players.getLocal(), distance);
	}

	public T nearest(Positionable to, Distance distance)
	{
		return results.stream().min(Comparator.comparingDouble(l -> l.distanceTo(to, distance))).orElse(null);
	}

	public T farthest()
	{
		return farthest(Players.getLocal());
	}

	public T farthest(Positionable to)
	{
		return results.stream().max(Comparator.comparingDouble(l -> l.distanceTo(to, Distance.EUCLIDEAN))).orElse(null);}

}
