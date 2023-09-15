package tasks.combat.common.combattask.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.InventorySupply;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
public enum MonsterArea {

    LUMBRIDGE_BARN_1(
            Area.polygonal(
                    new Position(3225, 3295, 0),
                    new Position(3231, 3295, 0),
                    new Position(3231, 3287, 0),
                    new Position(3237, 3287, 0),
                    new Position(3237, 3290, 0),
                    new Position(3238, 3291, 0),
                    new Position(3238, 3293, 0),
                    new Position(3237, 3294, 0),
                    new Position(3237, 3297, 0),
                    new Position(3238, 3298, 0),
                    new Position(3238, 3300, 0),
                    new Position(3236, 3302, 0),
                    new Position(3226, 3302, 0),
                    new Position(3225, 3301, 0)
            ),
            Set.of("Chicken")
    ),
    LUMBRIDGE_BARN_2(
            Area.polygonal(
                    new Position(3185, 3280, 0),
                    new Position(3192, 3280, 0),
                    new Position(3193, 3279, 0),
                    new Position(3193, 3276, 0),
                    new Position(3184, 3276, 0),
                    new Position(3184, 3279, 0)
            ),
            Set.of("Chicken")
    ),
    LUMBRIDGE_FIELD_EAST(
            Area.polygonal(
                    new Position(3241, 3299, 0),
                    new Position(3256, 3299, 0),
                    new Position(3257, 3300, 0),
                    new Position(3261, 3300, 0),
                    new Position(3262, 3299, 0),
                    new Position(3264, 3299, 0),
                    new Position(3266, 3297, 0),
                    new Position(3266, 3255, 0),
                    new Position(3253, 3255, 0),
                    new Position(3253, 3272, 0),
                    new Position(3251, 3274, 0),
                    new Position(3251, 3276, 0),
                    new Position(3249, 3278, 0),
                    new Position(3246, 3278, 0),
                    new Position(3244, 3280, 0),
                    new Position(3244, 3281, 0),
                    new Position(3240, 3285, 0),
                    new Position(3240, 3287, 0),
                    new Position(3241, 3288, 0),
                    new Position(3241, 3289, 0),
                    new Position(3242, 3290, 0),
                    new Position(3242, 3293, 0),
                    new Position(3241, 3294, 0),
                    new Position(3241, 3295, 0),
                    new Position(3240, 3296, 0),
                    new Position(3240, 3298, 0)
            ),
            Set.of("Cow")
    ),
    LUMBRIDGE_FIELD_MID(
            Area.polygonal(
                    new Position(3195, 3303, 0),
                    new Position(3200, 3303, 0),
                    new Position(3201, 3302, 0),
                    new Position(3205, 3302, 0),
                    new Position(3206, 3303, 0),
                    new Position(3210, 3303, 0),
                    new Position(3211, 3302, 0),
                    new Position(3211, 3297, 0),
                    new Position(3212, 3296, 0),
                    new Position(3212, 3295, 0),
                    new Position(3214, 3293, 0),
                    new Position(3214, 3290, 0),
                    new Position(3213, 3289, 0),
                    new Position(3213, 3285, 0),
                    new Position(3212, 3284, 0),
                    new Position(3207, 3284, 0),
                    new Position(3206, 3283, 0),
                    new Position(3201, 3283, 0),
                    new Position(3200, 3282, 0),
                    new Position(3196, 3282, 0),
                    new Position(3195, 3283, 0),
                    new Position(3195, 3284, 0),
                    new Position(3193, 3286, 0),
                    new Position(3193, 3301, 0)
            ),
            Set.of("Cow")
    ),
    LUMBRIDGE_FIELD_NORTH(
            Area.polygonal(
                    new Position(3176, 3316, 0),
                    new Position(3178, 3316, 0),
                    new Position(3180, 3314, 0),
                    new Position(3185, 3314, 0),
                    new Position(3189, 3310, 0),
                    new Position(3190, 3310, 0),
                    new Position(3192, 3308, 0),
                    new Position(3195, 3308, 0),
                    new Position(3195, 3332, 0),
                    new Position(3184, 3339, 0),
                    new Position(3179, 3342, 0),
                    new Position(3156, 3342, 0),
                    new Position(3156, 3314, 0),
                    new Position(3161, 3314, 0),
                    new Position(3165, 3318, 0),
                    new Position(3169, 3318, 0),
                    new Position(3171, 3316, 0)
            ),
            Set.of("Cow")
    ),
    VARROCK_SEWER_HOBGOLBIN(
            Area.polygonal(
                    new Position(3121, 9882, 0),
                    new Position(3126, 9882, 0),
                    new Position(3130, 9878, 0),
                    new Position(3130, 9872, 0),
                    new Position(3115, 9872, 0),
                    new Position(3115, 9875, 0),
                    new Position(3121, 9875, 0)
            ),
            Set.of("Hobgoblin"),
            Set.of(InventorySupply.builder(ItemId.BRASS_KEY).build()),
            Set.of(new Tradeable(ItemId.BRASS_KEY, 1)),
            Set.of(),
            false
    ),
    BARBARIAN_VILLAGE(
            Area.rectangular(3075, 3445, 3082, 3436),
            Set.of("Barbarian")
    ),
    LUMBRIDGE_SWAMP_FROGS(
            Area.rectangular(3192, 3191, 3209, 3169),
            Set.of("Giant frog")
    ),
    VARROCK_SEWERS_MOSS_GIANTS(
            Area.rectangular(3152, 9910, 3167, 9893),
            Set.of("Moss giant"),
            Set.of(InventorySupply.builder(ItemId.KNIFE).build()),
            Set.of(new Tradeable(ItemId.KNIFE, 1)),
            Set.of(ItemId.MOSSY_KEY),
            true
    ),
    VARROCK_SEWERS_HILL_GIANTS(
            Area.rectangular(3094, 9854, 3126, 9822),
            Set.of("Hill Giant"),
            Set.of(InventorySupply.builder(ItemId.BRASS_KEY).build()),
            Set.of(new Tradeable(ItemId.BRASS_KEY, 1)),
            Set.of(ItemId.GIANT_KEY),
            true
    ),
    CATACOMB_OF_FAMINE_FLESH_CRAWLERS(
            Area.rectangular(1985, 5246, 2046, 5185),
            Set.of("Flesh Crawler"),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            false
    ),
    CATABOMB_OF_FAMINE_ZOMBIE(
            Area.rectangular(1985, 5246, 2046, 5185),
            Set.of("Zombie"),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            false
    );

    @Getter
    private final Area area;

    @Getter
    private final Set<String> targetNames;

    @Getter
    private final Set<InventorySupply> inventorySupplies;

    @Getter
    private final Set<Tradeable> tradeables;

    @Getter
    private final Set<Integer> lootItemIds;

    @Getter
    private final boolean buryBones;

    MonsterArea(Area area, Set<String> targetNames) {
        this.area = area;
        this.targetNames = targetNames;
        this.inventorySupplies = Collections.emptySet();
        this.tradeables = Collections.emptySet();
        this.lootItemIds = Collections.emptySet();
        this.buryBones = false;
    }
}
