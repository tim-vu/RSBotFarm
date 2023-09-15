package net.rlbot.script.api.restocking.data;

import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.tree.Key;

public class RestockingKeys {

    public static final Key<Boolean> IS_RESTOCKING = new Key<>();

    public static final Key<RestockingSettings> SETTINGS = new Key<>();

    public static final Key<Boolean> IS_OUT_OF_COINS = new Key<>();

    public static final Key<Boolean> IS_WITHDRAWING = new Key<>();

    public static final Key<Boolean> IS_BUYING = new Key<>();

}
