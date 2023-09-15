package net.rlbot.script.api.restocking;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.loadout.LoadoutManager;

import java.util.*;

@Slf4j
public class Restocking {

    static final int COINS = 995;

    private static final List<Integer> RING_OF_WEALTH = Arrays.asList(ItemId.RING_OF_WEALTH_5, ItemId.RING_OF_WEALTH_4, ItemId.RING_OF_WEALTH_3, ItemId.RING_OF_WEALTH_2, ItemId.RING_OF_WEALTH_1);

    private static final Loadout RESTOCK_LOADOUT = Loadout.builder()
            .build();

    private static final Loadout RESTOCK_LOADOUT_WITH_RING = Loadout.builder()
            .withItem(RING_OF_WEALTH)
                .amount(0, 1)
                .build()
            .build();

    public static LoadoutManager.Result setupRestocking() {
        return setupRestocking(false);
    }

    public static LoadoutManager.Result setupRestocking(boolean ring) {
        var loadout = ring ? RESTOCK_LOADOUT_WITH_RING : RESTOCK_LOADOUT;
        return LoadoutManager.setup(loadout);
    }

}
