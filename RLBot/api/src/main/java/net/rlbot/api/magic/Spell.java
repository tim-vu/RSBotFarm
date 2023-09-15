package net.rlbot.api.magic;


import net.rlbot.api.widgets.WidgetInfo;

import java.util.Set;

public interface Spell {

    int getLevel();

    WidgetInfo getWidgetInfo();

    int getAutocastIndex();

    boolean canCast();

    Set<RuneRequirement> getRuneRequirements();
}
