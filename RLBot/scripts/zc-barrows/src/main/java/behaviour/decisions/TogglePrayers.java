package behaviour.decisions;

import data.Keys;
import enums.Brother;
import lombok.AllArgsConstructor;
import net.rlbot.api.game.Prayers;
import net.rlbot.script.api.tree.Blackboard;

import java.util.function.Supplier;

@AllArgsConstructor
public class TogglePrayers implements Supplier<Boolean> {

    private final Blackboard blackboard;

    @Override
    public Boolean get() {

        if(Prayers.getPoints() == 0) {
            return false;
        }

        var brother = Brother.getMyBrotherType();

        var prayersActive = Prayers.anyActive();

        var prayersShouldBeActive = brother != null && blackboard.get(Keys.SETTINGS).getBrotherToPray().contains(brother);

        return prayersActive == prayersShouldBeActive;
    }

}
