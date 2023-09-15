package net.rlbot.script.api.quests.tutorialisland;

import net.rlbot.script.api.quest.QuestDescriptorBase;

public class TutorialIslandQuestDescriptor extends QuestDescriptorBase {

    public TutorialIslandQuestDescriptor() {
        super(null, new TutorialIslandQuestBuilder());
    }

}
