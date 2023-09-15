package net.rlbot.api.movement.pathfinder.model.requirement;

import net.rlbot.api.game.Vars;

import java.util.function.Function;

public enum VarType implements Function<Integer, Integer>
{
    VARBIT,
    VARP;

    @Override
    public Integer apply(Integer index)
    {

        return switch (this) {
            case VARBIT -> Vars.getBit(index);
            case VARP -> Vars.getVarp(index);
        };
    }
}
