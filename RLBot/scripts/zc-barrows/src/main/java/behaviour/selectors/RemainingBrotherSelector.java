package behaviour.selectors;

import data.Keys;
import enums.Brother;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.script.api.tree.Blackboard;

import java.util.function.Supplier;

@AllArgsConstructor
@Slf4j
public class RemainingBrotherSelector implements Supplier<Brother> {

    private final Blackboard blackboard;

    @Override
    public Brother get() {
        return this.blackboard.get(Keys.REMAINING_BROTHERS).peek();
    }
}
