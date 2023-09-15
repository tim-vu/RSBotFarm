package net.rlbot.api.game;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.widgets.Tabs;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Tab;
import net.rlbot.api.adapter.component.Widget;
import net.runelite.api.Varbits;

import java.util.Arrays;

public class Prayers {

	public static boolean isEnabled(@NonNull Prayer prayer) {

		return Vars.getBit(prayer.getVarbit()) == 1;
	}

	public static boolean toggle(@NonNull Prayer prayer) {

		Widget widget = Widgets.get(prayer.getWidgetInfo());

		if (widget == null) {
			return false;
		}

		return widget.interact("Toggle");
	}

	public static int getPoints() {
		return Skills.getBoostedLevel(Skill.PRAYER);
	}

	public static boolean toggleQuickPrayer(boolean enabled) {

		Widget widget = Widgets.get(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
		if (widget == null) {
			return false;
		}

		return widget.interact(enabled ? "Activate" : "Deactivate");
	}

	public static boolean isQuickPrayerEnabled() {

		return Vars.getBit(Varbits.QUICK_PRAYER) == 1;
	}

	public static boolean anyActive() {

		return Arrays.stream(Prayer.values()).anyMatch(Prayers::isEnabled);
	}
}
