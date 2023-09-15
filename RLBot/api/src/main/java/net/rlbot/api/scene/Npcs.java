package net.rlbot.api.scene;

import lombok.NonNull;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.entities.NpcQuery;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.adapter.scene.Npc;
import net.runelite.api.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Npcs extends SceneEntities<Npc> {

    private static Npcs instance;

    private static ApiContext apiContext;

    private Npcs() {
    }

    private static void init(@NonNull ApiContext apiContext) {
        Npcs.apiContext = apiContext;
        instance = new Npcs();
    }

    @Override
    protected List<Npc> all(Predicate<Npc> filter) {
        var rNpcs = apiContext.getClient().getNpcs();

        var result = new ArrayList<Npc>(rNpcs.size());

        for (NPC rNpc : rNpcs) {

            var npcs = new Npc(rNpc);

            if (filter.test(npcs)) {
                result.add(npcs);
            }
        }

        return result;
    }

    public static NpcQuery query() {
        return new NpcQuery(Npcs::getAll);
    }

    public static List<Npc> getAll()
    {
        return instance.all(Predicates.always());
    }

    public static List<Npc> getAll(Predicate<Npc> filter)
    {
        return instance.all(filter);
    }

    public static List<Npc> getAll(String... names) {
        return instance.all(names);
    }

    public static List<Npc> getAll(int... ids) {
        return instance.all(ids);
    }

    public static Npc getNearest(Positionable to, Predicate<Npc> filter) {
        return instance.nearest(to, filter);
    }

    public static Npc getNearest(Positionable to, String... names) {
        return instance.nearest(to, names);
    }

    public static Npc getNearest(Positionable to, int... ids) {
        return instance.nearest(to, ids);
    }

    public static Npc getNearest(Predicate<Npc> filter) {
        return instance.nearest(Players.getLocal(), filter);
    }

    public static Npc getNearest(String... names) {
        return instance.nearest(Players.getLocal(), names);
    }

    public static Npc getNearest(int... ids) {
        return instance.nearest(Players.getLocal(), ids);
    }
}
