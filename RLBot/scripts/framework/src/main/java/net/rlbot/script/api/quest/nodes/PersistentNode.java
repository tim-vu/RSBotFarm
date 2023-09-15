package net.rlbot.script.api.quest.nodes;

import net.rlbot.script.api.node.Node;

public interface PersistentNode extends Node {
    boolean validate();
}
