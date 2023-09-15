package net.rlbot.api.definitions;

import lombok.Value;
import net.runelite.api.ObjectComposition;

@Value
public class ObjectDefinition {

    public ObjectDefinition(ObjectComposition def) {

        this.actions = def.getActions();
        this.name = def.getName();

        this.impostorIds = def.getImpostorIds();
        this.impostor = this.impostorIds == null || def.getImpostor() == null ? null : new ObjectDefinition(def.getImpostor());
    }

    String[] actions;

    String name;

    int[] impostorIds;

    ObjectDefinition impostor;
}