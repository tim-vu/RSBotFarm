package net.rlbot.script.api.tree.behaviourtree;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SequenceNode extends CompositeNodeBase {

    private int currentNodeIndex = 0;

    SequenceNode(String name, BTNode[] children) {
        super(name, children);
    }

    @Override
    public Result tick() {

        var node = children[currentNodeIndex];

        var result = node.tick();

        if(result == Result.FAILURE) {
            this.currentNodeIndex = 0;
            return Result.FAILURE;
        }

        if(result == Result.SUCCESS) {

            if(this.currentNodeIndex == this.children.length - 1) {
                this.currentNodeIndex = 0;
                return Result.SUCCESS;
            }

            this.currentNodeIndex++;
            return Result.IN_PROGRESS;
        }

        return Result.IN_PROGRESS;
    }

    @Override
    public void reset() {
        this.currentNodeIndex = 0;

        for(var child : this.children) {

            if(child instanceof ParentNode parentNode) {
                parentNode.reset();;
            }

        }
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder extends ParentNodeBuilder<Builder> {

        private final String name;

        private final List<BTNode> children;

        public Builder(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public BehaviourTree.SequenceBuilder<Builder> withSequence(@NonNull String name) {
            return new BehaviourTree.SequenceBuilder<>(this, name);
        }

        public BehaviourTree.SelectorBuilder<Builder> withSelector(@NonNull String name) {
            return new BehaviourTree.SelectorBuilder<>(this, name);
        }

        public BehaviourTree.LeafNodeBuilder<Builder> with(@NonNull BTNode action) {
            return new BehaviourTree.LeafNodeBuilder<>(this, action);
        }

        public BTNode end() {

            if(children.size() == 0) {
                throw new IllegalStateException("There must be at least one child");
            }

            return applyDecorators(new SequenceNode(name, children.toArray(new BTNode[0])));
        }

        @Override
        void onChildNodeBuilt(BTNode node) {
            this.children.add(node);
        }
    }
}
