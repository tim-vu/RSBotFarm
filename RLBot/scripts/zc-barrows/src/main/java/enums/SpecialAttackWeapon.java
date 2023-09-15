package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.script.api.data.ItemId;

import java.util.List;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true)
public enum SpecialAttackWeapon {

    MAGIC_SHORTBOW(ItemId.MAGIC_SHORTBOW, 55),
    MAGIC_SHORTBOW_I(ItemId.MAGIC_SHORTBOW_I, 50),
    BLOW_PIPE(ItemId.TOXIC_BLOWPIPE, 55),
    DRAGON_SCIMITAR(ItemId.DRAGON_SCIMITAR, 55),
    ABYSSAL_WHIP(ItemId.ABYSSAL_WHIP, 50),
    ABYSSAL_TENTACLE(ItemId.ABYSSAL_TENTACLE, 50);

    int itemId;

    int requiredEnergy;

    public static SpecialAttackWeapon getByItemIds(List<Integer> itemIds) {

        for(var weapon : SpecialAttackWeapon.values()) {

            if(itemIds.contains(weapon.getItemId())) {
                return weapon;
            }

        }

        return null;
    }
}
