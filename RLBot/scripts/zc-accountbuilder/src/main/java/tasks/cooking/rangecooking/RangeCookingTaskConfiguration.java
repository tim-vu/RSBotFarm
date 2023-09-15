package tasks.cooking.rangecooking;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.cooking.rangecooking.enums.Food;
import tasks.cooking.rangecooking.enums.Range;

@Builder
@Value
public class RangeCookingTaskConfiguration implements TaskConfiguration {

    Range range;

    Food food;

    @Builder.Default
    int restockAmount = 280;

    @Builder.Default
    boolean restock = false;
}