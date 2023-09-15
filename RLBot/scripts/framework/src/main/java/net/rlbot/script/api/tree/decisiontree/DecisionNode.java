package net.rlbot.script.api.tree.decisiontree;

import lombok.Getter;
import lombok.NonNull;
import net.rlbot.script.api.tree.Blackboard;

public class DecisionNode extends BlackboardNode implements DTNode {

    @Getter
    private final Decision decision;

    @Getter
    private final DTNode onTrueNode;

    @Getter
    private final DTNode onFalseNode;

    public DecisionNode(@NonNull Decision decision, @NonNull DTNode onTrueNode, @NonNull DTNode onFalseNode) {
        this.decision = decision;
        this.onTrueNode = onTrueNode;
        this.onFalseNode = onFalseNode;
    }

    @Override
    public LeafNode getValidNode() {
        var valid = decision.isValid(getBlackboard());

        if(valid) {
            return onTrueNode.getValidNode();
        }

        return onFalseNode.getValidNode();
    }

    public static OnTrueBranchBuilder builder(Decision decision) {
        return new Builder(decision);
    }

    public interface OnTrueBranchBuilder {
        OnFalseBranchBuilder yes(Action action, String message);

        OnFalseBranchBuilder yes(DTNode node);

        DecisionTree.DecisionNodeBuilder<OnFalseBranchBuilder> yes(Decision decision);
    }

    public interface OnFalseBranchBuilder {

        Builder no(Action action, String message);

        Builder no(DTNode node);

        DecisionTree.DecisionNodeBuilder<Builder> no(Decision decision);
    }

    public static class Builder implements OnTrueBranchBuilder, OnFalseBranchBuilder {

        private final Decision decision;

        private DTNode onTrueNode;

        private DTNode onFalseNode;

        public Builder(Decision decision) {
            this.decision = decision;
        }

        @Override
        public OnFalseBranchBuilder yes(@NonNull Action action, @NonNull String message) {
            this.onTrueNode = new AnonLeafNode(action, message);
            return this;
        }

        @Override
        public OnFalseBranchBuilder yes(@NonNull DTNode node) {
            this.onTrueNode = node;
            return this;
        }

        @Override
        public DecisionTree.DecisionNodeBuilder<OnFalseBranchBuilder> yes(@NonNull Decision decision) {
            return new DecisionTree.DecisionNodeBuilder<>(this, n -> this.onTrueNode = n, decision);
        }

        @Override
        public Builder no(@NonNull Action action, @NonNull String message) {
            this.onFalseNode = new AnonLeafNode(action, message);
            return this;
        }

        @Override
        public Builder no(@NonNull DTNode node) {
            this.onFalseNode = node;
            return this;
        }

        @Override
        public DecisionTree.DecisionNodeBuilder<Builder> no(@NonNull Decision decision) {
            return new DecisionTree.DecisionNodeBuilder<>(this, n -> this.onFalseNode = n, decision);
        }

        public DecisionNode build() {
            return new DecisionNode(this.decision, this.onTrueNode, this.onFalseNode);
        }

    }
}
