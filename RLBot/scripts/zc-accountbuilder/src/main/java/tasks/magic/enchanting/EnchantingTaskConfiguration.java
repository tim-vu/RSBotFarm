package tasks.magic.enchanting;

import lombok.Builder;
import lombok.Value;
import tasks.magic.enchanting.enums.EnchantmentProduct;

@Builder
@Value
public class EnchantingTaskConfiguration {

    EnchantmentProduct enchantmentProduct;

    int restockAmount;
}
