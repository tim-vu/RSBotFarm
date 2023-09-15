package equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.loadout.Loadout;

import java.util.List;

@AllArgsConstructor
public abstract class RechargeableEquipment {

    @Getter
    private final List<Integer> itemIds;

    @Getter
    private final Loadout rechargeLoadout;

    @Getter
    private final Area rechargeArea;

    public abstract ActionResult recharge();

    public abstract boolean needsRecharging();

    public boolean isInInventoryOrEquipment() {
        return Inventory.contains(this.itemIds) || Equipment.contains(this.itemIds);
    }
}