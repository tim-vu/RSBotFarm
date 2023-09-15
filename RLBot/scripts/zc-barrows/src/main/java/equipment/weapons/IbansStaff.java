package equipment.weapons;

import lombok.Getter;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;

import java.util.List;

public class IbansStaff extends RechargeableWeapon {

    private static final Loadout RECHARGE_LOADOUT = Loadout.builder()
                .withItem(ItemId.IBANS_STAFF_U).build()
                .withItem(ItemId.COINS_995).amount(250000).build()
                .withItem(ItemId.WEST_ARDOUGNE_TELEPORT).build()
            .build();

    public IbansStaff() {
        super(List.of(ItemId.IBANS_STAFF_U), RECHARGE_LOADOUT, Area.rectangular(2448, 3341, 2698, 3261), 708);
    }

    @Override
    public ActionResult recharge() {

        return ActionResult.FAILURE;

    }

    @Override
    public boolean needsRecharging() {
        return getRemainingCharges() < 250;
    }

    @Override
    public boolean checkCharges() {
        return false;
    }
}
