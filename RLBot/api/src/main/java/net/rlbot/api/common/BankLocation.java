package net.rlbot.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.common.Positionable;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Players;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;

@AllArgsConstructor
public enum BankLocation
{
    //FIXED
    LUMBRIDGE_BANK(Area.rectangular(3206, 3223, 3211, 3214, 2)),

    //FIXED
    VARROCK_WEST_BANK(Area.rectangular(3179, 3448, 3191, 3432)),

    //FIXED
    VARROCK_EAST_BANK(Area.rectangular(3250, 3424, 3257, 3416)),

    //FIXED
    GRAND_EXCHANGE_BANK(Area.surrounding(new Position(3164, 3489, 0), 10)),

    //FIXED
    EDGEVILLE_BANK(Area.polygonal(
            new Position(3091, 3500, 0),
            new Position(3099, 3500, 0),
            new Position(3099, 3488, 0),
            new Position(3091, 3488, 0),
            new Position(3091, 3493, 0),
            new Position(3090, 3494, 0),
            new Position(3090, 3497, 0),
            new Position(3091, 3498, 0))),
    FALADOR_EAST_BANK(Area.rectangular(3009, 3355, 13, 4, 0)),
    FALADOR_WEST_BANK(Area.polygonal(
            new Position(2943, 3374, 0),
            new Position(2948, 3374, 0),
            new Position(2948, 3370, 0),
            new Position(2950, 3370, 0),
            new Position(2950, 3368, 0),
            new Position(2943, 3368, 0)
    )),

    //FIXED
    DRAYNOR_BANK(Area.rectangular(3088, 3246, 3097, 3240)),
    DUEL_ARENA_BANK(Area.rectangular(3380, 3267, 3385, 3274, 0)),
    CLAN_WARS(Area.rectangular(3361, 3172, 3366, 3170)),
    SHANTAY_PASS_BANK(Area.rectangular(3299, 3118, 3310, 3128, 0)),
    AL_KHARID_BANK(Area.rectangular(3265, 3173, 3272, 3161)),
    CATHERBY_BANK(Area.rectangular(2806, 3438, 2813, 3242, 0)),
    SEERS_VILLAGE_BANK(Area.rectangular(2721, 3487, 2731, 3494, 0)),
    ARDOUGNE_NORTH_BANK(Area.rectangular(2612, 3330, 2622, 3336, 0)),
    ARDOUGNE_SOUTH_BANK(Area.rectangular(2649, 3280, 2656, 3288, 0)),
    PORT_KHAZARD_BANK(Area.rectangular(2658, 3156, 2665, 3165, 0)),
    YANILLE_BANK(Area.rectangular(2609, 3088, 2615, 3098, 0)),
    CORSAIR_COVE_BANK(Area.rectangular(2567, 2862, 2574, 2871, 0)),
    CASTLE_WARS_BANK(Area.rectangular(2435, 3081, 2447, 3099, 0)),
    LLETYA_BANK(Area.rectangular(2349, 3160, 2357, 3167, 0)),
    GRAND_TREE_WEST_BANK(Area.rectangular(2436, 3484, 2445, 3492, 1)),
    GRAND_TREE_SOUTH_BANK(Area.rectangular(2448, 3476, 2456, 3484, 1)),
    TREE_GNOME_STRONGHOLD_BANK(Area.rectangular(2441, 3414, 2451, 3437, 1)),
    SHILO_VILLAGE_BANK(Area.rectangular(2842, 2951, 2862, 2959, 0)),
    NEITIZNOT_BANK(Area.rectangular(2334, 3805, 2340, 3807, 0)),
    JATIZSO_BANK(Area.rectangular(2413, 3798, 2420, 3800, 0)),
    BARBARIAN_OUTPOST_BANK(Area.rectangular(2532, 3570, 2538, 3780, 0)),
    //	ETCETERIA_BANK(Area.rectangular(2618, 3893, 4, 4, 0)), has quest requirements
    DARKMEYER_BANK(Area.rectangular(3601, 3365, 3610, 3368, 0)),
    CHARCOAL_BURNERS_BANK(Area.rectangular(1711, 3460, 1725, 3470, 0)),
    HOSIDIUS_BANK(Area.rectangular(1748, 3594, 1753, 3602, 0)),
    PORT_PISCARILIUS_BANK(Area.rectangular(1794, 3784, 1812, 3791, 0)),
    //	HALLOWED_SEPULCHRE_BANK(Area.rectangular(2393, 5975, 15, 15, 0)), has quest requirements
    CANIFIS_BANK(Area.rectangular(3508, 3474, 3514, 3484, 0)),
    //	MOTHERLODE_MINE_BANK(Area.rectangular(3754, 5664, 4, 3, 0)), has pickaxe requirement
    BURGH_DE_ROTT_BANK(Area.rectangular(3492, 3208, 3502, 3214, 0)),
    VER_SINHAZA_BANK(Area.rectangular(3646, 3204, 3556, 3217, 0)),

    //FIXED
    FEROX_ENCLAVE_BANK(Area.rectangular(3127, 3633, 3137, 3627, 0));

    @Getter
    private final Area area;

    @NotNull
    public static BankLocation getNearest()
    {
        var position = Players.getLocal().getPosition();
        return Arrays.stream(values())
                .min(Comparator.comparingDouble(x -> position.distanceTo(x.getArea())))
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
