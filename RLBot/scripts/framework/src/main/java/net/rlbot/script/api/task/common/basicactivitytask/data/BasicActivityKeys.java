package net.rlbot.script.api.task.common.basicactivitytask.data;

import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.tree.Key;

public class BasicActivityKeys {

    public static final Key<Area> TRAINING_AREA = new Key<>();

    public static final Key<Loadout> LOADOUT = new Key<>();

    public static final Key<TempMemory<Boolean>> HAS_LOADOUT = new Key<>();
}
