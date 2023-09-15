package net.rlbot.api.scene;

import lombok.NonNull;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.entities.PlayerQuery;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.scene.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Players extends SceneEntities<Player>{

	private static ApiContext apiContext;

	private static Players instance;

	private Players() {
	}

	private static void init(@NonNull ApiContext apiContext) {
		Players.apiContext = apiContext;
		instance = new Players();
	}

	@Override
	protected List<Player> all(Predicate<Player> filter) {

		var players = apiContext.getClient().getPlayers();
		var me = apiContext.getClient().getLocalPlayer();

		var result = new ArrayList<Player>();

		for (net.runelite.api.Player rPlayer : players) {

			if(rPlayer == null || rPlayer.equals(me)) {
				continue;
			}

			var player = new Player(rPlayer);

			if (filter.test(player)) {
				result.add(player);
			}

		}

		return result;
	}

	@NonNull
	public static PlayerQuery query() {
		return new PlayerQuery(Players::getAll);
	}

	@NotNull
	public static Player getLocal() {

		var rlPlayer = apiContext.getClient().getLocalPlayer();

		if(rlPlayer == null) {
			//noinspection DataFlowIssue
			return null;
		}

		return new Player(rlPlayer);
	}

	public static List<Player> getAll() {
		return instance.all(Predicates.always());
	}

	public static List<Player> getAll(Predicate<Player> predicate) {
		return instance.all(predicate);
	}

	public static List<Player> getAll(String... names) {
		return instance.all(names);
	}

	public static Player getNearest(Positionable to, Predicate<Player> filter) {
		return instance.nearest(to, filter);
	}

	public static Player getNearest(Positionable to, String... names) {
		return instance.nearest(to, names);
	}

	public static Player getNearest(Predicate<Player> filter) {
		return instance.nearest(Players.getLocal(), filter);
	}

	public static Player getNearest(String... names) {
		return instance.nearest(Players.getLocal(), names);
	}


}
