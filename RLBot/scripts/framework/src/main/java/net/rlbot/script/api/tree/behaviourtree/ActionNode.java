package net.rlbot.script.api.tree.behaviourtree;

public interface ActionNode extends BTNode {

    static Builder builder(BTNode node) {
        return new Builder(node);
    }

    class Builder extends ParentNodeBuilder<Builder> {

        private final BTNode node;

        public Builder(BTNode node) {
            this.node = node;
        }

        public BTNode build() {
            return applyDecorators(node);
        }

        @Override
        void onChildNodeBuilt(BTNode node) {
            throw new IllegalStateException("onChildNodeBuilt is not valid for ActionBuilder");
        }
    }

}
