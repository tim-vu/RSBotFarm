package behaviour.actions;

import data.Keys;
import enums.Brother;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Prayer;
import net.rlbot.api.game.Prayers;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

import java.util.Arrays;

@Slf4j
public class TogglePrayersAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Toggling prayers";
    }

    @Override
    public void execute() {

        var brother = Brother.getMyBrotherType();

        if(brother == null || !getBlackboard().get(Keys.SETTINGS).getBrotherToPray().contains(brother)) {

            var activePrayer = Arrays.stream(Prayer.values()).filter(Prayers::isEnabled).findFirst();

            if(activePrayer.isEmpty()) {
                log.warn("No prayers active when trying to disable prayers");
                Time.sleepTick();
                return;
            }

            Prayers.toggle(activePrayer.get());
            getBlackboard().get(Keys.TOGGLE_PRAYERS).set(false);
            Reaction.PREDICTABLE.sleep();
            return;
        }

        if(!Prayers.toggle(brother.getPrayer())) {
            log.warn("Failed to toggle prayer");
            Time.sleepTick();
            return;
        }

        getBlackboard().get(Keys.TOGGLE_PRAYERS).set(false);
        Reaction.PREDICTABLE.sleep();
    }
}

