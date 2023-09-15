package net.rlbot.script.api.tree.behaviourtree;

import net.rlbot.api.common.ActionResult;

public enum Result {
    SUCCESS,
    FAILURE,
    IN_PROGRESS;

    public static Result from(ActionResult result) {
        return switch(result) {

            case SUCCESS -> SUCCESS;
            case IN_PROGRESS -> IN_PROGRESS;
            case FAILURE -> FAILURE;
        };
    }
}
