package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Health;
import net.rlbot.api.game.Prayers;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
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

        if (!Constants.CHURCH.contains(Players.getLocal())) {
            Movement.walkTo(Constants.CHURCH);
            return;
        }

        var pool = SceneObjects.getNearest("Pool of Refreshment");

        if (pool == null) {
            log.warn("Failed to find the pool of refreshment");
            return;
        }

        Action.logPerform("USE_POOL_OF_REFRESHMENT");
        if (!pool.interact("Drink") || !Time.sleepUntil(UsePoolOfRefreshment::isRefreshed, 4000)) {
            Action.logFail("USE_POOL_OF_REFRESHMENT");
            return;
        }

        Time.sleepTicks(3);
        Reaction.REGULAR.sleep();
    }

    private static boolean isRefreshed() {
        return Health.getPercent() == 100 && Movement.getRunEnergy() == 100 && Prayers.getPoints() == Skills.getLevel(Skill.PRAYER);
    }
}
