package net.rlbot.script.api.quests.tutorialisland;

import net.rlbot.api.game.Vars;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.quest.questexecution.QuestTask;

public class TutorialIsland extends QuestTask {

    public TutorialIsland() {
        super(new TutorialIslandQuestDescriptor());
    }

    private static final int TUTORIAL_ISLAND_PROGRESS = 281;

    @Override
    public boolean isDone() {
        return Vars.getVarp(TUTORIAL_ISLAND_PROGRESS) == 1000;
    }

}
