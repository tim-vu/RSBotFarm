package net.rlbot.oc.airorbs.task.magetraining.superheat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.InventorySupply;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public enum Bar {

    BRONZE(ItemId.BRONZE_BAR, 1, List.of(
            InventorySupply.builder(ItemId.COPPER_ORE).amount(1, 13).build(),
            InventorySupply.builder(ItemId.TIN_ORE).amount(1, 13).build()
    )),
    IRON(ItemId.IRON_BAR, 15, List.of(
            InventorySupply.builder(ItemId.IRON_ORE).amount(1, 27).build()
    )),
    SILVER(ItemId.SILVER_BAR, 20, List.of(
            InventorySupply.builder(ItemId.SILVER_ORE).amount(1, 27).build()
    ));

    @Getter
    private final int barItemId;

    @Getter
    private final int requiredSmithingLevel;

    public List<InventorySupply> getInventorySupplies(){
        return Collections.unmodifiableList(inventorySupplies);
    }

    private final List<InventorySupply> inventorySupplies;

}
