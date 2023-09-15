package net.rlbot.api.adapter.scene;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.TileEntity;
import net.rlbot.api.definitions.Definitions;
import net.rlbot.api.definitions.ObjectDefinition;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.SceneEntity;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.ObjectPackets;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.internal.Interaction;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.geometry.Shapes;
import net.runelite.api.geometry.SimplePolygon;
import org.locationtech.jts.awt.ShapeReader;
import org.locationtech.jts.awt.ShapeWriter;
import org.locationtech.jts.geom.GeometryFactory;

import java.awt.*;
import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public abstract class SceneObject implements TileEntity {

	protected static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	protected final TileObject object;

	@Getter
	private final int id;

	private final ObjectDefinition def;

	protected SceneObject(@NonNull TileObject object) {

		this.object = object;
		this.id = this.object.getId();

 		var def = Definitions.getObjectDefinition(object.getId());

		if(def == null) {
			this.def = null;
			return;
		}

		if (def.getImpostorIds() == null || def.getImpostor() == null) {
			this.def = def;
			return;
		}

		this.def = def.getImpostor();
	}

	public int getX() {
		return this.object.getWorldLocation().getX();
	}

	public int getY() {
		return this.object.getWorldLocation().getY();
	}

	public int getPlane() {
		return this.object.getWorldLocation().getPlane();
	}

	public Position getPosition() {
		var position = this.object.getWorldLocation();
		return new Position(position.getX(), position.getY(), position.getPlane());
	}


	public Area getArea() {
		return Area.singular(getPosition());
	}

	public boolean hasAction(@NonNull String... actions) {
		var objectActions = getActions();

		for (String s : objectActions) {

			for(String a : actions) {
				if (!a.equalsIgnoreCase(s)) {
					continue;
				}

				return true;
			}

			return false;
		}

		return false;
	}

	public String[] getActions() {
		return this.def.getActions();
	}

	public String getName() {

		return this.def.getName();
	}

	public Point getMenuPoint()
	{
		if (this.object instanceof GameObject temp)
		{
			var point = temp.getSceneMinLocation();
			return new Point(point.getX(), point.getY());
		}

		var local = this.object.getLocalLocation();
		return new Point(local.getSceneX(), local.getSceneY());
	}

	@Override
	public boolean interact(int index) {

		int num = index + 1;

		if(num < 1 || num > 5) {
			return false;
		}

		var point = getMenuPoint();
		LocalPoint lp = new LocalPoint(point.x, point.y);
		WorldPoint wp = WorldPoint.fromScene(API_CONTEXT.getClient(), lp.getX(), lp.getY(), getPlane());

		Interaction.log("OBJECT", kv("id", id));
		MousePackets.queueClickPacket();
		ObjectPackets.queueAction(num, object.getId(), wp.getX(), wp.getY(), false);
		return true;
	}

	@Override
	public boolean equals(java.lang.Object o) {

		return (o instanceof SceneObject) && ((SceneObject) o).object == this.object;
	}

	@Override
	public int hashCode() {

		return this.object.hashCode();
	}
}
