package net.rlbot.api.definitions;

import net.rlbot.internal.ApiContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Definitions {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final Map<Integer, ItemDefinition> ITEM_ID_TO_DEFINITION = new HashMap<>();

    public static ItemDefinition getItemDefinition(int itemId) {
        return getOrUseCachedValue(itemId, ITEM_ID_TO_DEFINITION, id -> new ItemDefinition(API_CONTEXT.getClient().getItemDefinition(id)));
    }

    private static final Map<Integer, ObjectDefinition> OBJECT_ID_TO_DEFINITION = new HashMap<>();

    public static ObjectDefinition getObjectDefinition(int objectId) {
        return getOrUseCachedValue(objectId, OBJECT_ID_TO_DEFINITION, id -> {
            var def = API_CONTEXT.getClient().getObjectDefinition(id);

            if(def == null) {
                return null;
            }

            return new ObjectDefinition(def);
        });
    }

    private static final Map<Integer, NpcDefinition> NPC_ID_TO_DEFINITION = new HashMap<>();

    public static NpcDefinition getNpcDefinition(int npcId) {
        return getOrUseCachedValue(npcId, NPC_ID_TO_DEFINITION, id -> {
            var def = API_CONTEXT.getClient().getNpcDefinition(id);

            if(def == null) {
                return null;
            }

            return new NpcDefinition(def);
        });
    }

    private static <T> T getOrUseCachedValue(int id, Map<Integer, T> cache, Function<Integer, T> getter) {

        var value = cache.getOrDefault(id, null);

        if(value != null) {
            return value;
        }

        value = getter.apply(id);
        cache.put(id, value);

        return value;
    }
}
