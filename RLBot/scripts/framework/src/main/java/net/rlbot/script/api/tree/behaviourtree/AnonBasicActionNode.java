package net.rlbot.script.api.tree.behaviourtree;

import lombok.AllArgsConstructor;
import net.rlbot.script.api.tree.Blackboard;

import java.util.function.Function;

@AllArgsConstructor
public class AnonBasicActionNode extends BasicActionNode {

    private final Function<Blackboard, Boolean> action;

    @Override
    protected boolean execute() {
        return action.apply(getBlackboard());
    }
}
