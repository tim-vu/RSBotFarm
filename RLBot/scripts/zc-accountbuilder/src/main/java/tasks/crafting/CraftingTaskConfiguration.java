package tasks.crafting;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.crafting.data.Product;

@Builder
@Value
public class CraftingTaskConfiguration implements TaskConfiguration {

    Product product;

}
