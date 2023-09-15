package net.rlbot.api.game;

public class Health {

    public static int getCurrent()
    {
        return Skills.getBoostedLevel(Skill.HITPOINTS);
    }

    public static double getPercent()
    {
        return ((double) getCurrent() / Skills.getLevel(Skill.HITPOINTS)) * 100;
    }

}
