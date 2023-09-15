package behaviour.decisions;

import net.rlbot.api.items.Equipment;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.Decision;

public class EscapeAggro implements Decision {
    @Override
    public boolean isValid(Blackboard blackboard) {

        if(Equipment.contains(ItemIds.RING_OF_DUELING)) {
            return false;
        }

        var local = Players.getLocal();
        return Npcs.query()
                .filter(n -> local.equals(n.getTarget()))
                .within(3)
                .results()
                .size() > 0;
    }
}
