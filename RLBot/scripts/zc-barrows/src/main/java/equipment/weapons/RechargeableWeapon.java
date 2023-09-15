package equipment.weapons;

import equipment.RechargeableEquipment;
import lombok.Getter;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.loadout.InventorySupply;
import net.rlbot.script.api.loadout.Loadout;

import java.util.List;
import java.util.Set;

public abstract class RechargeableWeapon extends RechargeableEquipment {

    @Getter
    private final int animationId;

    public RechargeableWeapon(List<Integer> itemIds, Loadout rechargeLoadout, Area rechargeArea, int animationId) {
        super(itemIds, rechargeLoadout, rechargeArea);
        this.animationId = animationId;
    }

    public abstract boolean checkCharges();

    public void decrementShotsRemaining() {
        this.remainingCharges--;
    }

    protected void setRemainingCharges(int charges) {
        this.remainingCharges = charges;
    }

    @Getter
    private int remainingCharges = -1;
}
