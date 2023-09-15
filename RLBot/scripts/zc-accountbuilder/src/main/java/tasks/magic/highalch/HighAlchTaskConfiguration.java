package tasks.magic.highalch;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class HighAlchTaskConfiguration {

    int itemId;

    @Builder.Default
    int restockAmount = 500;
}
