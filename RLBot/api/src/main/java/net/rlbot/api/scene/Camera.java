package net.rlbot.api.scene;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.common.Calculations;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.game.Vars;
import net.rlbot.api.movement.Position;
import net.runelite.api.VarClientInt;

public class Camera {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	private static final int MAX_ZOOM_VALUE = 896;
	private static final int MIN_ZOOM_VALUE = 128;

	public static int getZoom() {
		var value =  Vars.getVarcInt(VarClientInt.CAMERA_ZOOM_FIXED_VIEWPORT);
		return 100 * (value - MIN_ZOOM_VALUE) / (MAX_ZOOM_VALUE - MIN_ZOOM_VALUE);
	}

	public static void lookNorth() {
		API_CONTEXT.getClient().runScript(1050, 1);
	}

	public static void setZoom(int percent) {
		var absolute = MIN_ZOOM_VALUE + (percent / 100d) * (MAX_ZOOM_VALUE - MIN_ZOOM_VALUE);
		API_CONTEXT.getClient().runScript(2234, absolute, absolute);
	}

	public static void turnTo(Positionable positionable) {

		turnTo(positionable, 0);
	}

	public static void turnTo(Positionable positionable, int dev) {

		int angle = getAngleTo(positionable.getPosition());
		angle = Random.between(angle - dev, angle + dev + 1);
		setAngle(angle);
	}

	private static int getAngleTo(Position position) {
		int a = (Calculations.positionToAngle(position) - 90) % 360;
		return a < 0 ? a + 360 : a;
	}


	private static final int YAW_MIN_VALUE = 0;
	private static final int YAW_MAX_VALUE = 2048;

	public static void setAngle(int degrees) {
		var yaw = (degrees / 100d) * YAW_MAX_VALUE;
		var pitch = API_CONTEXT.getClient().getCameraPitch();
		API_CONTEXT.getClient().runScript(143, pitch, (int)yaw);
	}

	public static int getAngle() {
		return (int) Math.abs(API_CONTEXT.getClient().getCameraYaw() / 45.51 * 8);
	}
	private static final int MIN_PITCH_VALUE = 128;
	private static final int MAX_PITCH_VALUE = 383;

	public static void setPitch(int percent) {
		var pitchValue = MIN_PITCH_VALUE + (percent / 100d) * (MAX_PITCH_VALUE - MIN_PITCH_VALUE);
		var yaw = API_CONTEXT.getClient().getCameraYaw();
		API_CONTEXT.getClient().runScript(143, (int)pitchValue, yaw);
	}

	public static int getPitch() {
		var pitch = API_CONTEXT.getClient().getCameraPitch();
		return (int)Math.round((100d * (pitch - MIN_PITCH_VALUE) / (double)(MAX_PITCH_VALUE - MIN_PITCH_VALUE)));
	}

	public static int getX() {

		return API_CONTEXT.getClient().getCameraX();
	}

	public static int getY() {

		return API_CONTEXT.getClient().getCameraY();
	}

	public static int getZ() {

		return API_CONTEXT.getClient().getCameraZ();
	}

}
