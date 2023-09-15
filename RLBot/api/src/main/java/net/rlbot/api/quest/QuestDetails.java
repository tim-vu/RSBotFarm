package net.rlbot.api.quest;

public class QuestDetails {

    enum Difficulty
    {
        ALL,
        NOVICE,
        INTERMEDIATE,
        EXPERIENCED,
        MASTER,
        GRANDMASTER,
        MINIQUEST,
        ACHIEVEMENT_DIARY,
        GENERIC,
        SKILL,
        PLAYER_QUEST;
    }

    /**
     * Describes if the quest is free-to-play (F2P), pay-to-play(P2P),
     * or a miniquest.
     */
    enum Type
    {
        F2P,
        P2P,
        MINIQUEST,
        SKILL,
        SKILL_F2P,
        SKILL_P2P,
    }

}
