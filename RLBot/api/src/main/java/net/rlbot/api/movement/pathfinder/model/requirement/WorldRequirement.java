package net.rlbot.api.movement.pathfinder.model.requirement;

import lombok.Value;
import net.rlbot.api.game.Worlds;

@Value
public class WorldRequirement implements Requirement
{
    boolean memberWorld;

    @Override
    public Boolean get()
    {
        return !memberWorld || Worlds.isInMemberWorld();
    }
}