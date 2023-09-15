package net.rlbot.script.api.tree.decisiontree;

public abstract class LeafNodeBase extends BlackboardNode implements LeafNode {

    @Override
    public LeafNode getValidNode() {
        return this;
    }

    public abstract void execute();
}
