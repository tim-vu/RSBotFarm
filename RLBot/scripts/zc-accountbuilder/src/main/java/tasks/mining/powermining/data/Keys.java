package tasks.mining.powermining.data;

import net.rlbot.script.api.tree.Key;
import tasks.mining.powermining.enums.Pickaxe;
import tasks.mining.powermining.enums.Rock;

public class Keys {

    public static final Key<Cluster> CLUSTER = new Key<>();

    public static final Key<Rock> ROCK = new Key<>();

    public static final Key<Boolean> IS_DROPPING = new Key<>();

    public static final Key<Pickaxe> PICKAXE = new Key<>();

    public static final Key<Boolean> STOP = new Key<>();

    public static final Key<Boolean> STOPPED = new Key<>();
}
