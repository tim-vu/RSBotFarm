package tasks.magic.superheat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.InventorySupply;

import java.util.List;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true)
public enum Bar {

    BRONZE(ItemId.BRONZE_BAR, List.of(
            InventorySupply.builder(ItemId.COPPER_ORE).amount(1, 13).build(),
            InventorySupply.builder(ItemId.TIN_ORE).amount(1, 13).build()
    )),
    IRON(ItemId.IRON_BAR, List.of(
            InventorySupply.builder(ItemId.IRON_ORE).amount(1, 27).build()
    )),
    SILVER(ItemId.SILVER_BAR, List.of(
            InventorySupply.builder(ItemId.SILVER_ORE).amount(1, 27).build()
    ));

    @Getter
    int barItemId;

    @Getter
    List<InventorySupply> inventorySupplies;

}
