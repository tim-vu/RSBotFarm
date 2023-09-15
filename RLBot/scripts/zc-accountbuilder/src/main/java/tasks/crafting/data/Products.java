package tasks.crafting.data;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Set;

public class Products {

    public static final Product LEATHER_GLOVES = new LeatherProduct(false, 1, 3);

    private static final int INVENTORIES = 10;

    public static final Product GOLD_RINGS = new JewelleryProduct(
            ItemId.GOLD_RING,
            Loadout.builder()
                    .withItem(ItemId.RING_MOULD).build()
                    .withItem(ItemId.GOLD_BAR).amount(1, 27).build()
                    .build(),
            Set.of(
                    new Tradeable(ItemId.RING_MOULD, 1),
                    new Tradeable(ItemId.GOLD_BAR, 27 * INVENTORIES)
            ),
            3
    );

    public static final Product GOLD_AMULETS = new JewelleryProduct(
            ItemId.GOLD_AMULET_U,
            Loadout.builder()
                    .withItem(ItemId.AMULET_MOULD).build()
                    .withItem(ItemId.GOLD_BAR).amount(1, 27).build()
                    .build(),
            Set.of(
                    new Tradeable(ItemId.AMULET_MOULD, 1),
                    new Tradeable(ItemId.GOLD_BAR, 27 * INVENTORIES)
            ),
            3
    );

    public static final Product EMERALD_AMULETS = new JewelleryProduct(
            ItemId.EMERALD_AMULET_U,
            Loadout.builder()
                    .withItem(ItemId.AMULET_MOULD).build()
                    .withItem(ItemId.GOLD_BAR).amount(1, 13).build()
                    .withItem(ItemId.EMERALD).amount(1, 13).build()
                    .build(),
            Set.of(
                    new Tradeable(ItemId.AMULET_MOULD, 1),
                    new Tradeable(ItemId.GOLD_BAR, 13 * INVENTORIES),
                    new Tradeable(ItemId.EMERALD, 13 * INVENTORIES)
            ),
            3
    );
}