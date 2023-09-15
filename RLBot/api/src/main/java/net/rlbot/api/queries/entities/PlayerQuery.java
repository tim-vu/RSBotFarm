package net.rlbot.api.queries.entities;


import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.queries.results.SceneEntityQueryResults;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlayerQuery extends ActorQuery<Player, PlayerQuery>
{
	private Set<Integer> playerIds = null;

	public PlayerQuery(Supplier<List<Player>> supplier) {

		super(supplier);
	}

	public PlayerQuery playerIds(int... ids) {

		this.playerIds = Arrays.stream(ids).boxed().collect(Collectors.toSet());
		return this;
	}

	@Override
	protected SceneEntityQueryResults<Player> results(List<Player> list) {

		return new SceneEntityQueryResults<>(list);
	}

	@Override
	public boolean test(Player player) {

		if (playerIds != null && !playerIds.contains(player.getId())) {
			return false;
		}

		return super.test(player);
	}
}
