package net.rlbot.api;

import lombok.NonNull;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.*;
import net.rlbot.api.event.EventDispatcher;
import net.rlbot.api.game.Vars;
import net.rlbot.internal.ApiContext;
import net.rlbot.internal.wrapper.ClientImpl;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Players;
import net.runelite.client.ui.DrawManager;

import java.util.Arrays;

/**
 * Game state and GUI operations.
 */
public class Game {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	public static ClientImpl getClient() {

		return API_CONTEXT.getClient();
	}

	public static EventDispatcher getEventDispatcher() {
		return API_CONTEXT.getEventDispatcher();
	}

	public static void runScript(Object... args) {

		API_CONTEXT.getClient().runScript(args);
	}

	public static boolean isInInstancedRegion() {

		return API_CONTEXT.getClient().isInInstancedRegion();
	}

	public static boolean logout() {

		if(!Tabs.isOpen(Tab.LOG_OUT)) {

			Tabs.open(Tab.LOG_OUT);

			if(!Time.sleepUntil(() -> Tabs.isOpen(Tab.LOG_OUT), 600)) {
				return false;
			}
		}

		var widget = Widgets.get(WidgetID.LOGOUT_PANEL_ID, 8);

		if(!Widgets.isVisible(widget)) {
			return false;
		}

		return widget.interact("Logout") && Time.sleepUntil(() -> !Game.isLoggedIn(), 1200);
	}

	private static final int MEMBER_DAYS_VARP = 1780;

	public static int getMembershipDays() {

		return Vars.getVarp(MEMBER_DAYS_VARP);
	}

	public static DrawManager getDrawManager() {

		return API_CONTEXT.getDrawManager();
	}

	public static World getWorld() {

		var world = Arrays.stream(API_CONTEXT.getClient().getWorldList())
				.filter(w -> w.getId() == API_CONTEXT.getClient().getWorld())
				.findFirst()
				.orElse(null);

		if(world == null) {
			return null;
		}

		return new World(world);
	}

	public static boolean setWorld(World world) {

		var targetWorld = Arrays.stream(API_CONTEXT.getClient().getWorldList())
				.filter(w -> w.getId() == world.getId())
				.findFirst()
				.orElse(null);

		if(targetWorld == null) {
			return false;
		}

		API_CONTEXT.getClient().changeWorld(targetWorld);
		return true;
	}

	public static int getWildyLevel() {

		var wildyLevelWidget = Widgets.get(WidgetInfo.PVP_WILDERNESS_LEVEL);
		if (!Widgets.isVisible(wildyLevelWidget)) {
			return 0;
		}

		// Dmm
		if (wildyLevelWidget.getText().contains("Guarded")
				|| wildyLevelWidget.getText().contains("Protection")) {
			return 0;
		}

		if (wildyLevelWidget.getText().contains("Deadman")) {
			return Integer.MAX_VALUE;
		}
		String widgetText = wildyLevelWidget.getText();
		if (widgetText.equals("")) {
			return 0;
		}
		if (widgetText.equals("Level: --")) {
			var player = Players.getLocal();
			return 2 + (player.getY() - 3528) / 8;
		}
		String levelText = widgetText.contains("<br>") ? widgetText.substring(0, widgetText.indexOf("<br>")) : widgetText;
		return Integer.parseInt(levelText.replace("Level: ", ""));
	}

	private static final int CUTSCENE_VARBIT = 542;

	public static boolean isInCutscene() {

		return Vars.getBit(CUTSCENE_VARBIT) > 0;
	}

	public static int getLoginIndex() {

		return API_CONTEXT.getClient().getLoginIndex();
	}

	public static int getCurrentLoginField() {

		return API_CONTEXT.getClient().getCurrentLoginField();
	}

	public static boolean isLoggedIn() {

		var gameState = getState();

		return gameState == net.rlbot.api.GameState.LOGGED_IN || gameState == net.rlbot.api.GameState.LOADING || gameState == net.rlbot.api.GameState.HOPPING || gameState == net.rlbot.api.GameState.CONNECTION_LOST;
	}

	public static boolean isOnLoginScreen() {

		var gameState = getState();

		return gameState == GameState.LOGIN_SCREEN
				|| gameState == GameState.LOGIN_SCREEN_AUTHENTICATOR
				|| gameState == GameState.LOGGING_IN;
	}

	public static boolean isLoadingRegion() {

		var gameState = getState();

		return gameState == GameState.LOADING || gameState == GameState.HOPPING;
	}

	public static void setUsername(String username) {

		API_CONTEXT.getClient().setUsername(username);
	}

	public static void setPassword(String password) {

		API_CONTEXT.getClient().setPassword(password);
	}

	public static GameState getState() {

		return GameState.of(API_CONTEXT.getClient().getGameState().getState());
	}

	public static int getPlane() {

		return API_CONTEXT.getClient().getPlane();
	}

	public static int getBaseX() {

		return API_CONTEXT.getClient().getBaseX();
	}

	public static int getBaseY() {

		return API_CONTEXT.getClient().getBaseY();
	}

	public static Position getMapBase() {

		return new Position(
				API_CONTEXT.getClient().getBaseX(),
				API_CONTEXT.getClient().getBaseY(),
				API_CONTEXT.getClient().getPlane()
		);
	}

	public static byte[][][] getSceneFlags() {

		return API_CONTEXT.getClient().getTileSettings();
	}

	public static int getWidth() {

		return API_CONTEXT.getClient().getCanvas().getWidth();
	}

	public static int getHeight() {

		return API_CONTEXT.getClient().getCanvas().getHeight();
	}

}
