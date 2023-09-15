package net.rlbot.api.scene;

import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.movement.Position;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.coords.WorldPoint;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.*;
import java.util.function.Predicate;

public class Tiles
{

	private static ApiContext API_CONTEXT;

	private static void init(ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	public static List<Tile> 	getAll(Predicate<Tile> filter) {

		List<Tile> out = new ArrayList<>();

		for (int x = 0; x < Constants.SCENE_SIZE; x++) {
			for (int y = 0; y < Constants.SCENE_SIZE; y++) {

				var rTile = API_CONTEXT.getClient().getScene().getTiles()[API_CONTEXT.getClient().getPlane()][x][y];

				if(rTile == null) {
					continue;
				}

				var tile = new Tile(rTile);

				if (!filter.test(tile)) {
					continue;
				}

				out.add(tile);
			}
		}

		return out;
	}

	public static List<Tile> getAll() {

		return getAll(x -> true);
	}

	@Nullable
	public static Tile getAt(Position position) {

		return getAt(position.getX(), position.getY(), position.getPlane());
	}

	@Nullable
	public static Tile getAt(int worldX, int worldY, int plane) {

		Client client = API_CONTEXT.getClient();
		int correctedX = worldX < Constants.SCENE_SIZE ? worldX + client.getBaseX() : worldX;
		int correctedY = worldY < Constants.SCENE_SIZE ? worldY + client.getBaseY() : worldY;

		if (!WorldPoint.isInScene(client, correctedX, correctedY)) {
			return null;
		}

		int x = correctedX - client.getBaseX();
		int y = correctedY - client.getBaseY();

		return new Tile(client.getScene().getTiles()[plane][x][y]);
	}

	public static List<Tile> getSurrounding(Position position, int radius) {

		List<Tile> out = new ArrayList<>();

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {

				out.add(getAt(position.getX(), position.getY(), position.getPlane()));

			}
		}

		return out;
	}

	@Nullable
	public static Tile getHoveredTile() {
		var tile = API_CONTEXT.getClient().getSelectedSceneTile();

		if(tile == null) {
			return null;
		}

		return new Tile(tile);
	}
}
