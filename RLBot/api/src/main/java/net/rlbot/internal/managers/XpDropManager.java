package net.rlbot.internal.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.rlbot.api.event.types.SkillEvent;
import net.rlbot.api.game.Skill;
import net.rlbot.internal.ApiContext;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import java.util.EnumMap;
import java.util.Map;

@Singleton
public class XpDropManager {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private final Map<Skill, Integer> previousSkillExpTable = new EnumMap<>(Skill.class);

    @Inject
    public XpDropManager(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    void onStatChanged(StatChanged statChanged) {
        var rlSkill = statChanged.getSkill();
        var skill = toSkill(rlSkill);
        var xp = statChanged.getXp();

        var previous = previousSkillExpTable.put(skill, xp);

        if(previous == null || skill == null) {
            return;
        }

        API_CONTEXT.getEventDispatcher().onSkillEvent(new SkillEvent(skill, previous, xp));
    }

    private static Skill toSkill(net.runelite.api.Skill skill) {
        return switch(skill) {

            case ATTACK -> Skill.ATTACK;
            case DEFENCE -> Skill.DEFENCE;
            case STRENGTH -> Skill.STRENGTH;
            case HITPOINTS -> Skill.HITPOINTS;
            case RANGED -> Skill.RANGED;
            case PRAYER -> Skill.PRAYER;
            case MAGIC -> Skill.MAGIC;
            case COOKING -> Skill.COOKING;
            case WOODCUTTING -> Skill.WOODCUTTING;
            case FLETCHING -> Skill.FLETCHING;
            case FISHING -> Skill.FISHING;
            case FIREMAKING -> Skill.FIREMAKING;
            case CRAFTING -> Skill.CRAFTING;
            case SMITHING -> Skill.SMITHING;
            case MINING -> Skill.MINING;
            case HERBLORE -> Skill.HERBLORE;
            case AGILITY -> Skill.AGILITY;
            case THIEVING -> Skill.THIEVING;
            case SLAYER -> Skill.SLAYER;
            case FARMING -> Skill.FARMING;
            case RUNECRAFT -> Skill.RUNECRAFT;
            case HUNTER -> Skill.HUNTER;
            case CONSTRUCTION -> Skill.CONSTRUCTION;
            default -> throw new IllegalStateException("Unexpected value: " + skill);
        };
    }

}
