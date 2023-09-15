package net.rlbot.script.api.common.requirements;

import lombok.Value;
import net.rlbot.api.scene.Players;

@Value
public class CombatLevelRequirement implements Requirement {

    int level;

    @Override
    public boolean isSatisfied() {
        return Players.getLocal().getCombatLevel() >= this.level;
    }
}
