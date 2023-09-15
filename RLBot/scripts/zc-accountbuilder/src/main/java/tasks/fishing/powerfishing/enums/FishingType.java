package tasks.fishing.powerfishing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
public enum FishingType {

    NET_FISHING(
            1,
            Loadout.builder()
                        .withItem(ItemId.SMALL_FISHING_NET).build()
                    .build(),
            Set.of(new Tradeable(ItemId.SMALL_FISHING_NET, 1))
    ),
    FLY_FISHING(
            20,
            Loadout.builder()
                        .withItem(ItemId.FLY_FISHING_ROD).build()
                        .withItem(ItemId.FEATHER).amount(1, Integer.MAX_VALUE).build()
                    .build(),
            Set.of(new Tradeable(ItemId.FLY_FISHING_ROD, 1), new Tradeable(ItemId.FEATHER, 1000))
    ),
    BARBARIAN_FISHING(
            48,
            Loadout.builder()
                                .withEquipmentSet()
                                        .with(ItemId.BARBARIAN_ROD).build()
                                        .with(ItemIds.RING_OF_WEALTH).build()
                                        .with(ItemIds.GAMES_NECKLACE).build()
                                .build()
                                .withItem(ItemId.FEATHER).amount(1, Integer.MAX_VALUE).build()
                            .build(),
            Set.of(new Tradeable(ItemId.FEATHER, 13000), new Tradeable(ItemId.GAMES_NECKLACE8, 1), new Tradeable(ItemId.RING_OF_WEALTH, 1))
    );

    private final int minimumFishingLevel;

    @Getter
    private final Loadout loadout;

    public Set<Tradeable> getTradeables() {
        return Collections.unmodifiableSet(this.tradeables);
    }

    private final Set<Tradeable> tradeables;

}
