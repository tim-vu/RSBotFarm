package tasks.prayer.gildedaltar.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.prayer.gildedaltar.behaviour.Wilderness;
import tasks.prayer.gildedaltar.data.Keys;

@Slf4j
public class UnnoteBonesAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Unnoting bones";
    }

    @Override
    public void execute() {

        var druid = Npcs.getNearest("Elder chaos druid");

        if(druid == null){
            log.info("Unable to find the druid");
            return;
        }

        if(!Reachable.isReachable(druid) || druid.distance() > 8){
            Movement.walkTo(druid.getPosition());
            return;
        }

        if(Dialog.isOpen()) {

            if(!Dialog.chooseOption("Exchange All", "Exchange 'Dragon bones': 50 coins"))
            {
                log.warn("Failed to select option");
                Time.sleepTick();
                return;
            }

            if(!Time.sleepUntil(() -> Inventory.contains(getBlackboard().get(Keys.BONE).getItemId()), 3000)) {
                log.warn("Failed to unnote bones");
                Time.sleepTick();
                return;
            };

            Reaction.REGULAR.sleep();
            return;
        }

        var bones = Inventory.getFirst(getBlackboard().get(Keys.BONE).getNotedId());

        if(bones == null) {
            log.warn("Unable to find bones");
            Time.sleepTick();
            return;
        }

        if(!bones.useOn(druid) || !Time.sleepUntil(() -> Dialog.isOpen() || Wilderness.arePlayersNearby(), () -> Players.getLocal().isMoving(), 2400))
        {
            log.warn("Failed to use bones on druid");
            Time.sleepTick();
            return;
        }

        if(!Dialog.isOpen())
        {
            return;
        }

        Reaction.REGULAR.sleepWhile(() -> !Wilderness.arePlayersNearby());

    }
}
