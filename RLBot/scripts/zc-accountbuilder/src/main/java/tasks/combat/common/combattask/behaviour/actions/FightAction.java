package tasks.combat.common.combattask.behaviour.actions;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Combat;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.combat.common.combattask.data.CombatKeys;

@Slf4j
public class FightAction extends LeafNodeBase {
    private static final int MINIMUM_HEALTH_PERCENTAGE = 50;

    @Override
    public String getStatus() {
        return "Fighting";
    }

    @Override
    public void execute() {

        var currentTarget = getCurrentTarget(getBlackboard());

        var monsterArea = getBlackboard().get(CombatKeys.MONSTER_AREA);

        if (currentTarget != null &&
                currentTarget.isAlive() &&
                currentTarget.distance() < 12) {

            if (!currentTarget.equals(Players.getLocal().getTarget())) {

                log.debug("Attacking existing target");
                Action.logPerform("ATTACK_TARGET");
                if(getBlackboard().get(CombatKeys.COMBAT_STYLE).attack(currentTarget) == ActionResult.FAILURE) {
                    Action.logTimeout("ATTACK_TARGET");
                    return;
                }

                return;
            }

            if (!Time.sleepUntil(() -> { var currTarget = getCurrentTarget(getBlackboard()); return currTarget == null || currTarget.getHealthPercent() == 0; }, 3000)) {
                log.debug("Target not dead yet");
                return;
            }

            log.debug("Target died2");
            return;
        }

        getBlackboard().put(CombatKeys.TARGET_INDEX, -1);
        getBlackboard().put(CombatKeys.TARGET_DEATH_POSITION, null);

        var player = Players.getLocal();
        var newTarget = Npcs.query().names(monsterArea.getTargetNames())
                .within(monsterArea.getArea())
                .filter(t -> Players.getAll(p -> t.equals(p.getTarget())).size() == 0)
                .filter(t -> t.getTarget() == null || t.getTarget().equals(player) || !t.isHealthBarVisible())
                .alive()
                .health(1)
                .results()
                .nearest();

        if (newTarget == null) {
            log.debug("Unable to find new target");
            Time.sleepTick();
            return;
        }

        if(newTarget.distance() > 12) {
            Movement.walkTo(newTarget);
            Time.sleepTick();
            return;
        }

        log.debug("Attacking new target");
        Action.logPerform("ATTACK_NEW_TARGET");
        if (getBlackboard().get(CombatKeys.COMBAT_STYLE).attack(newTarget) == ActionResult.FAILURE) {
            Action.logFail("ATTACK_NEW_TARGET");
            return;
        }

        getBlackboard().put(CombatKeys.TARGET_INDEX, newTarget.getIndex());
    }


    private static Npc getCurrentTarget(Blackboard context ){
        return Npcs.getNearest(npc -> npc.getIndex() == context.get(CombatKeys.TARGET_INDEX));
    }

    private boolean attack(Actor npc){

        log.info("Attacking target at distance: " + npc.distance());

        if(!npc.interact("Attack"))
        {
            log.info("Failed to attack");
            return false;
        }

        var attcked = Time.sleepUntil(() -> {
            Player self = Players.getLocal();
            return !self.isMoving() && npc.equals(self.getTarget());
        }, () -> Players.getLocal().isMoving(), 3000);

        if(!attcked) {
            log.warn("Attack timed out");
            return false;
        }

        return true;
    }

}
