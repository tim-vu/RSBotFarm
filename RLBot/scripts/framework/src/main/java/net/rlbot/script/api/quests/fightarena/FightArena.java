package net.rlbot.script.api.quests.fightarena;

import net.rlbot.script.api.quest.questexecution.QuestTask;

public class FightArena extends QuestTask {
    public FightArena() {
        super(new FightArenaQuestDescriptor());
    }
}
