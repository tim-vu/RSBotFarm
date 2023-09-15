package net.rlbot.script.api.task.common.basicactivitytask.behaviour.action;

import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class EquipItems extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Equipping items";
    }

    @Override
    public void execute() {
        LoadoutManager.equipItems(getBlackboard().get(BasicActivityKeys.LOADOUT));
    }
}
