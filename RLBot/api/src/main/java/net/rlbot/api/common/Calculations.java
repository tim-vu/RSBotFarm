package net.rlbot.api.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.script.Stopwatch;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.movement.Position;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;

import java.time.Duration;

/**
 * Game world and projection calculations.
 */
@Slf4j
public class Calculations {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {
		API_CONTEXT = apiContext;
	}


	/**
	 * Returns the angle to a given tile in degrees anti-clockwise from the
	 * positive x-axis (where the x-axis is from west to east).
	 *
	 * @param position The target tile
	 * @return The angle in degrees
	 */
	public static int positionToAngle(Position position) {

		var player = API_CONTEXT.getClient().getLocalPlayer();

		var me = new Position(
				player.getWorldLocation().getX(),
				player.getWorldLocation().getY(),
				API_CONTEXT.getClient().getPlane()
		);

		int angle = (int) Math.toDegrees(
				Math.atan2(
						position.getY() - me.getY(),
						position.getX() - me.getX()
				)
		);

		return angle >= 0 ? angle : 360 + angle;
	}

	public static final int[] SIN_TABLE = new int[16384];
	public static final int[] COS_TABLE = new int[16384];

	static {
		final double d = 0.00038349519697141029D;
		for (int i = 0; i < 16384; i++) {
			Calculations.SIN_TABLE[i] = (int) (32768D * Math.sin(i * d));
			Calculations.COS_TABLE[i] = (int) (32768D * Math.cos(i * d));
		}
	}

	private static final int SECONDS_IN_MINUTE = 60;

	private static final int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60;

	public static double getHourlyRate(int amount, Duration duration) {
		var totalSeconds = duration.getSeconds();

		return amount / (double)totalSeconds * SECONDS_IN_HOUR;
	}
}
