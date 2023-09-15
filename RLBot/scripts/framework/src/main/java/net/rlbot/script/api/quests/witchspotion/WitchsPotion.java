package net.rlbot.script.api.quests.witchspotion;

import net.rlbot.script.api.quest.QuestDescriptor;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class WitchsPotion extends QuestTask {

    public WitchsPotion() {
        super(new WitchsPotionQuestDescriptor());
    }
}
