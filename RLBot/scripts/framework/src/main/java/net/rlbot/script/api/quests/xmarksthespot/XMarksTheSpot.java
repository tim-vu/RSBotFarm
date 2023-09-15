package net.rlbot.script.api.quests.xmarksthespot;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class XMarksTheSpot extends QuestTask {

    public XMarksTheSpot() {
        super(new XMarkTheSpotQuestDescriptor());
    }

}
