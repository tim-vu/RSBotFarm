package tasks.mining.powermining.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.data.ObjectId;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

@AllArgsConstructor
public enum Rock implements Predicate<SceneObject> {

    COPPER(1, Set.of(436), Set.of(ObjectId.COPPER_ROCKS_10943, ObjectId.COPPER_ROCKS_11161)),
    TIN(1, Set.of(438), Set.of(ObjectId.TIN_ROCKS_11360, ObjectId.TIN_ROCKS_11361)),
    IRON(15, Set.of(440), Set.of(ObjectId.IRON_ROCKS, ObjectId.IRON_ROCKS_11365, ObjectId.IRON_ROCKS_36203)),
    COAL(30, Set.of(453), Set.of(ObjectId.COAL_ROCKS_11366, ObjectId.COAL_ROCKS_11367, ObjectId.COAL_ROCKS_36204)),
    GOLD(40, Set.of(444), Set.of(ObjectId.SILVER_ROCKS, ObjectId.SILVER_ROCKS_11369, ObjectId.SILVER_ROCKS_36205)),
    MITHRIL(55, Set.of(447), Set.of(ObjectId.MITHRIL_ROCKS, ObjectId.MITHRIL_ROCKS_11373, ObjectId.MITHRIL_ROCKS_36207)),
    ADAMANTITE(70, Set.of(449), Set.of(ObjectId.ADAMANTITE_ROCKS, ObjectId.ADAMANTITE_ROCKS_11375, ObjectId.ADAMANTITE_ROCKS_36208)),
    RUNITE(82, Set.of(451), Set.of(ObjectId.RUNITE_ROCKS, ObjectId.RUNITE_ROCKS_11377, ObjectId.RUNITE_ROCKS_36209));

    @Getter
    private final int requiredMiningLevel;

    public Set<Integer> getOreItemIds() {
        return Collections.unmodifiableSet(oreItemIds);
    }

    private final Set<Integer> oreItemIds;

    public Set<Integer> getObjectIds() {
        return Collections.unmodifiableSet(objectIds);
    }

    private final Set<Integer> objectIds;

    @Override
    public boolean test(SceneObject sceneObject) {
        return getRock(sceneObject) == this;
    }

    public static final Predicate<Item> IS_ORE = item -> {

        for(Rock rock : Rock.values()){

            if(rock.getOreItemIds().contains(item.getId()))
                return true;

        }

        return false;
    };

    public static boolean isRockAvailable(Area area){
        return !SceneObjects.query().within(area).filter(Rock::isRock).results().isEmpty();
    }

    public static boolean isRock(SceneObject sceneObject){
        return getRock(sceneObject) != null;
    }

    public static Rock getRock(Position position){

        var object = SceneObjects.getFirstAt(position);

        if(object == null) {
            return null;
        }

        return getRock(object);
    }

    public static Rock getRock(SceneObject object) {

        if (object == null || !object.getName().endsWith("rocks"))
        {
            return null;
        }

        for (Rock rock : Rock.values()) {

            if (!rock.getObjectIds().contains(object.getId())) {
                continue;
            }

            return rock;
        }

        return null;
    }
}
