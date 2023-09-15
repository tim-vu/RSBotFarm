package net.rlbot.script.api.quests.biohazard;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class Biohazard extends QuestTask {
    public Biohazard() {
        super(new BiohazardQuestDescriptor());
    }
}
