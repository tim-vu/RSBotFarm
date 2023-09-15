package tasks.common;

import net.rlbot.script.api.node.Node;

public interface AccountBuilderTask {

    Node getNode();

    boolean isStopped();

    void signalStop();

    default void initialize() {

    }

    default void terminate() {

    }
}
