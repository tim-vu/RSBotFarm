package net.rlbot.api.queries.entities;


import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.queries.results.SceneEntityQueryResults;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NpcQuery extends ActorQuery<Npc, NpcQuery>
{

	private Set<Integer> indices = null;

	public NpcQuery(Supplier<List<Npc>> supplier) {

		super(supplier);
	}

	public NpcQuery indices(int... indices) {

		this.indices = Arrays.stream(indices).boxed().collect(Collectors.toSet());
		return this;
	}

	@Override
	protected SceneEntityQueryResults<Npc> results(List<Npc> list) {

		return new SceneEntityQueryResults<>(list);
	}

	@Override
	public boolean test(Npc npc) {

		if (indices != null && !indices.contains(npc.getIndex())) {
			return false;
		}

		return super.test(npc);
	}
}
