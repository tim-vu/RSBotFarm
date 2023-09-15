package tasks.prayer.gildedaltar.data;

import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.tree.Key;
import tasks.prayer.gildedaltar.enums.Bone;

public class Keys {

    public static final Key<Bone> BONE = new Key<>();

    public static final Key<Loadout> LOADOUT = new Key<>();

    public static final Key<TempMemory<Boolean>> HAS_LOADOUT = new Key<>();

}
