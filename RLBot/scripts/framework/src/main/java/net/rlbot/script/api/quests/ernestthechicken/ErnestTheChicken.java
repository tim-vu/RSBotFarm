package net.rlbot.script.api.quests.ernestthechicken;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class ErnestTheChicken extends QuestTask {

    public ErnestTheChicken() {
        super(new ErnestTheChickenQuestDescriptor());
    }
}
