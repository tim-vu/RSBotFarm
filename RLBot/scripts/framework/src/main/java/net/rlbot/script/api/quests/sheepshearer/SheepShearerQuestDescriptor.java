package net.rlbot.script.api.quests.sheepshearer;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.quest.QuestDescriptorBase;

public class SheepShearerQuestDescriptor extends QuestDescriptorBase {

    public SheepShearerQuestDescriptor() {
        super(Quest.SHEEP_SHEARER, new SheepShearerQuestBuilder());
    }
}
