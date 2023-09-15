package behaviour.actions;

import data.Areas;
import data.Keys;
import enums.Brother;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

import java.util.function.Supplier;

@AllArgsConstructor
@Slf4j
public class EnterMoundAction extends LeafNodeBase {

    private final Supplier<Brother> brotherSupplier;

    @Override
    public String getStatus() {
        return "Entering mound";
    }

    @Override
    public void execute() {

        var digArea = brotherSupplier.get().getDigArea();

        if(!digArea.contains(Players.getLocal())) {
            Movement.walkTo(digArea);
            return;
        }

        var spade = Inventory.getFirst(ItemId.SPADE);

        Action.logPerform("DIG_MOUND");
        if(!spade.interact("Dig")) {
            log.warn("Failed to dig into mound");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Areas.MOUNDS.contains(Players.getLocal()), 1800)) {
            log.warn("Digging into mound timed out");
            Time.sleepTick();
            return;
        }

        Reaction.REGULAR.sleep();
    }
}
