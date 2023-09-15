package tasks.woodcutting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.script.api.data.ItemId;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
public enum Tree {

    REGULAR(Set.of("Tree", "Dying tree", "Dead tree", "Burnt tree"), ItemId.LOGS, 1),
    OAK(Set.of("Oak"), ItemId.OAK_LOGS, 15),
    WILLOW(Set.of("Willow"), ItemId.WILLOW_LOGS, 30),
    TEAK(Set.of("Teak"), ItemId.TEAK_LOGS, 35),
    MAPLE(Set.of("Maple"), ItemId.MAPLE_LOGS, 45),
    MAHOGANY(Set.of("Mahogany"), ItemId.MAHOGANY_LOGS, 50),
    YEW(Set.of("Yew"), ItemId.YEW_LOGS, 60);

    public Set<String> getNames() {
        return Collections.unmodifiableSet(names);
    }

    private final Set<String> names;

    @Getter
    private final int logItemId;

    @Getter
    private final int minimumWoodcuttingLevel;

}
