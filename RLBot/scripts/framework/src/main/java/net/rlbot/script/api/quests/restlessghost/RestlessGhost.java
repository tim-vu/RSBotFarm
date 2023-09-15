package net.rlbot.script.api.quests.restlessghost;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class RestlessGhost extends QuestTask {
    public RestlessGhost() {
        super(new RestlessGhostQuestDescriptor());
    }
}
