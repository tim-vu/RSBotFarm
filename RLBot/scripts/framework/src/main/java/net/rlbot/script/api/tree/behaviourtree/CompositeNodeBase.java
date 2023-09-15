package net.rlbot.script.api.tree.behaviourtree;

import lombok.Getter;

public abstract class CompositeNodeBase extends BlackboardNode implements CompositeNode {

    @Getter
    private final String name;

    @Getter
    protected final BTNode[] children;

    protected CompositeNodeBase(String name, BTNode[] children) {
        this.name = name;
        this.children = children;
    }
}
