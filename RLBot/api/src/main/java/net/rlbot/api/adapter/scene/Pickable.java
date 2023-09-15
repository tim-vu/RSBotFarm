package net.rlbot.api.adapter.scene;

import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.definitions.Definitions;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.PickablePackets;
import net.rlbot.api.scene.Tiles;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.*;
import net.rlbot.api.common.math.Random;
import net.rlbot.internal.Interaction;
import net.rlbot.internal.menu.Menu;
import net.rlbot.api.movement.Position;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.Perspective;
import net.runelite.api.TileItem;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.geometry.Shapes;
import net.runelite.api.geometry.SimplePolygon;

import java.awt.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@ToString
public class Pickable implements TileEntity, BaseItem {

	private static ApiContext API_CONTEXT;
	private static void init(@NonNull ApiContext apiContext) {
		API_CONTEXT = apiContext;
	}

	private static final String[] ACTIONS = new String[]{null, null, "Take", null, null};

	private final TileItem tileItem;

	private final Position position;

	private final ItemDefinition def;

	public Pickable(final Position position, final TileItem tileItem) {
		this.position = position;
		this.tileItem = tileItem;
		this.def = Definitions.getItemDefinition(tileItem.getId());
	}

	@Override
	public String[] getActions() {
		return ACTIONS;
	}

	@Override
	public boolean interact(int index) {

		var num = index + 1;

		if(num < 1 || num > 5) {
			return false;
		}

		Interaction.log("PICKABLE", kv("menuIndex", index), kv("itemId", this.getId()));
		MousePackets.queueClickPacket();
		PickablePackets.queueAction(num, tileItem.getId(), this.getX(), this.getY(), false);
		return true;
	}

	public int getX() {
		return this.position.getX();
	}

	public int getY() {
		return this.position.getY();
	}

	public int getPlane() {
		return this.position.getPlane();
	}

	public Position getPosition() {

		return this.position;
	}

	public Tile getTile() {
		return Tiles.getAt(position);
	}

	@Override
	public String getName() {
		return this.def.getName();
	}

	@Override
	public int getId() {
		return this.tileItem.getId();
	}

	public boolean isTradeable() {
		return this.def.isTradeable();
	}

	public boolean isStackable() {
		return this.def.isStackable();
	}

	public boolean isNoted() {
		return this.def.isNoted();
	}

	public boolean isMembers() {
		return this.def.isMembers();
	}

	public int getStorePrice() {
		return this.def.getPrice();
	}

	@Override
	public int getQuantity() {
		return tileItem.getQuantity();
	}

}