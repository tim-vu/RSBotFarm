package net.rlbot.script.api.task;

import net.rlbot.api.common.math.Random;
import net.rlbot.api.script.Script;
import net.rlbot.script.api.node.Node;

import java.util.Collections;
import java.util.List;

public interface Task {

    Node getNode();

    boolean isDone();

    default List<String> getPaintInfo() {

        return Collections.emptyList();
    }

    default void initialize() {

    }

    default void onPause() {

    }

    default void onResume() {

    }

    default void terminate() {

    }

    default int getLoopDelay() {

        return Random.between(20, 50);
    }

}
