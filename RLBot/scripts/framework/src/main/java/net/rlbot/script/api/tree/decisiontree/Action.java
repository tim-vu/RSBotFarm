package net.rlbot.script.api.tree.decisiontree;

import net.rlbot.script.api.tree.Blackboard;

@FunctionalInterface
public interface Action {

    void execute(Blackboard context);

}
