package net.rlbot.api.game;

import lombok.Getter;
import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.widgets.Tabs;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Tab;
import net.rlbot.api.adapter.component.Widget;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Combat related operations.
 */
public class Combat {

	private static ApiContext API_CONTEXT;

	private static void init(@NonNull ApiContext apiContext) {

		API_CONTEXT = apiContext;
	}

	private static final int SPEC_VARP = 301;
	private static final int SPEC_ENERGY_VARP = 300;
	private static final Supplier<Widget> SPEC_BUTTON = () -> Widgets.get(593, 36);

	private static final int VENOM_THRESHOLD = 1000000;

	private static final int ANTIFIRE = 3981;

	private static final int SUPER_ANTIFIRE = 6101;

	public static boolean isRetaliating()
	{
		return Vars.getVarp(VarPlayer.AUTO_RETALIATE.getId()) == 0;
	}

	public static boolean isPoisoned()
	{
		return Vars.getVarp(VarPlayer.POISON.getId()) > 0;
	}

	public static boolean isVenomed()
	{
		return Vars.getVarp(VarPlayer.POISON.getId()) >= VENOM_THRESHOLD;
	}

	public static boolean isSpecEnabled()
	{
		return Vars.getVarp(SPEC_VARP) == 1;
	}

	public static int getSpecEnergy()
	{
		return Vars.getVarp(SPEC_ENERGY_VARP) / 10;
	}

	public static boolean isAntifired()
	{
		return Vars.getBit(ANTIFIRE) > 0;
	}

	public static boolean isSuperAntifired()
	{
		return Vars.getBit(SUPER_ANTIFIRE) > 0;
	}

	public static boolean toggleSpec()
	{
		if (isSpecEnabled())
		{
			return false;
		}

		Widget spec = SPEC_BUTTON.get();

		if (spec == null) {
			return false;
		}

		//TODO: verify
		return spec.interact("Special attack");
	}

	public static boolean setAutoRetaliate(boolean enable) {

		if(isAutoRetaliateEnabled() == enable) {
			return false;
		}

		if(!Tabs.isOpen(Tab.COMBAT)) {
			Tabs.open(Tab.COMBAT);
		}

		var autoRetaliate = Widgets.get(WidgetInfo.COMBAT_AUTO_RETALIATE);

		if(!Widgets.isVisible(autoRetaliate)) {
			return false;
		}

		return autoRetaliate.interact("Auto retaliate");
	}

	/**
	 * Returns whether the auto-retaliate option is enabled.
	 *
	 * @return <code>true</code> if retaliate is enabled; otherwise <code>false</code>.
	 */
	public static boolean isAutoRetaliateEnabled() {

		return Vars.getVarp(VarPlayer.AUTO_RETALIATE.getId()) == 0;
	}

	public static boolean setAttackStyle(@NonNull AttackStyle attackStyle)
	{
		if (attackStyle.getWidgetInfo() == null)
		{
			return false;
		}

		var widget = Widgets.get(attackStyle.widgetInfo);

		if (widget == null) {
			return false;
		}

		return widget.interact(widget.getActions()[0]);
	}

	public static AttackStyle getAttackStyle()
	{
		return AttackStyle.fromIndex(Vars.getVarp(43));
	}

	public static int getWildernessLevel() {

		Widget widget = Widgets.get(WidgetInfo.PVP_WILDERNESS_LEVEL);

		if (!Widgets.isVisible(widget)) {
			return 0;
		}

		String levelText = widget.getText().replace("Level: ", "").trim();

		if (levelText.contains("<")) {
			levelText = levelText.substring(0, levelText.indexOf("<"));
		}
		// In Clan Wars the level is 0

		if (levelText.equals("--")) {
			return 0;
		}

		return Integer.parseInt(levelText);
	}

	public enum AttackStyle
	{
		FIRST(0, WidgetInfo.COMBAT_STYLE_ONE),
		SECOND(1, WidgetInfo.COMBAT_STYLE_TWO),
		THIRD(2, WidgetInfo.COMBAT_STYLE_THREE),
		FOURTH(3, WidgetInfo.COMBAT_STYLE_FOUR),
		SPELLS(4, WidgetInfo.COMBAT_SPELL_BOX),
		SPELLS_DEFENSIVE(4, WidgetInfo.COMBAT_DEFENSIVE_SPELL_BOX),

		UNKNOWN(-1, null);

		@Getter
		private final int index;

		@Getter
		private final WidgetInfo widgetInfo;

		AttackStyle(int index, WidgetInfo widgetInfo)
		{
			this.index = index;
			this.widgetInfo = widgetInfo;
		}

		public static AttackStyle fromIndex(int index)
		{
			return Arrays.stream(values()).filter(x -> x.index == index)
					.findFirst()
					.orElse(UNKNOWN);
		}
	}
}
