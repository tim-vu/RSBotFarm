package net.rlbot.api.game;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;

import java.util.Map;

public class Skills {

    public static final int[] XP_TABLE = { 0, 0, 83, 174, 276, 388, 512, 650,
            801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
            3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
            13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
            33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
            184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
            407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
            1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
            7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431,
            14391160, 15889109, 17542976, 19368992, 21385073, 23611006,
            26068632, 28782069, 31777943, 35085654, 38737661, 42769801,
            47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
            85539082, 94442737, 104273167 };

    private static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final Map<Skill, net.runelite.api.Skill> SKILL_TO_RLSKILL = Map.ofEntries(
            Map.entry(Skill.ATTACK, net.runelite.api.Skill.ATTACK),
            Map.entry(Skill.DEFENCE, net.runelite.api.Skill.DEFENCE),
            Map.entry(Skill.STRENGTH, net.runelite.api.Skill.STRENGTH),
            Map.entry(Skill.HITPOINTS, net.runelite.api.Skill.HITPOINTS),
            Map.entry(Skill.RANGED, net.runelite.api.Skill.RANGED),
            Map.entry(Skill.PRAYER, net.runelite.api.Skill.PRAYER),
            Map.entry(Skill.MAGIC, net.runelite.api.Skill.MAGIC),
            Map.entry(Skill.COOKING, net.runelite.api.Skill.COOKING),
            Map.entry(Skill.WOODCUTTING, net.runelite.api.Skill.WOODCUTTING),
            Map.entry(Skill.FLETCHING, net.runelite.api.Skill.FLETCHING),
            Map.entry(Skill.FISHING, net.runelite.api.Skill.FISHING),
            Map.entry(Skill.FIREMAKING, net.runelite.api.Skill.FIREMAKING),
            Map.entry(Skill.CRAFTING, net.runelite.api.Skill.CRAFTING),
            Map.entry(Skill.SMITHING, net.runelite.api.Skill.SMITHING),
            Map.entry(Skill.MINING, net.runelite.api.Skill.MINING),
            Map.entry(Skill.HERBLORE, net.runelite.api.Skill.HERBLORE),
            Map.entry(Skill.AGILITY, net.runelite.api.Skill.AGILITY),
            Map.entry(Skill.THIEVING, net.runelite.api.Skill.THIEVING),
            Map.entry(Skill.SLAYER, net.runelite.api.Skill.SLAYER),
            Map.entry(Skill.FARMING, net.runelite.api.Skill.FARMING),
            Map.entry(Skill.RUNECRAFT, net.runelite.api.Skill.RUNECRAFT),
            Map.entry(Skill.HUNTER, net.runelite.api.Skill.HUNTER),
            Map.entry(Skill.CONSTRUCTION, net.runelite.api.Skill.CONSTRUCTION)
    );

    public static int getBoostedLevel(@NonNull Skill skill)
    {
        return API_CONTEXT.getClient().getBoostedSkillLevel(SKILL_TO_RLSKILL.get(skill));
    }

    public static int getLevel(@NonNull Skill skill)
    {
        return API_CONTEXT.getClient().getRealSkillLevel(SKILL_TO_RLSKILL.get(skill));
    }

    public static int getExperience(@NonNull Skill skill)
    {
        return API_CONTEXT.getClient().getSkillExperience(SKILL_TO_RLSKILL.get(skill));
    }

    public static int getExperienceAt(int level) {

        if(level < 1 || level > 99)
        {
            throw new IllegalArgumentException("level must be greater than 0 and less than 100");
        }

        return Skills.XP_TABLE[level];
    }

    public static int getLevelAt(final int exp) {
        for (int i = Skills.XP_TABLE.length - 1; i > 0; i--) {
            if (exp > Skills.XP_TABLE[i]) {
                return i;
            }
        }
        return 1;
    }
}
