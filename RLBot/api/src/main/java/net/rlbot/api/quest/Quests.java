package net.rlbot.api.quest;

import net.rlbot.internal.ApiContext;

public class Quests {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static int getStage(Quest quest) {
        return quest.getVar(API_CONTEXT.getClient());
    }

    public static QuestState getState(Quest quest)
    {
        return quest.getState(API_CONTEXT.getClient());
    }

    public static boolean isFinished(Quest quest)
    {
        return getState(quest) == QuestState.FINISHED;
    }

}
