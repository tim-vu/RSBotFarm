package tasks.combat.common.combattask.behaviour.actions;

import net.rlbot.api.common.Time;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class WaitForDeathAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Waiting for the target to die fully";
    }

    @Override
    public void execute() {
        Time.sleepTick();
    }
}
