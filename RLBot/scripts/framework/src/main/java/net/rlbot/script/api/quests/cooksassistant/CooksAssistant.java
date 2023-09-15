package net.rlbot.script.api.quests.cooksassistant;

import net.rlbot.api.script.Script;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class CooksAssistant extends QuestTask {

    public CooksAssistant(Script script) {
        super(new CooksAssistantQuestDescriptor());
    }
}
