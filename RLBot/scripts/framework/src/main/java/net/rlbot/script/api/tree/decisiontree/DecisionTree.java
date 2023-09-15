package net.rlbot.script.api.tree.decisiontree;

import lombok.NonNull;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.tree.Blackboard;

import java.util.function.Consumer;

public class DecisionTree {
    
    private final DTNode root;

    DecisionTree(DTNode root, Blackboard blackboard) {
        this.root = root;
        attachBlackboard(this.root, blackboard);
    }

    public Node getValidNode() {
        return root.getValidNode();
    }

    private static void attachBlackboard(DTNode node, Blackboard blackboard) {

        if(node instanceof BlackboardNode blackboardNode) {
            blackboardNode.setBlackboard(blackboard);
        }

        if(node instanceof DecisionNode decisionNode) {
            attachBlackboard(decisionNode.getOnTrueNode(), blackboard);
            attachBlackboard(decisionNode.getOnFalseNode(), blackboard);
        }
    }

    public static RootBuilder builder(Blackboard blackboard) {
        return new RootBuilder(blackboard);
    }

    public static class RootBuilder {
        
        private final Blackboard blackboard;
        
        private DTNode node;
        
        public RootBuilder(Blackboard blackboard) {
            this.blackboard = blackboard;
        }
        
        public OnTrueBranchBuilder<RootBuilder> decide(@NonNull Decision decision) {
            return new DecisionNodeBuilder<>(this, n -> this.node = n, decision);
        }
        
        public DecisionTree build() {
            return new DecisionTree(this.node, this.blackboard);
        }
    }

    public interface OnTrueBranchBuilder<T> {

        OnFalseBranchBuilder<T> yes(Action action, String message);

        OnFalseBranchBuilder<T> yes(DTNode node);

        DecisionNodeBuilder<OnFalseBranchBuilder<T>> yes(Decision decision);
    }

    public interface OnFalseBranchBuilder<T> {

        T no(Action action, String message);

        T no(DTNode node);

        DecisionNodeBuilder<T> no(Decision decision);
    }

    public static class DecisionNodeBuilder<T> implements OnTrueBranchBuilder<T>, OnFalseBranchBuilder<T> {

        private final T parent;

        private final Decision decision;

        private final Consumer<DTNode> updateParent;

        private DTNode onTrueNode;

        public DecisionNodeBuilder(T parent, Consumer<DTNode> updateParent, Decision decision) {
            this.parent = parent;
            this.updateParent = updateParent;
            this.decision = decision;
        }

        public OnFalseBranchBuilder<T> yes(@NonNull Action action, @NonNull String message) {
            this.onTrueNode = new AnonLeafNode(action, message);
            return this;
        }

        public OnFalseBranchBuilder<T> yes(@NonNull DTNode node) {
            this.onTrueNode = node;
            return this;
        }

        public DecisionNodeBuilder<OnFalseBranchBuilder<T>> yes(@NonNull Decision decision) {
            return new DecisionNodeBuilder<>(this, n -> this.onTrueNode = n, decision);
        }

        public T no(@NonNull Action action, @NonNull String message) {
            var onFalseNode = new AnonLeafNode(action, message);
            updateParent.accept(new DecisionNode(this.decision, this.onTrueNode, onFalseNode));
            return parent;
        }

        public T no(@NonNull DTNode node) {
            updateParent.accept(new DecisionNode(this.decision, this.onTrueNode, node));
            return parent;
        }

        public DecisionNodeBuilder<T> no(@NonNull Decision decision) {
            return new DecisionNodeBuilder<>(parent, n -> updateParent.accept(new DecisionNode(this.decision, this.onTrueNode, n)), decision);
        }
    }
}
