package net.rlbot.script.api.tree.behaviourtree.condition;

import net.rlbot.script.api.tree.behaviourtree.BTNode;

public interface ConditionNode extends BTNode {

    default ConditionNode not() {
        return new NotNode(this);
    }

    default ConditionNode or(ConditionNode other) {
        return new OrNode(this, other);
    }

    default ConditionNode and(ConditionNode other) { return new AndNode(this, other); }

    static ConditionNode or(ConditionNode left, ConditionNode right) {
        return new OrNode(left, right);
    }

    static ConditionNode and(ConditionNode left, ConditionNode right) {
        return new AndNode(left, right);
    }
}
