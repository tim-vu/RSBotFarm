package net.rlbot.api.movement.pathfinder.model.requirement;

import lombok.Value;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;

@Value
public class SkillRequirement implements Requirement
{
    Skill skill;
    int level;

    @Override
    public Boolean get()
    {
        return Skills.getLevel(skill) >= level;
    }
}
