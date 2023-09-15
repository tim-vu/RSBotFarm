package tasks.combat.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.rlbot.api.game.Skill;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.LevelRequirement;
import net.rlbot.script.api.common.requirements.QuestRequirement;
import net.rlbot.script.api.common.requirements.Requirement;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class Equipment {

    @Getter
    private final EquipmentSlot slot;

    @Getter
    private final int itemId;

    @Getter
    private final boolean tradeable;

    public boolean hasRequirements() {
        return this.requirements.stream().allMatch(Requirement::isSatisfied);
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    private final Set<Requirement> requirements;

    public static Builder builder(EquipmentSlot equipmentSlot, int itemId) {
        return new Builder(equipmentSlot, itemId);
    }

    public static class Builder {

        private final EquipmentSlot equipmentSlot;

        private final int itemId;

        private boolean tradeable;

        private final Set<Requirement> requirements;

        public Builder(@NonNull EquipmentSlot equipmentSlot, int itemId) {
            this.equipmentSlot = equipmentSlot;
            this.itemId = itemId;
            this.tradeable = true;
            this.requirements = new HashSet<>();
        }

        public Builder tradeable(boolean tradeable) {
            this.tradeable = tradeable;
            return this;
        }

        public Builder withRequirement(@NonNull Requirement requirement)
        {
               this.requirements.add(requirement);
               return this;
        }

        public Builder withLevelRequirement(@NonNull Skill skill, int level) {
            this.requirements.add(new LevelRequirement(skill, level));
            return this;
        }

        public Builder withQuestRequirement(@NonNull Quest quest) {
            this.requirements.add(new QuestRequirement(quest));
            return this;
        }

        public Equipment build() {
            return new Equipment(this.equipmentSlot, this.itemId, this.tradeable, this.requirements);
        }
    }
}
