package tasks.woodcutting;

import activity.TaskConfiguration;
import lombok.Builder;
import lombok.Value;
import tasks.woodcutting.enums.Axe;
import tasks.woodcutting.enums.TreeArea;

@Builder
@Value
public class WoodcuttingTaskConfiguration implements TaskConfiguration {

    TreeArea treeArea;

    Axe axe;

}
