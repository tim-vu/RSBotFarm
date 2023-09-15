package net.rlbot.script.api.tree.decisiontree;

import net.rlbot.script.api.tree.Blackboard;

@FunctionalInterface
public interface Decision {

    boolean isValid(Blackboard blackboard);

}
