package net.rlbot.api.movement.pathfinder;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.adapter.scene.WallObject;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.MovementPackets;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.movement.pathfinder.model.Teleport;
import net.rlbot.api.movement.pathfinder.model.Transport;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.movement.position.RectangularArea;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.scene.Tiles;
import net.rlbot.internal.Interaction;
import net.runelite.api.NpcID;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Singleton
@Slf4j
public class Walker
{

	public static final int MAX_INTERACT_DISTANCE = 10;

	private static final int MIN_TILES_WALKED_IN_STEP = 9;

	private static final int MAX_TILES_WALKED_IN_STEP = 20;

	private static final int MAX_MIN_ENERGY = 50;

	private static final int MIN_ENERGY = 5;

	private static final int MAX_NEAREST_SEARCH_ITERATIONS = 10;

	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	public static Future<List<Position>> pathFuture = null;

	private static Area currentDestination = null;

	public static boolean walkTo(Position destination) {

		return walkTo(Area.rectangular(destination, destination));
	}

	public static boolean walkTo(Area destination) {

		var player = Players.getLocal();

		if (Game.isInCutscene() || Widgets.isVisible(299, 0)) {
			Time.sleepTicks(2);
			return true;
		}

		Map<Position, List<Transport>> transports = buildTransportLinks();
		LinkedHashMap<Position, Teleport> teleports = buildTeleportLinks(destination);
		List<Position> path = buildPath(destination);

		if (path == null || path.isEmpty()) {
			log.error(path == null ? "Path is null" : "Path is empty");
			return false;
		}

		Position startPosition = path.get(0);
		Teleport teleport = teleports.get(startPosition);
		Position localWP = player.getPosition();
		boolean offPath = path.stream().noneMatch(t -> t.distanceTo(localWP) <= 5 && canPathTo(localWP, t));

		// Teleport or refresh path if our direction changed
		if (offPath) {

			if (teleport != null) {
				log.debug("Casting teleport");
				return teleport.getHandler().getAsBoolean();
			}

			path = buildPath(destination.getCenter(), true);
			log.debug("Refreshed path {}", path.size() - 1);
		}

		var result =  walkAlong(path, transports);

		if (destination.contains(player)) {
			log.debug("Setting current destination to null");
			currentDestination = null;
		}

		return result;
	}

	public static boolean walkAlong(List<Position> path) {
		return walkAlong(path, Collections.emptyMap());
	}

	public static boolean walkAlong(List<Position> path, Map<Position, List<Transport>> transports) {

		List<Position> remainingPath = remainingPath(path);

		var pathAction = getNextPathAction(remainingPath, transports);

		if(pathAction != null) {

			if(pathAction.getDistance() <= MAX_INTERACT_DISTANCE) {

				log.debug(pathAction.getMessage());
				if(!pathAction.getHandler().getAsBoolean()) {
					log.warn("PathAction failed");
					return false;
				}

				return true;
			}
		}

		var pathActionDistance = pathAction == null ? Integer.MAX_VALUE : pathAction.getDistance();

		return stepAlong(remainingPath, pathActionDistance);
	}

	public static boolean stepAlong(List<Position> path, int maxDistance) {

		var destination = Movement.getDestination();

		if(destination != null && (destination.distance() > 5 || destination.equals(path.get(path.size() - 1)))) {
			return true;
		}

		List<Position> reachablePath = reachablePath(path);

		if (reachablePath.isEmpty()) {
			return false;
		}

		int nextTileIdx = reachablePath.size() - 1;
		if (nextTileIdx <= MIN_TILES_WALKED_IN_STEP) {
			return step(reachablePath.get(nextTileIdx));
		}

		var max = Math.min(MAX_TILES_WALKED_IN_STEP, maxDistance);

		if (nextTileIdx > max) {
			nextTileIdx = max;
		}

		int targetDistance = Random.between(MIN_TILES_WALKED_IN_STEP, nextTileIdx);
		return step(reachablePath.get(targetDistance));
	}

	public static List<Position> reachablePath(List<Position> remainingPath) {

		var local = Players.getLocal();

		List<Position> out = new ArrayList<>();
		for (Position p : remainingPath) {

			var tile = Tiles.getAt(p);

			if (tile == null) {
				break;
			}

			out.add(p);
		}

		if (out.isEmpty() || out.size() == 1 && out.get(0).equals(local.getPosition())) {
			return Collections.emptyList();
		}

		return out;
	}

	public static boolean step(Position destination) {

		var local = Players.getLocal();

		log.debug("Stepping towards " + destination);

		walk(destination);

		if (local.getPosition().equals(destination)) {
			log.debug("Walkflag at destination");
			return true;
		}

		if (!Movement.isRunEnabled() && (Movement.getRunEnergy() >= Random.between(MIN_ENERGY, MAX_MIN_ENERGY) || (local.getHealthScale() > -1 && Movement.getRunEnergy() > 0))) {
			Movement.toggleRun();
			Time.sleepUntil(Movement::isRunEnabled, 2000);
			return true;
		}

		if (!Movement.isRunEnabled() && Movement.getRunEnergy() > 0 && Movement.isStaminaBoosted()) {
			Movement.toggleRun();
			Time.sleepUntil(Movement::isRunEnabled, 2000);
			return true;
		}

		// Handles when stuck on those trees next to draynor manor
		if (!local.isMoving()) {
			var tree = Npcs.getNearest(n -> n.getId() == NpcID.TREE_4416 && n.getTarget().equals(local) && n.getPosition().distanceTo(local) <= 1);

			if (tree != null) {
				var area = Area.rectangular2(local.getX() - 2, local.getY() - 2, 3, 3, local.getPlane());
				area.getPositions().stream()
						.filter(wp -> !wp.equals(local.getPosition()) && !wp.equals(tree.getPosition()) && canPathTo(local.getPosition(), wp))
						.unordered()
						.min(Comparator.comparingDouble(wp -> wp.distanceTo(tree)))
						.ifPresent(Walker::walk);
				return false;
			}
		}

		return true;
	}

	private static PathAction getNextPathAction(List<Position> path, Map<Position, List<Transport>> transports) {

		if(Dialog.canContinue()) {
			return new PathAction("Continuing dialog", 0, () -> {
				if(!Dialog.continueSpace()) {
					log.warn("Failed to continue dialogue");
					Time.sleepTick();
					return false;
				}

				return true;
			});
		}

		// Wilderness ditch warning
		if (Constants.WILDY_DITCH_WARNING.isWidgetVisible()) {
			return new PathAction("Handling the wilderness ditch warning", 0, () -> {

				var warning = Constants.WILDY_DITCH_WARNING.resolve();

				if(!Widgets.isVisible(warning)) {
					log.warn("Unable to find the Wilderness warning widget");
					return false;
				}

				if(!warning.interact("Enter Wilderness") || !Time.sleepUntil(() -> !Constants.WILDY_DITCH_WARNING.isWidgetVisible(), 1200)) {
					log.warn("Failed to close the Wilderness warning widget");
					return true;
				}

				return true;
			});
		}

		if(Dialog.isViewingOptions()) {

			var title = Dialog.getOptionsTitle();
			if(title != null && title.contains("Continue through the Barrier")) {
				return new PathAction("Handling the ferox enclave warning", 0, () -> {

					if(!Dialog.chooseOption(1) || !Time.sleepUntil(() -> Game.getWildyLevel() > 0, 2400)) {
						log.warn("Failed to handle the dialog");
						Time.sleepTick();
						return false;
					}

					return true;
				});
			}

			if (Dialog.getOptions().stream().anyMatch(o -> o.contains("Eeep! The Wilderness"))) {
				return new PathAction("Handling the wilderness warning", 0, () -> {

					if(!Dialog.isOpen()) {
						log.warn("Wilderness warning dialog not open");
						return true;
					}

					if(!Dialog.chooseOption("Yes") ||
							!Time.sleepUntil(() -> !Dialog.isOpen() && Game.getWildyLevel() > 0, () -> Players.getLocal().isAnimating(), 4800)) {
						log.warn("Failed to close the Wilderness warning widget");
						Time.sleepTick();
						return false;
					}

					return true;
				});
			}

		}

		for (int i = 0; i < MAX_TILES_WALKED_IN_STEP; i++) {

			if (i + 1 >= path.size()) {
				break;
			}

			Position a = path.get(i);
			Position b = path.get(i + 1);

			var tileA = Tiles.getAt(a);
			var tileB = Tiles.getAt(b);

			if (a.distanceTo(b) > 1 || (tileA != null && tileB != null && Reachable.getCost(b) > 15)) {

				Transport transport = transports.getOrDefault(a, List.of()).stream()
						.filter(x -> x.getSource().equals(a) && x.getDestination().equals(b))
						.findFirst()
						.orElse(null);

				if (transport != null) {

					return new PathAction(String.format("Using transport at %s to move %s -> %s", transport.getSource(), a, b), i, () -> {

						if (!transport.getHandler().getAsBoolean()) {
							log.debug("Handling transport failed");
							return false;
						}

						return true;
					});
				}
			}

			// MLM Rocks
			var rockfall = SceneObjects.query()
					.locations(a)
					.names("Rockfall")
					.results()
					.first();

			var hasPickaxe = Inventory.contains(Predicates.nameContains("pickaxe")) ||
					Equipment.contains(Predicates.nameContains("pickaxe"));

			if (rockfall != null && hasPickaxe) {
				return new PathAction("Handling MLM rockfall", i, () -> {

					if (Players.getLocal().isMoving()) {
						return true;
					}

					//TODO: SleepUntil rock is gone
					return rockfall.interact("Mine");
				});
			}

			if (tileA == null) {
				return null;
			}

			// Diagonal door bullshit
			if (Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() + b.getY()) > 1 && a.getPlane() == b.getPlane()) {

				var wall = SceneObjects.getAt(a, it -> !(it instanceof WallObject) && it.getName() != null && it.getName().equals("Door"))
						.stream()
						.findFirst()
						.orElse(null);

				if (wall != null && wall.hasAction("Open")) {

					return new PathAction(String.format("Handling door at %s", wall.getPosition()), i, () -> {

						var door = SceneObjects.query()
								.locations(a)
								.actions("Open")
								.results()
								.first();

						if(!door.interact("Open") ||
								!Time.sleepUntil(
										() -> isGameObjectDoorOpen(a),
										() -> Players.getLocal().isMoving(), 1200))
						{
							log.debug("Handle door at {} failed", wall.getPosition());
							Time.sleepTick();
							return false;
						}

						return true;
					});
				}
			}

			if (tileB == null) {
				return null;
			}

			// Normal doors
			if (Reachable.isDoored(tileA, tileB)) {
				return handleDoorAction(a, i);
			}

			if (Reachable.isDoored(tileB, tileA)) {
				return handleDoorAction(b, i);
			}
		}

		return null;
	}

	private static PathAction handleDoorAction(Position position, int distance) {

		return new PathAction(String.format("Handling door at %s", position), distance, () -> {

			var tile = Tiles.getAt(position);
			WallObject door;

			if(tile == null || (door = tile.getWallObject()) == null) {
				return false;
			}

			if(!door.interact("Open") || !Time.sleepUntil(
					() -> isDoorOpen(position) || Constants.WILDY_DITCH_WARNING.isWidgetVisible(),
					() -> Players.getLocal().isMoving(), 1200))
			{
				log.warn("Handle door failed");
				Time.sleepTick();
				return false;
			}

			return true;
		});
	}

	private static PathAction handleGameObjectDoor(Position position, int distance) {
		return new PathAction(String.format("Handling special door at %s", position), distance, () -> {

			var door = SceneObjects.query()
					.locations(position)
					.nameContains("door")
					.actions("Open")
					.results()
					.first();

			if(door == null) {
				log.warn("Unable to find door");
				return false;
			}

			if(!door.interact("Open") || !Time.sleepUntil(
					() -> isGameObjectDoorOpen(position),
					() -> Players.getLocal().isMoving(), 1200))
			{
				log.warn("Handle door failed");
				return false;
			}

			return true;
		});
	}

	private static boolean isDoorOpen(Position position) {
		var tile = Tiles.getAt(position);

		if(tile == null) {
			return true;
		}

		var door = tile.getWallObject();

		return door == null || !door.hasAction("Open");
	}



	private static boolean isGameObjectDoorOpen(Position position) {

		return SceneObjects.query()
				.locations(position)
				.filter(o -> !(o instanceof WallObject))
				.nameContains("door")
				.actions("Open")
				.results()
				.isEmpty();
	}

	private static WallObject getWallObjectAt(Position position) {
		var tile = Tiles.getAt(position);

		if(tile == null) {
			return null;
		}

		return tile.getWallObject();
	}

	@SneakyThrows
	public static Position nearestWalkableTile(Position source, Predicate<Position> filter) {

		CollisionMap cm = GlobalCollisionMap.fromResources();

		if (!cm.fullBlock(source) && filter.test(source)) {
			return source;
		}

		int currentIteration = 1;
		for (int radius = currentIteration; radius < MAX_NEAREST_SEARCH_ITERATIONS; radius++) {
			for (int x = -radius; x < radius; x++) {
				for (int y = -radius; y < radius; y++) {
					Position p = source.dx(x).dy(y);
					if (cm.fullBlock(p) || !filter.test(p)) {
						continue;
					}
					return p;
				}
			}
		}
		log.debug("Could not find a walkable tile near {}", source);
		return null;
	}

	public static Position nearestWalkableTile(Position source) {

		return nearestWalkableTile(source, x -> true);
	}

	public static List<Position> remainingPath(List<Position> path) {

		var local = Players.getLocal();

		var nearest = path.stream()
				.filter(p -> p.getPlane() == local.getPlane())
				.min(Comparator.comparingDouble(x -> x.distanceTo(local.getPosition())))
				.orElse(null);

		if (nearest == null) {
			return Collections.emptyList();
		}

		return path.subList(path.indexOf(nearest), path.size());
	}

	public static List<Position> calculatePath(RectangularArea destination) {

		var local = Players.getLocal();
		LinkedHashMap<Position, Teleport> teleports = buildTeleportLinks(destination);
		List<Position> startPoints = new ArrayList<>(teleports.keySet());
		startPoints.add(local.getPosition());
		return calculatePath(startPoints, destination);
	}

	@SneakyThrows
	public static List<Position> calculatePath(List<Position> startPoints, RectangularArea destination) {

		return new Pathfinder(GlobalCollisionMap.fromResources(), buildTransportLinks(), startPoints, destination, false).find();
	}

	public static List<Position> calculatePath(Position destination) {

		return calculatePath(Area.singular(destination));
	}

	public static List<Position> calculatePath(List<Position> startPoints, Position destination) {

		return calculatePath(startPoints, Area.singular(destination));
	}

	@SneakyThrows
	private static List<Position> buildPath(
			List<Position> startPoints,
			Area destination,
			boolean avoidWilderness,
			boolean forced
	) {

		if (pathFuture == null) {
			pathFuture = executor.submit(new Pathfinder(GlobalCollisionMap.fromResources(), buildTransportLinks(), startPoints, destination, avoidWilderness));
			currentDestination = destination;
		}

		boolean sameDestination = destination.equals(currentDestination);
		if (!sameDestination || forced) {
			pathFuture.cancel(true);
			pathFuture = executor.submit(new Pathfinder(GlobalCollisionMap.fromResources(), buildTransportLinks(), startPoints, destination, avoidWilderness));
			currentDestination = destination;
		}

		try {
			return pathFuture.get();
		} catch (Exception e) {
			log.debug("Path is loading");
			return List.of();
		}
	}

	public static List<Position> buildPath() {

		if (currentDestination == null) {
			return List.of();
		}
		return buildPath(currentDestination);
	}

	public static List<Position> buildPath(Area destination, boolean avoidWilderness, boolean forced) {

		var local = Players.getLocal();
		LinkedHashMap<Position, Teleport> teleports = buildTeleportLinks(destination);
		List<Position> startPoints = new ArrayList<>(teleports.keySet());
		startPoints.add(local.getPosition());

		return buildPath(startPoints, destination, avoidWilderness, forced);
	}

	public static List<Position> buildPath(Area destination) {

		return buildPath(destination, false, false);
	}

	public static List<Position> buildPath(Position destination) {

		return buildPath(Area.singular(destination));
	}

	public static List<Position> buildPath(Position destination, boolean forced) {

		return buildPath(Area.singular(destination), false, forced);
	}

	public static List<Position> buildPath(Position destination, boolean avoidWilderness, boolean forced) {

		return buildPath(Area.singular(destination), avoidWilderness, forced);
	}

	public static List<Position> buildPath(List<Position> startPoints, Position destination, boolean avoidWilderness, boolean forced) {

		return buildPath(startPoints, Area.singular(destination), avoidWilderness, forced);
	}

	public static Map<Position, List<Transport>> buildTransportLinks() {

		Map<Position, List<Transport>> out = new HashMap<>();

		for (Transport transport : TransportLoader.buildTransports()) {
			out.computeIfAbsent(transport.getSource(), x -> new ArrayList<>()).add(transport);
		}

		return out;
	}

	public static LinkedHashMap<Position, Teleport> buildTeleportLinks(Area destination) {

		var out = new LinkedHashMap<Position, Teleport>();

		var local = Players.getLocal();

		for (Teleport teleport : TeleportLoader.buildTeleports()) {
			if (teleport.getDestination().distanceTo(local) > 50
					&& destination.distanceTo(local) > destination.distanceTo(teleport.getDestination()) + 20) {
				out.putIfAbsent(teleport.getDestination(), teleport);
			}
		}

		return out;
	}

	public static Map<Position, List<Transport>> buildTransportLinksOnPath(List<Position> path) {

		Map<Position, List<Transport>> out = new HashMap<>();
		for (Transport transport : TransportLoader.buildTransports()) {
			Position destination = transport.getDestination();
			if (path.contains(destination)) {
				out.computeIfAbsent(transport.getSource(), x -> new ArrayList<>()).add(transport);
			}
		}
		return out;
	}

	public static LinkedHashMap<Position, Teleport> buildTeleportLinksOnPath(List<Position> path) {

		LinkedHashMap<Position, Teleport> out = new LinkedHashMap<>();
		for (Teleport teleport : TeleportLoader.buildTeleports()) {
			Position destination = teleport.getDestination();
			if (path.contains(destination)) {
				out.putIfAbsent(destination, teleport);
			}
		}
		return out;
	}

	public static boolean canPathTo(Position start, Position destination) {

		List<Position> pathTo = start.pathTo(destination);
		return pathTo != null && pathTo.contains(destination);
	}

	public static boolean walk(Position position) {

		var local = Players.getLocal();

		var destinationTile = Tiles.getAt(position);
		// Check if tile is in loaded client scene

		Position finalPosition;

		if (destinationTile == null) {
			log.debug("Destination {} is not in scene", position);
			var nearestInScene = Tiles.getAll()
					.stream()
					.min(Comparator.comparingDouble(x -> x.distanceTo(local)))
					.orElse(null);

			if (nearestInScene == null) {
				log.debug("Couldn't find nearest walkable tile");
				return false;
			}

			finalPosition = nearestInScene.getPosition();
		} else {
			finalPosition = position;
		}

		Interaction.log("MOVE", kv("destination", position));
		MousePackets.queueClickPacket();
		MovementPackets.queueMovement(position.getX(), position.getY(), false);
		return Time.sleepUntil(() -> finalPosition.equals(Movement.getDestination()), 1200);
	}

	@Value
	private static class PathAction {
		String message;
		int distance;
		BooleanSupplier handler;
	}

}
