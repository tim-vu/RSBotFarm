package net.rlbot.api.movement.pathfinder;

import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.model.Transport;
import net.rlbot.api.movement.position.Area;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static net.rlbot.api.movement.pathfinder.model.MovementConstants.WILDERNESS_ABOVE_GROUND;
import static net.rlbot.api.movement.pathfinder.model.MovementConstants.WILDERNESS_UNDERGROUND;

@Data
@Slf4j
public class Pathfinder implements Callable<List<Position>>
{

	final CollisionMap map;

	final Map<Position, List<Transport>> transports;

	private List<Node> start;

	private Area target;

	private Set<Position> targetPositions;

	private final List<Node> boundary = new LinkedList<>();

	private final Set<Position> visited = new HashSet<>();

	private Node nearest;

	boolean avoidWilderness;


	private static boolean isInWilderness(Position position) {

		return WILDERNESS_ABOVE_GROUND.contains(position) || WILDERNESS_UNDERGROUND.contains(position);
	}

	public Pathfinder(CollisionMap collisionMap, Map<Position, List<Transport>> transports, List<Position> start, Position target, boolean avoidWilderness) {

		this(collisionMap, transports, start, Area.rectangular(target, target), avoidWilderness);
	}

	public Pathfinder(CollisionMap collisionMap, Map<Position, List<Transport>> transports, List<Position> start, Area target, boolean avoidWilderness) {

		this.map = collisionMap;
		this.transports = transports;
		this.target = target;
		this.targetPositions = new HashSet<>(target.getPositions());
		this.start = new ArrayList<>();
		this.start.addAll(start.stream().map(point -> new Node(point, null)).toList());
		this.nearest = null;
		this.avoidWilderness = avoidWilderness;
			if (targetPositions.stream().allMatch(t -> collisionMap.fullBlock(t.getX(), t.getY(), t.getPlane()))) {
			log.warn("Walking to a {}, pathfinder will be slow", targetPositions.size() == 1 ? "blocked tile" : "fully blocked area");
		}
	}

	private void addNeighbors(Node node) {

		int x = node.position.getX();
		int y = node.position.getY();
		int plane = node.position.getPlane();

		if (map.w(x, y, plane)) {
			addNeighbor(node, new Position(x - 1, y, plane));
		}

		if (map.e(x, y, plane)) {
			addNeighbor(node, new Position(x + 1, y, plane));
		}

		if (map.s(x, y, plane)) {
			addNeighbor(node, new Position(x, y - 1, plane));
		}

		if (map.n(x, y, plane)) {
			addNeighbor(node, new Position(x, y + 1, plane));
		}

		if (map.sw(x, y, plane)) {
			addNeighbor(node, new Position(x - 1, y - 1, plane));
		}

		if (map.se(x, y, plane)) {
			addNeighbor(node, new Position(x + 1, y - 1, plane));
		}

		if (map.nw(x, y, plane)) {
			addNeighbor(node, new Position(x - 1, y + 1, plane));
		}

		if (map.ne(x, y, plane)) {
			addNeighbor(node, new Position(x + 1, y + 1, plane));
		}

		for (var transport : transports.getOrDefault(node.position, new ArrayList<>())) {
			addNeighbor(node, transport.getDestination());
		}
	}

	private void addNeighbor(Node node, Position neighbor) {

		if (avoidWilderness && isInWilderness(neighbor) && !isInWilderness(node.position) && targetPositions.stream().noneMatch(Pathfinder::isInWilderness)) {
			return;
		}

		if (!visited.add(neighbor)) {
			return;
		}

		boundary.add(new Node(neighbor, node));
	}

	public List<Position> find() {

		long startTime = System.currentTimeMillis();
		List<Position> path = find(5_000_000);
		String targetStr = target.getCenter().toString();
		log.debug("Path calculation took {} ms to {}", System.currentTimeMillis() - startTime, targetStr);
		return path;
	}

	public List<Position> find(int maxSearch) {

		boundary.addAll(start);

		int bestDistance = Integer.MAX_VALUE;

		boolean withinTarget = false;

		while (!boundary.isEmpty()) {
			if (Thread.interrupted()) {
				return List.of();
			}

			if (visited.size() >= maxSearch) {
				return nearest.path();
			}

			Node node = boundary.remove(0);

			visited.add(node.position);

			if(withinTarget) {

				if(!targetPositions.contains(node.position)) {
					continue;
				}

				if(node.position.equals(target.getCenter())) {
					return node.path();
				}
			}

			if(!withinTarget && targetPositions.contains(node.position)) {
				withinTarget = true;
			}

			int distance = (int)target.distanceTo(node.position);
			if (distance < bestDistance) {
				nearest = node;
				bestDistance = distance;
			}

			addNeighbors(node);
		}

		if (nearest != null) {
			return nearest.path();
		}
		return List.of();
	}

	@Override
	public List<Position> call() throws Exception {

		return find();
	}

	@Value
	private static class Node {

		Position position;

		Node previous;


		public List<Position> path() {

			List<Position> path = new LinkedList<>();
			Node node = this;

			while (node != null) {
				path.add(0, node.position);
				node = node.previous;
			}

			return new ArrayList<>(path);
		}

	}
}
