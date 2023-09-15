package net.rlbot.script.api.tree.behaviourtree;

public interface ParentNode extends BTNode {

    BTNode[] getChildren();

    default void reset() {

        for(var child : getChildren()) {
            if(child instanceof ParentNode parentNode) {
                parentNode.reset();
            }
        }

    }

}
