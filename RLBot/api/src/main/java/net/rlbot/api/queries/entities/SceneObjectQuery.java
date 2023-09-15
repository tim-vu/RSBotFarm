package net.rlbot.api.queries.entities;

import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.movement.Position;
import net.rlbot.api.queries.results.SceneEntityQueryResults;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.scene.Tiles;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SceneObjectQuery extends SceneEntityQuery<SceneObject, SceneObjectQuery>
{

	private Class<? extends SceneObject>[] is = null;

	public SceneObjectQuery(Supplier<List<SceneObject>> supplier) {

		super(supplier);
	}

	@Override
	public SceneObjectQuery locations(Position... positions) {
		setSupplier(() -> {
			var result = new LinkedList<SceneObject>();

			for(var position : positions) {
				result.addAll(SceneObjects.getAt(position, Predicates.always()));
			}

			return result;
		});

		return this;
	}

	@SafeVarargs
	public final SceneObjectQuery is(Class<? extends SceneObject>... classes) {

		this.is = classes;
		return this;
	}

	@Override
	protected SceneEntityQueryResults<SceneObject> results(List<SceneObject> list) {

		return new SceneEntityQueryResults<>(list);
	}
}
