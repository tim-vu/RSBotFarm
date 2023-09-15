package net.rlbot.script.api.common.requirements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;

@AllArgsConstructor
@ToString
public class QuestRequirement implements Requirement {

    @Getter
    private final Quest quest;

    @Override
    public boolean isSatisfied() {
        return Quests.isFinished(this.quest);
    }
}
