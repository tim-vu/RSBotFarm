package tasks.prayer.gildedaltar.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.prayer.gildedaltar.behaviour.Wilderness;
import tasks.prayer.gildedaltar.data.Constants;
import tasks.prayer.gildedaltar.data.Keys;

@Slf4j
public class PrayAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Praying";
    }

    @Override
    public void execute() {


        var altar = SceneObjects.query()
                .names("Chaos altar")
                .within(Constants.GILDED_ALTAR)
                .results()
                .nearest();

        if(altar == null){
            log.info("Unable to find the chaos altar");
            return;
        }

        if(altar.distance() > 8 || !Constants.CHAOS_TEMPLE_INSIDE.contains(Players.getLocal())){
            Movement.walkTo(altar);
            return;
        }

        var bones = Inventory.getFirst(getBlackboard().get(Keys.BONE).getItemId());

        if(bones == null) {
            log.warn("Unable to find bones");
            Time.sleepTick();
            return;
        }

        int currentXp = Skills.getExperience(Skill.PRAYER);

        if(!bones.useOn(altar) || !Time.sleepUntil(() -> Skills.getExperience(Skill.PRAYER) > currentXp || Wilderness.arePlayersNearby() || Dialog.isOpen(), () -> Players.getLocal().isMoving(), 3000)) {
            log.warn("Failed to bury bone");
            Time.sleepTick();
            return;
        }

        if(Wilderness.arePlayersNearby())
        {
            return;
        }

        Reaction.REGULAR.sleepWhile(() -> !Wilderness.arePlayersNearby());
    }
}
