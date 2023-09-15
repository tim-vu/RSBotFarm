package tasks.magic.splashing;

import lombok.Builder;
import lombok.Value;
import tasks.magic.splashing.enums.SplashingSpell;

@Builder
@Value
public class SplashingTaskConfiguration {

    SplashingSpell splashingSpell;

    int restockAmount;
}
