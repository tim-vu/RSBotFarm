package net.rlbot.script.api.task.common.basicactivitytask;

import lombok.Builder;
import lombok.Value;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.tree.decisiontree.DTNode;

import java.util.Collections;
import java.util.Set;

@Builder
@Value
public class BasicActivityConfiguration {

    Loadout loadout;

    Set<Tradeable> tradeables;

    Area area;

    DTNode trainingNode;

    @Builder.Default
    Set<Integer> extraItemIds = Collections.emptySet();

    @Builder.Default
    boolean restocking = false;

}
