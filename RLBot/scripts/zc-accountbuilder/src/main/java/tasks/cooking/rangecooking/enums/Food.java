package tasks.cooking.rangecooking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.script.api.data.ItemId;

@AllArgsConstructor
public enum Food {

    //TODO: Verify ids for BURNT_FISH
    SHRIMP(30, ItemId.RAW_SHRIMPS, ItemId.SHRIMPS, ItemId.BURNT_SHRIMP),
    MEAT(30, ItemId.RAW_BEEF, ItemId.COOKED_MEAT, ItemId.BURNT_MEAT),
    CHICKEN(30, ItemId.RAW_CHICKEN, ItemId.COOKED_CHICKEN, ItemId.BURNT_CHICKEN),
    ANCHOVIES(40, ItemId.RAW_ANCHOVIES, ItemId.ANCHOVIES, ItemId.BURNT_FISH),
    TROUT(70, ItemId.RAW_TROUT, ItemId.TROUT, ItemId.BURNT_FISH_343),
    PIKE(80, ItemId.RAW_PIKE, ItemId.PIKE, ItemId.BURNT_FISH_357),
    SALMON(90, ItemId.RAW_SALMON, ItemId.SALMON, ItemId.BURNT_FISH_367),
    APPLE_PIE(130, ItemId.UNCOOKED_APPLE_PIE, ItemId.APPLE_PIE, ItemId.BURNT_PIE),
    TUNA(100, ItemId.RAW_TUNA, ItemId.TUNA, ItemId.BURNT_FISH_369),
    LOBSTER(120, ItemId.RAW_LOBSTER, ItemId.LOBSTER, ItemId.BURNT_LOBSTER),
    SWORDFISH(140, ItemId.RAW_SWORDFISH, ItemId.SWORDFISH, ItemId.BURNT_SWORDFISH);

    @Getter
    private final int xp;
    @Getter
    private final int rawItemId;

    @Getter
    private final int cookedItemId;

    @Getter
    private final int burntItemId;
}
