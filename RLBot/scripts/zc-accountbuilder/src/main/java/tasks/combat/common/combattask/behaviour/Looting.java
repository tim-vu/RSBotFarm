package tasks.combat.common.combattask.behaviour;

import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.scene.Pickables;
import net.rlbot.script.api.tree.Blackboard;
import tasks.combat.common.combattask.data.CombatKeys;

public class Looting {

    public static Pickable getLoot(Blackboard blackboard) {

        var deathPosition = blackboard.get(CombatKeys.TARGET_DEATH_POSITION);

        if(deathPosition == null) {
            return null;
        }

        return Pickables.query()
                .ids(blackboard.get(CombatKeys.ITEM_IDS_TO_LOOT))
                .locations(deathPosition)
                .results()
                .first();
    }

}
