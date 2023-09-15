package net.rlbot.api.widgets;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tab
{
    COMBAT(0),
    SKILLS(1),
    QUESTS(2),
    INVENTORY(3),
    EQUIPMENT(4),
    PRAYER(5),
    MAGIC(6),
    CHAT(7),
    FRIENDS(9),
    ACC_MAG(8),
    LOG_OUT(10),
    OPTIONS(11),
    EMOTES(12),
    MUSIC(13);

    @Getter
    private final int index;
}
