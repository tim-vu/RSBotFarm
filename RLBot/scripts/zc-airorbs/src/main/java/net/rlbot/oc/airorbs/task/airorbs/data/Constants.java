package net.rlbot.oc.airorbs.task.airorbs.data;

import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Set;

public class Constants {

    public static final Loadout LOADOUT = Loadout.builder()
            .withItem(ItemId.COSMIC_RUNE)
                .amount(81).build()
            .withItem(ItemId.UNPOWERED_ORB)
                .amount(27).build()
            .withEquipmentSet()
                .with(ItemIds.AMULET_OF_GLORY).minimumDose().build()
                .with(ItemId.STAFF_OF_AIR).build()
                .with(ItemIds.RING_OF_DUELING).minimumDose().build()
                .with(ItemId.BLUE_WIZARD_HAT).build()
                .with(ItemId.BLUE_WIZARD_ROBE).build()
                .with(ItemId.BLUE_SKIRT).build()
                .build()
            .build();

    public static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemIds.AMULET_OF_GLORY.get(0), 4),
            new Tradeable(ItemId.AMULET_OF_GLORY, 0),
            new Tradeable(ItemId.STAFF_OF_AIR, 5),
            new Tradeable(ItemIds.RING_OF_DUELING.get(0), 3),
            new Tradeable(ItemIds.RING_OF_WEALTH.get(0), 1),
            new Tradeable(ItemId.RING_OF_WEALTH, 0),
            new Tradeable(ItemId.COSMIC_RUNE, 1620),
            new Tradeable(ItemId.UNPOWERED_ORB, 540),
            new Tradeable(ItemId.AIR_ORB, 0),
            new Tradeable(ItemId.BLUE_WIZARD_HAT, 1),
            new Tradeable(ItemId.BLUE_WIZARD_ROBE, 1),
            new Tradeable(ItemId.BLUE_SKIRT, 1)
    );

    public static final Area OBELISK = Area.rectangular(3084, 3574, 3092, 3567);

    public static final Area EDGEVILLE = Area.rectangular(3069, 3512, 3108, 3457);

    public static final Position OBELISK_CHARGE_POSITION = new Position(3088, 3568, 0);


    public static final Area GRAND_EXCHANGE = Area.surrounding(new Position(3164, 3489, 0), 20);


    public static final int TELEBLOCK_DURATION_MINUTES = 5;

    public static final Area FEROX_ENCLAVE = Area.rectangular(3125, 3639, 3155, 3625);

    public static final Area CHURCH = Area.polygonal(
        new Position(3131, 3640, 0),
        new Position(3131, 3639, 0),
        new Position(3132, 3638, 0),
        new Position(3132, 3635, 0),
        new Position(3131, 3634, 0),
        new Position(3131, 3633, 0),
        new Position(3127, 3633, 0),
        new Position(3127, 3634, 0),
        new Position(3125, 3634, 0),
        new Position(3125, 3638, 0),
        new Position(3127, 3638, 0),
        new Position(3127, 3640, 0)
    );

    public static final int COINS_TO_MULE = 500_000;
}
