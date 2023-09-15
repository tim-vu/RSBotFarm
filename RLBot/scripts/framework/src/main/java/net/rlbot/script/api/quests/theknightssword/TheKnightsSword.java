package net.rlbot.script.api.quests.theknightssword;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class TheKnightsSword extends QuestTask {
    public TheKnightsSword() {
        super(new TheKnightsSwordQuestDescriptor());
    }
}
