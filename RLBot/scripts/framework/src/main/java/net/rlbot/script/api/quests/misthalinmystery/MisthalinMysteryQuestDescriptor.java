package net.rlbot.script.api.quests.misthalinmystery;

import net.rlbot.api.game.Skill;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.quest.QuestDescriptorBase;

import java.util.Set;

public class MisthalinMysteryQuestDescriptor extends QuestDescriptorBase {

    public MisthalinMysteryQuestDescriptor() {
        super(Quest.MISTHALIN_MYSTERY, new MisthalinMysteryQuestBuilder());
    }
}
