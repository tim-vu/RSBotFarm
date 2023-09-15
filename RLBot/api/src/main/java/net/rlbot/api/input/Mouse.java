package net.rlbot.api.input;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.runelite.api.Point;

/**
 * Mouse related operations.
 */
public class Mouse {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {
		API_CONTEXT = apiContext;
	}

	public static void setPosition(int x, int y) {
		API_CONTEXT.getInputManager().setMouse(x, y);
	}

	public synchronized static void click(int x, int y, boolean leftClick) {
		API_CONTEXT.getInputManager().clickMouse(x, y, leftClick);
	}

	public synchronized static void click(Point point, boolean leftClick) {
		API_CONTEXT.getInputManager().clickMouse(point.getX(), point.getY(), leftClick);
	}

}
