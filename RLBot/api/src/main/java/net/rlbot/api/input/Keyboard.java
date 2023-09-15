package net.rlbot.api.input;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;

import java.awt.event.KeyEvent;

/**
 * Keyboard related operations.
 */
public class Keyboard {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	public static void sendText(final String text, final boolean pressEnter) {

		API_CONTEXT.getInputManager().sendKeys(text, pressEnter);
	}

	public static void sendEscape() {
		API_CONTEXT.getInputManager().sendKey((char) KeyEvent.VK_ESCAPE);
	}

	public static void sendKey(char key) {
		API_CONTEXT.getInputManager().sendKey(key);
	}

	public static void sendBackspace() {
		API_CONTEXT.getInputManager().sendKey((char)KeyEvent.VK_BACK_SPACE);
	}

	public static void sendSpace() {
		API_CONTEXT.getInputManager().sendKey((char) KeyEvent.VK_SPACE);
	}
}
