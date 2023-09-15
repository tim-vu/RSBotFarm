package net.rlbot.oc.airorbs.task.airorbs.data;

import net.rlbot.api.widgets.World;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.tree.Key;
import net.rlbot.script.farm.api.MulingApi;
import net.rlbot.script.farm.api.model.MulingRequestVm;

import java.time.LocalDateTime;
import java.util.List;

public class Keys {

    public static final Key<MulingRequestVm> MULING_REQUEST = new Key<>();

    public static final Key<MulingApi> MULING_API = new Key<>();

    public static final Key<Boolean> HAS_GOLD = new Key<>();

    public static final Key<LocalDateTime> HITSPLAT_TIMESTAMP = new Key<>();

    public static final Key<LocalDateTime> TELEBLOCK_TIMESTAMP = new Key<>();

    public static final Key<Boolean> IS_RESTOCKING = new Key<>();

    public static final Key<List<String>> DANGEROUS_PLAYERS = new Key<>();

    public static final Key<Boolean> IS_CHARGING = new Key<>();

    public static final Key<Statistics> STATISTICS = new Key<>();

    public static final Key<TempMemory<Boolean>> HAS_LOADOUT = new Key<>();

    public static final Key<World.Region> WORLD_REGION = new Key<>();

}
