package net.rlbot.script.api.quests.sheepshearer;

import net.rlbot.api.script.Script;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class SheepShearer extends QuestTask {
    public SheepShearer(Script script) {
        super(new SheepShearerQuestDescriptor());
    }
}
