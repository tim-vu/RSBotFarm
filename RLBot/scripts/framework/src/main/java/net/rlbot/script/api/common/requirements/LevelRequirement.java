package net.rlbot.script.api.common.requirements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;

@AllArgsConstructor
@ToString
public class LevelRequirement implements Requirement {

    @Getter
    private final Skill skill;

    @Getter
    private final int level;

    @Override
    public boolean isSatisfied() {
        return Skills.getLevel(this.skill) >= this.level;
    }
}
