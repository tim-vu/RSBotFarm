package net.rlbot.api.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HintArrowType {

    NONE(0),
    PLAYER(10),
    NPC(1),
    COORDINATE(2);

    @Getter
    private final int value;

    public static HintArrowType fromValue(int value) {
        for(var hintArrow : HintArrowType.values()) {

            if(hintArrow.getValue() != value) {
                continue;
            }

            return hintArrow;
        }

        return null;
    }
}
