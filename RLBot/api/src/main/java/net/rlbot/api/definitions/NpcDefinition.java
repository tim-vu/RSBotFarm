package net.rlbot.api.definitions;

import lombok.Value;
import net.runelite.api.NPCComposition;

@Value
public class NpcDefinition {

    public NpcDefinition(NPCComposition def) {
        this.id = def.getId();
        this.name = def.getName();
        this.combatLevel = def.getCombatLevel();
        this.actions = def.getActions();
        this.size = def.getSize();

        this.configs = def.getConfigs();
        this.transform = this.configs == null ? null : new NpcDefinition(def.transform());
    }

    int id;
    String name;
    int combatLevel;

    String[] actions;

    int size;

    int[] configs;

    NpcDefinition transform;
}
