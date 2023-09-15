package net.rlbot.script.api.tree.behaviourtree;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.rlbot.script.api.tree.Blackboard;

public class BlackboardNode {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PACKAGE)
    private Blackboard blackboard;

}
