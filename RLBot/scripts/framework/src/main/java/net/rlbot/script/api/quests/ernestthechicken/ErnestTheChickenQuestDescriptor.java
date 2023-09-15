package net.rlbot.script.api.quests.ernestthechicken;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.Set;

public class ErnestTheChickenQuestDescriptor extends QuestDescriptorBase {

    public ErnestTheChickenQuestDescriptor() {
        super(ErnestTheChickenQuestBuilder.QUEST, new ErnestTheChickenQuestBuilder());
    }
}
