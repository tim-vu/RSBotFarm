package net.rlbot.api.movement;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Vars;
import net.rlbot.api.movement.pathfinder.Walker;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.MovementPackets;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.Tiles;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.internal.ApiContext;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.BooleanSupplier;

/**
 * Walking related operations.
 */
@Slf4j
public class Movement {


	private static Position lastPosition;

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	private static final int RUN_VARP = 173;

	public static boolean walk(Positionable positionable) {
		return Walker.walk(positionable.getPosition());
	}

	public static boolean walkTo(Positionable positionable) {
		return walkTo(Area.singular(positionable));
	}

	public static boolean walkTo(Area area) {
		var result = Walker.walkTo(area);

		if(!result) {
			log.warn("Failed to walk");
			Time.sleepTick();
			return false;
		}

		return true;
	}

	public static boolean toggleRun() {

		var widget = Widgets.get(WidgetInfo.MINIMAP_TOGGLE_RUN_ORB);

		if(widget == null) {
			return false;
		}

		return widget.interact("Toggle Run");
	}

	public static boolean setRun(final boolean enable) {

		if (isRunEnabled() != enable) {
			return toggleRun();
		}

        return false;
    }

	public static boolean isRunEnabled() {

		return Vars.getVarp(RUN_VARP) == 1;
	}

	public static int getRunEnergy() {

		return API_CONTEXT.getClient().getEnergy() / 100;
	}

	private static final int STAMINA_VARBIT = 25;

	public static boolean isStaminaBoosted() {

		return Vars.getBit(STAMINA_VARBIT) == 1;
	}

	@Nullable
	public static Position getDestination() {

		LocalPoint local = API_CONTEXT.getClient().getLocalDestinationLocation();

		if(local == null) {
			return null;
		}

		var worldPoint = WorldPoint.fromLocal(API_CONTEXT.getClient(), local);
		return new Position(worldPoint.getX(), worldPoint.getY(), worldPoint.getPlane());
	}

	public static double getDestinationDistance() {

		var destination = getDestination();

		if(destination == null) {
			return 0;
		}

		var player = Players.getLocal();
		return player.getPosition().distanceTo(destination);
	}
}
