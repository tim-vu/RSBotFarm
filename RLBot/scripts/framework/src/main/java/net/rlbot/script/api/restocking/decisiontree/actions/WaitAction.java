package net.rlbot.script.api.restocking.decisiontree.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Action;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class WaitAction implements Action {

    @Override
    public void execute(Blackboard context) {
        Time.sleepTick();
    }
}
