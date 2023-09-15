package net.rlbot.script.api.quests.vampyreslayer;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class VampyreSlayer extends QuestTask {

    public VampyreSlayer() {
        super(new VampyreSlayerQuestDescriptor());
    }
}
