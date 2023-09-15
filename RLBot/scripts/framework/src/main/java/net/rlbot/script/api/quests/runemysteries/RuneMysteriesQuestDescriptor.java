package net.rlbot.script.api.quests.runemysteries;

import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.common.requirements.Requirement;
import net.rlbot.script.api.quest.QuestBuilder;
import net.rlbot.script.api.quest.QuestDescriptorBase;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RuneMysteriesQuestDescriptor extends QuestDescriptorBase {

    public RuneMysteriesQuestDescriptor() {
        super(RuneMysteriesQuestBuilder.QUEST, Collections.emptySet(), new RuneMysteriesQuestBuilder(), Collections.emptyList(), Collections.emptySet());
    }
}
