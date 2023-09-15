package net.rlbot.api.adapter.common;

import net.rlbot.api.common.Predicates;

import java.util.function.Predicate;

public interface Interactable {
    default boolean interact(String... actions) {
        return interact(Predicates.texts(actions));
    }

    default boolean interact(Predicate<String> predicate) {
        var index = getActionIndex(predicate);

        if(index == -1) {
            return false;
        }

        return interact(index);
    }

    boolean interact(int index);

    default int getActionIndex(Predicate<String> predicate) {
        var actions = getActions();

        if(actions == null) {
            return -1;
        }

        for(var i = 0; i < actions.length; i++) {
            if(actions[i] == null || actions[i].equals("") || !predicate.test(actions[i])) {
                continue;
            }

            return i;
        }

        return -1;
    };

    default boolean hasAction(Predicate<String> predicate) {
        return getActionIndex(predicate) != -1;
    }

    default boolean hasAction(String... names) {
        return hasAction(Predicates.texts(names));
    }

    String[] getActions();
}