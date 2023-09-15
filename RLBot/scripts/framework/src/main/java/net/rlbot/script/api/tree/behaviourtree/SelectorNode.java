package net.rlbot.script.api.tree.behaviourtree;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SelectorNode extends CompositeNodeBase {

    private int currentNodeIndex;

    public SelectorNode(String name, BTNode[] children) {
        super(name, children);
    }

    @Override
    public Result tick() {

        var node = children[currentNodeIndex];

        var result = node.tick();

        if(result == Result.FAILURE) {

            if(this.currentNodeIndex == children.length - 1) {
                this.currentNodeIndex = 0;
                return Result.FAILURE;
            }

            this.currentNodeIndex = (this.currentNodeIndex + 1) % this.children.length;
            return Result.IN_PROGRESS;
        }

        if(result == Result.SUCCESS) {
            this.currentNodeIndex = 0;
            return Result.SUCCESS;
        }

        return Result.IN_PROGRESS;
    }


    @Override
    public void reset() {
        this.currentNodeIndex = 0;

        for(var child : children) {

            if(child instanceof ParentNode parentNode) {
                parentNode.reset();
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

            return applyDecorators(new SelectorNode(name, children.toArray(new BTNode[0])));
        }

        @Override
        void onChildNodeBuilt(BTNode node) {
            this.children.add(node);
        }
    }
}
