package net.rlbot.script.api.quests.doricsquest;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class DoricsQuest extends QuestTask {
    public DoricsQuest() {
        super(new DoricsQuestDescriptor());
    }
}
