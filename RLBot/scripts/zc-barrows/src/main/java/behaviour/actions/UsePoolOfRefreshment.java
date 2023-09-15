package behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Health;
import net.rlbot.api.game.Prayers;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class UsePoolOfRefreshment extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Entering the portal";
    }

    @Override
    public void execute() {

        if (!Areas.FEROX_ENCLAVE_CHURCH.contains(Players.getLocal())) {
            Movement.walkTo(Areas.FEROX_ENCLAVE_CHURCH);
            return;
        }

        var pool = SceneObjects.getNearest("Pool of Refreshment");

        if (pool == null) {
            log.warn("Failed to find the pool of refreshment");
            return;
        }

        Action.logPerform("USE_POOL_OF_REFRESHMENT");
        if (!pool.interact("Drink")) {
            Action.logFail("USE_POOL_OF_REFRESHMENT");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(UsePoolOfRefreshment::isRefreshed, 4000)) {
            Action.logTimeout("USE_POOL_OF_REFRESHMENT");
            Time.sleepTick();
            return;
        }

        Time.sleepTicks(3);
        Reaction.REGULAR.sleep();
    }

    private static boolean isRefreshed() {
        return Health.getPercent() == 100 && Movement.getRunEnergy() == 100 && Prayers.getPoints() == Skills.getLevel(Skill.PRAYER);
    }
}
