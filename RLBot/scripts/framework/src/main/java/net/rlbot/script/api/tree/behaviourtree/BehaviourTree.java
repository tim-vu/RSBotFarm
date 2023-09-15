package net.rlbot.script.api.tree.behaviourtree;

import lombok.NonNull;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.behaviourtree.decorator.RepeatUntil;
import net.rlbot.script.api.tree.behaviourtree.decorator.RepeatWhile;

import java.util.ArrayList;
import java.util.List;

public class BehaviourTree {

    public void execute() {
        this.node.tick();
    }

    private final BTNode node;

    public BehaviourTree(BTNode node, Blackboard blackboard) {
        this.node = node;
        attachBlackboard(this.node, blackboard);
    }

    private static void attachBlackboard(BTNode node, Blackboard blackboard) {

        if(node instanceof BlackboardNode blackboardNode) {
            blackboardNode.setBlackboard(blackboard);
        }

        if(node instanceof ParentNode parentNode) {
            for(var child : parentNode.getChildren()) {
                attachBlackboard(child, blackboard);
            }
        }

        if(node instanceof RepeatUntil repeatUntil) {
            attachBlackboard(repeatUntil.getCondition(), blackboard);
        }

        if(node instanceof RepeatWhile repeatWhile) {
            attachBlackboard(repeatWhile.getCondition(), blackboard);
        }
    }

    public static Builder builder(Blackboard blackboard) {
        return new Builder(blackboard);
    }

    public static class Builder extends ParentNodeBuilder<Builder> {

        private BTNode node;

        private final Blackboard blackboard;

        public Builder(Blackboard blackboard) {
            this.blackboard = blackboard;
        }

        public SequenceBuilder<Builder> withSequence(@NonNull String name) {
            return new SequenceBuilder<>(this, name);
        }

        public SelectorBuilder<Builder> withSelector(@NonNull String name) {
            return new SelectorBuilder<>(this, name);
        }


        @Override
        void onChildNodeBuilt(BTNode node) {
            this.node = node;
        }

        public BehaviourTree build() {

            if(node == null) {
                throw new IllegalStateException("node must be set");
            }

            return new BehaviourTree(this.node, this.blackboard);
        }
    }

    public static class SequenceBuilder<TParent extends ParentNodeBuilder<?>> extends ParentNodeBuilder<SequenceBuilder<TParent>> {

        private final TParent parent;

        private final String name;

        private final List<BTNode> children;

        public SequenceBuilder(TParent parent, String name) {
            this.parent = parent;
            this.name = name;
            this.children = new ArrayList<>();
        }

        public SequenceBuilder<SequenceBuilder<TParent>> withSequence(@NonNull String name) {
            return new SequenceBuilder<>(this, name);
        }

        public SelectorBuilder<SequenceBuilder<TParent>> withSelector(@NonNull String name) {
            return new SelectorBuilder<>(this, name);
        }

        public LeafNodeBuilder<SequenceBuilder<TParent>> with(@NonNull BTNode node) {
            return new LeafNodeBuilder<>(this, node);
        }

        @Override
        void onChildNodeBuilt(BTNode node) {
            this.children.add(node);
        }

        public TParent end() {

            if(children.size() == 0) {
                throw new IllegalStateException("There must be at least one child");
            }

            this.parent.onChildNodeBuilt(applyDecorators(new SequenceNode(name, children.toArray(new BTNode[0]))));
            return parent;
        }
    }

    public static class SelectorBuilder<TParent extends ParentNodeBuilder<?>> extends ParentNodeBuilder<SequenceBuilder<TParent>> {

        private final TParent parent;

        private final String name;

        private final List<BTNode> children;

        public SelectorBuilder(TParent parent, String name) {
            this.parent = parent;
            this.name = name;
            this.children = new ArrayList<>();
        }

        public SequenceBuilder<SelectorBuilder<TParent>> withSequence(@NonNull String name) {
            return new SequenceBuilder<>(this, name);
        }

        public SelectorBuilder<SelectorBuilder<TParent>> withSelector(@NonNull String name) {
            return new SelectorBuilder<>(this, name);
        }

        public LeafNodeBuilder<SelectorBuilder<TParent>> with(@NonNull BTNode node) {
            return new LeafNodeBuilder<>(this, node);
        }

        @Override
        void onChildNodeBuilt(BTNode node) {
            this.children.add(node);
        }

        public TParent end() {

            if(children.size() == 0) {
                throw new IllegalStateException("There must be at least one child");
            }

            this.parent.onChildNodeBuilt(applyDecorators(new SelectorNode(name, children.toArray(new BTNode[0]))));
            return parent;
        }
    }

    public static class LeafNodeBuilder<T extends ParentNodeBuilder<?>> extends ParentNodeBuilder<LeafNodeBuilder<T>> {

        private final T parent;

        private final BTNode action;

        public LeafNodeBuilder(T parent, BTNode action) {
            this.parent = parent;
            this.action = action;
        }

        public T build() {
            this.parent.onChildNodeBuilt(applyDecorators(this.action));
            return parent;
        }
    }
}
