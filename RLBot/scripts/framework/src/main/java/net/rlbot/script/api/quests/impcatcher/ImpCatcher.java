package net.rlbot.script.api.quests.impcatcher;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class ImpCatcher extends QuestTask {

    public ImpCatcher() {
        super(new ImpCatcherQuestDescriptor());
    }
}
