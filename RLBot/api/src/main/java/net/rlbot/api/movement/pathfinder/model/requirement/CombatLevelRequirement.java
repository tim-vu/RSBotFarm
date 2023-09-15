package net.rlbot.api.movement.pathfinder.model.requirement;

import lombok.AllArgsConstructor;
import net.rlbot.api.scene.Players;

@AllArgsConstructor
public class CombatLevelRequirement implements Requirement{

    private final int level;

    @Override
    public Boolean get() {

        return Players.getLocal().getCombatLevel() >= this.level;
   }
}
