package net.rlbot.script.api.quests.restlessghost;

import net.rlbot.script.api.quest.QuestDescriptorBase;

public class RestlessGhostQuestDescriptor extends QuestDescriptorBase {

    public RestlessGhostQuestDescriptor() {
        super(RestlessGhostBuilder.QUEST, new RestlessGhostBuilder());
    }
}

