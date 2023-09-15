package tasks.mining.powermining;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.mining.powermining.enums.MiningArea;
import tasks.mining.powermining.enums.Pickaxe;
import tasks.mining.powermining.enums.Rock;

@Builder
@Value
public class PowerMiningTaskConfiguration implements TaskConfiguration {

    MiningArea miningArea;

    Rock rock;

    Pickaxe pickaxe;

}

