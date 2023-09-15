package tasks.fishing.powerfishing;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.fishing.powerfishing.enums.FishingSpot;

@Value
@Builder
public class PowerFishingTaskConfiguration implements TaskConfiguration {

    FishingSpot fishingSpot;

}
