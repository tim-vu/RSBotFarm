package net.rlbot.api.movement.pathfinder.model.requirement;

import lombok.Value;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.QuestState;
import net.rlbot.api.quest.Quests;

import java.util.Set;

@Value
public class QuestRequirement implements Requirement
{

    Quest quest;
    Set<QuestState> states;

    @Override
    public Boolean get()
    {
        return states.contains(Quests.getState(quest));
    }
}
