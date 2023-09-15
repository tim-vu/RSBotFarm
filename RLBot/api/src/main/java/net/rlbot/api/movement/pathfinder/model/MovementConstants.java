package net.rlbot.api.movement.pathfinder.model;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.runelite.api.ItemID;

public class MovementConstants
{
    public static final Area WILDERNESS_ABOVE_GROUND = Area.rectangular2(2944, 3523, 448, 448, 0);
    public static final Area WILDERNESS_UNDERGROUND = Area.rectangular2(2944, 9918, 320, 442, 0);
    public static Position HOUSE_POINT = new Position(10000, 4000, 1);

    public static final int[] RING_OF_DUELING = new int[]
            {
                    ItemID.RING_OF_DUELING8,
                    ItemID.RING_OF_DUELING7,
                    ItemID.RING_OF_DUELING6,
                    ItemID.RING_OF_DUELING5,
                    ItemID.RING_OF_DUELING4,
                    ItemID.RING_OF_DUELING3,
                    ItemID.RING_OF_DUELING2,
                    ItemID.RING_OF_DUELING1,
            };

    public static final int[] GAMES_NECKLACE = new int[]
            {
                    ItemID.GAMES_NECKLACE8,
                    ItemID.GAMES_NECKLACE7,
                    ItemID.GAMES_NECKLACE6,
                    ItemID.GAMES_NECKLACE5,
                    ItemID.GAMES_NECKLACE4,
                    ItemID.GAMES_NECKLACE3,
                    ItemID.GAMES_NECKLACE2,
                    ItemID.GAMES_NECKLACE1,
            };

    public static final int[] COMBAT_BRACELET = new int[]
            {
                    ItemID.COMBAT_BRACELET1,
                    ItemID.COMBAT_BRACELET2,
                    ItemID.COMBAT_BRACELET3,
                    ItemID.COMBAT_BRACELET4,
                    ItemID.COMBAT_BRACELET5,
                    ItemID.COMBAT_BRACELET6,
            };

    public static final int[] RING_OF_WEALTH = new int[]
            {
                    ItemID.RING_OF_WEALTH_5,
                    ItemID.RING_OF_WEALTH_4,
                    ItemID.RING_OF_WEALTH_3,
                    ItemID.RING_OF_WEALTH_2,
                    ItemID.RING_OF_WEALTH_1,
            };

    public static final int[] AMULET_OF_GLORY = new int[]
            {
                    ItemID.AMULET_OF_GLORY6,
                    ItemID.AMULET_OF_GLORY5,
                    ItemID.AMULET_OF_GLORY4,
                    ItemID.AMULET_OF_GLORY3,
                    ItemID.AMULET_OF_GLORY2,
                    ItemID.AMULET_OF_GLORY1,
            };

    public static final int[] NECKLACE_OF_PASSAGE = new int[]
            {
                    ItemID.NECKLACE_OF_PASSAGE1,
                    ItemID.NECKLACE_OF_PASSAGE2,
                    ItemID.NECKLACE_OF_PASSAGE3,
                    ItemID.NECKLACE_OF_PASSAGE4,
                    ItemID.NECKLACE_OF_PASSAGE5,
            };

    public static final int[] BURNING_AMULET = new int[]
            {
                    ItemID.BURNING_AMULET5,
                    ItemID.BURNING_AMULET4,
                    ItemID.BURNING_AMULET3,
                    ItemID.BURNING_AMULET2,
                    ItemID.BURNING_AMULET1,
            };

    public static final int[] XERICS_TALISMAN = new int[]
            {
                    ItemID.XERICS_TALISMAN
            };

    public static final int[] SLAYER_RING = new int[]
            {
                    ItemID.SLAYER_RING_ETERNAL,
                    ItemID.SLAYER_RING_8,
                    ItemID.SLAYER_RING_7,
                    ItemID.SLAYER_RING_6,
                    ItemID.SLAYER_RING_5,
                    ItemID.SLAYER_RING_4,
                    ItemID.SLAYER_RING_3,
                    ItemID.SLAYER_RING_2,
                    ItemID.SLAYER_RING_1,
            };

    public static final int[] DIGSITE_PENDANT = new int[]
            {
                    ItemID.DIGSITE_PENDANT_5,
                    ItemID.DIGSITE_PENDANT_4,
                    ItemID.DIGSITE_PENDANT_3,
                    ItemID.DIGSITE_PENDANT_2,
                    ItemID.DIGSITE_PENDANT_1,
            };

    public static final int[] DRAKANS_MEDALLION = new int[]
            {
                    ItemID.DRAKANS_MEDALLION
            };

    public static final int[] SKILLS_NECKLACE = new int[]
            {
                    ItemID.SKILLS_NECKLACE6,
                    ItemID.SKILLS_NECKLACE5,
                    ItemID.SKILLS_NECKLACE4,
                    ItemID.SKILLS_NECKLACE3,
                    ItemID.SKILLS_NECKLACE2,
                    ItemID.SKILLS_NECKLACE1,
            };

    public static final int[] TELEPORT_CRYSTAL = new int[]
            {
                    ItemID.ETERNAL_TELEPORT_CRYSTAL,
                    ItemID.TELEPORT_CRYSTAL_5,
                    ItemID.TELEPORT_CRYSTAL_4,
                    ItemID.TELEPORT_CRYSTAL_5,
                    ItemID.TELEPORT_CRYSTAL_2,
                    ItemID.TELEPORT_CRYSTAL_1
            };

    public static final int[] ENCHANTED_LYRE = new int[]
            {
                    ItemID.ENCHANTED_LYREI,
                    ItemID.ENCHANTED_LYRE5,
                    ItemID.ENCHANTED_LYRE4,
                    ItemID.ENCHANTED_LYRE3,
                    ItemID.ENCHANTED_LYRE2,
                    ItemID.ENCHANTED_LYRE1
            };

    public static final int[] SLASH_ITEMS = new int[]
            {
                    ItemID.KNIFE,
                    ItemID.WILDERNESS_SWORD_1,
                    ItemID.WILDERNESS_SWORD_2,
                    ItemID.WILDERNESS_SWORD_3,
                    ItemID.WILDERNESS_SWORD_4,
                    ItemID.IRON_SCIMITAR,
                    ItemID.IRON_LONGSWORD,
                    ItemID.STEEL_SCIMITAR,
                    ItemID.STEEL_LONGSWORD,
                    ItemID.BLACK_SCIMITAR,
                    ItemID.BLACK_LONGSWORD,
                    ItemID.MITHRIL_SCIMITAR,
                    ItemID.MITHRIL_LONGSWORD,
                    ItemID.ADAMANT_SCIMITAR,
                    ItemID.ADAMANT_LONGSWORD,
                    ItemID.RUNE_SCIMITAR,
                    ItemID.RUNE_LONGSWORD
            };

    public static final Position[][] SLASH_WEB_POINTS = {
            { new Position(3031, 3852, 0), new Position(3029, 3852, 0) },
            { new Position(3148, 3727, 0), new Position(3146, 3727, 0) },
            { new Position(3147, 3728, 0), new Position(3147, 3726, 0) },
            { new Position(3164, 3736, 0), new Position(3162, 3736, 0) },
            { new Position(3163, 3737, 0), new Position(3163, 3735, 0) },
            { new Position(3183, 3734, 0), new Position(3183, 3732, 0) },
            { new Position(3158, 3952, 0), new Position(3158, 3950, 0) },
            //Fixed
            { new Position(3210, 9899, 0), new Position(3210, 9898, 0) },
            { new Position(3115, 3860, 0), new Position(3115, 3858, 0) },
            { new Position(3093, 3957, 0), new Position(3091, 3957, 0) },
            { new Position(3096, 3957, 0), new Position(3094, 3957, 0) },
            { new Position(3105, 3959, 0), new Position(3105, 3957, 0) },
            { new Position(3106, 3959, 0), new Position(3106, 3957, 0) },
            { new Position(2654, 9767, 0), new Position(2654, 9765, 0) },
            { new Position(2566, 3124, 0), new Position(2564, 3124, 0) },
            { new Position(2565, 3125, 0), new Position(2565, 3123, 0) },
            { new Position(2569, 3119, 0), new Position(2569, 3117, 0) },
            { new Position(2570, 3119, 0), new Position(2570, 3117, 0) },
            { new Position(2574, 3125, 0), new Position(2574, 3123, 0) },
            { new Position(2631, 9248, 0), new Position(2629, 9248, 0) },
            { new Position(2632, 9264, 0), new Position(2630, 9264, 0) },
            { new Position(2628, 9231, 1), new Position(2628, 9229, 1) },
            { new Position(2629, 9239, 1), new Position(2629, 9237, 1) },
            { new Position(2647, 9118, 0), new Position(2647, 9116, 0) },
            { new Position(2638, 9092, 1), new Position(2638, 9090, 1) },
            { new Position(2653, 9124, 1), new Position(2653, 9122, 1) },
            { new Position(2663, 9110, 1), new Position(2663, 9108, 1) },
            { new Position(2633, 9200, 0), new Position(2633, 9198, 0) },
            { new Position(2646, 9190, 0), new Position(2644, 9190, 0) },
            { new Position(2648, 9199, 0), new Position(2648, 9197, 0) },
            { new Position(2662, 9206, 0), new Position(2662, 9204, 0) },
            { new Position(2666, 9160, 0), new Position(2664, 9160, 0) },
            { new Position(2668, 9194, 0), new Position(2666, 9194, 0) },
            { new Position(2541, 9069, 1), new Position(2539, 9069, 1) },
            { new Position(2547, 9064, 1), new Position(2547, 9062, 1) },
            { new Position(2551, 9054, 1), new Position(2551, 9052, 1) },
            { new Position(2555, 9039, 1), new Position(2553, 9039, 1) },
            { new Position(2604, 9273, 1), new Position(2602, 9273, 1) },
            { new Position(2618, 9211, 1), new Position(2618, 9209, 1) },
            { new Position(2620, 9205, 1), new Position(2620, 9203, 1) },
            { new Position(2571, 9051, 0), new Position(2569, 9051, 0) },
            { new Position(2570, 9052, 0), new Position(2570, 9050, 0) },
            { new Position(2599, 9080, 1), new Position(2597, 9080, 1) },
            { new Position(2608, 9079, 1), new Position(2606, 9079, 1) },
            { new Position(2610, 9047, 1), new Position(2610, 9045, 1) },
            { new Position(2613, 9057, 1), new Position(2613, 9055, 1) },
            { new Position(2619, 9071, 1), new Position(2617, 9071, 1) },
            { new Position(2618, 9072, 1), new Position(2618, 9070, 1) },
            { new Position(2674, 9039, 0), new Position(2674, 9037, 0) },
            { new Position(2633, 9049, 1), new Position(2633, 9047, 1) },
            { new Position(2639, 9062, 1), new Position(2637, 9062, 1) },
            { new Position(2638, 9063, 1), new Position(2638, 9061, 1) },
            { new Position(2645, 9056, 1), new Position(2643, 9056, 1) },
            { new Position(2655, 9073, 1), new Position(2653, 9073, 1) },
            { new Position(2654, 9074, 1), new Position(2654, 9072, 1) },
            { new Position(2657, 9082, 1), new Position(2655, 9082, 1) },
            { new Position(2676, 9074, 1), new Position(2674, 9074, 1) },
            { new Position(2678, 9061, 1), new Position(2678, 9059, 1) },
            { new Position(2678, 9068, 1), new Position(2678, 9066, 1) },
            { new Position(1833, 9945, 0), new Position(1833, 9943, 0) },
            { new Position(1841, 9934, 0), new Position(1841, 9932, 0) },
            { new Position(1843, 9933, 0), new Position(1841, 9933, 0) },
            { new Position(1842, 9934, 0), new Position(1842, 9932, 0) },
            { new Position(1849, 9935, 0), new Position(1849, 9933, 0) },
            { new Position(1850, 9935, 0), new Position(1850, 9933, 0) },
            { new Position(1848, 9919, 0), new Position(1846, 9919, 0) },
            { new Position(1847, 9920, 0), new Position(1847, 9918, 0) }
    };

}
