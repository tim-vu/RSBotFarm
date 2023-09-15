package net.rlbot.script.api.tree.behaviourtree.decorator;

import lombok.Getter;
import net.rlbot.script.api.tree.behaviourtree.BlackboardNode;
import net.rlbot.script.api.tree.behaviourtree.BTNode;

public abstract class DecoratorBase extends BlackboardNode implements Decorator {

    public DecoratorBase(BTNode child)
    {
        this.child = child;
        this.children = new BTNode[] { this.child };
    }

    @Getter
    private final BTNode child;

    @Getter
    private final BTNode[] children;
}
