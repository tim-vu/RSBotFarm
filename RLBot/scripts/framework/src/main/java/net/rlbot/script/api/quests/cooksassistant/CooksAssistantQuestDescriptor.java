package net.rlbot.script.api.quests.cooksassistant;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.quest.QuestDescriptorBase;

import java.util.Set;

public class CooksAssistantQuestDescriptor extends QuestDescriptorBase {
    public CooksAssistantQuestDescriptor() {
        super(Quest.COOKS_ASSISTANT, new CooksAssistantQuestBuilder());
    }
}
