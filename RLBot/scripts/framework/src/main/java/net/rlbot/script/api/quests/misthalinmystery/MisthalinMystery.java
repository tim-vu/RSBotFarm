package net.rlbot.script.api.quests.misthalinmystery;

import net.rlbot.api.script.Script;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class MisthalinMystery extends QuestTask {
    public MisthalinMystery(Script script) {
        super(new MisthalinMysteryQuestDescriptor());
    }
}
