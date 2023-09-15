import lombok.Builder;
import lombok.Value;
import net.rlbot.script.api.loadout.EquipmentSet;

import java.util.List;

@Builder
@Value
public class BarrowsConfiguration {

    EquipmentSet magicEquipmentSet;

    EquipmentSet rangedEquipmentSet;

    EquipmentSet monsterEquipmentSet;

    int minimumPrayerPotions;

    int maximumPrayerPotions;

    int minimumFood;

    int maximumFood;

    List<Integer> foodItemIds;

    int minimumHealthPercent;
}
