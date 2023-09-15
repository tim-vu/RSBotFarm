package tasks.magic.superheat;

import lombok.Builder;
import lombok.Value;
import tasks.magic.superheat.enums.Bar;

@Builder
@Value
public class SuperHeatTaskConfiguration {

    Bar bar;

    @Builder.Default
    int restockAmount = 280;

}
