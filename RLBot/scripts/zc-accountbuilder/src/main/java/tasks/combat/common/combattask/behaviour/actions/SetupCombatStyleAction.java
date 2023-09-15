package tasks.combat.common.combattask.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.combat.common.combattask.data.CombatKeys;

@Slf4j
public class SetupCombatStyleAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Setting up combat style";
    }

    @Override
    public void execute() {

        var combatStyle = getBlackboard().get(CombatKeys.COMBAT_STYLE);

        if(!combatStyle.setup()) {
            Time.sleepTick();
            log.warn("Failed to set up combatstyle");
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
