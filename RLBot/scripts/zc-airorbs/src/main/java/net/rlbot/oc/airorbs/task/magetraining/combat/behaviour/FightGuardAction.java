package net.rlbot.oc.airorbs.task.magetraining.combat.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.game.Health;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.magetraining.combat.data.Keys;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class FightGuardAction extends LeafNodeBase {

    private static final String TARGET_NAME = "Guard";

    private static final int MINIMUM_HEALTH_PERCENTAGE = 65;

    private static final Spell SPELL = SpellBook.Standard.FIRE_BOLT;

    @Override
    public String getStatus() {
        return "Fighting guards";
    }

    @Override
    public void execute() {

        if (Health.getPercent() < MINIMUM_HEALTH_PERCENTAGE) {

            var food = Inventory.getFirst(getBlackboard().get(Keys.FOOD_ITEM_ID));

            if (food == null)
                return;

            var currentHp = Health.getCurrent();

            Action.logPerform("EAT_FOOD");
            if(!food.interact("Eat") || !Time.sleepUntil(() -> Health.getCurrent() > currentHp, 1200)) {
                Action.logFail("EAT_FOOD");
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        if(Magic.Autocast.getSelectedSpell() != SPELL){

            Action.logPerform("ENABLE_AUTOCAST");
            if(!Magic.Autocast.selectSpell(Magic.Autocast.Mode.OFFENSIVE, SPELL)) {
                Action.logFail("ENABLE_AUTOCAST");
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        Actor currentTarget = getCurrentTarget(getBlackboard());

        if (currentTarget != null &&
                currentTarget.isAlive() &&
                currentTarget.getName().equals(TARGET_NAME) &&
                currentTarget.distance() < 12) {

            log.trace("Saved target still alive");
            log.trace("Health:" + currentTarget.getHealthPercent());

            if (!currentTarget.equals(Players.getLocal().getTarget())) {

                Action.logPerform("ATTACK_TARGET");
                if(!attack(currentTarget)) {
                    Action.logTimeout("ATTACK_TARGET");
                }

                return;
            }

            if (!Time.sleepUntil(() -> { var currTarget = getCurrentTarget(getBlackboard()); return currTarget == null || currTarget.getHealthPercent() == 0; }, 3000)) {
                return;
            }

            Reaction.REGULAR.sleep();
            return;
        }

        getBlackboard().put(Keys.TARGET_INDEX, -1);

        Npc newTarget = Npcs.query()
                .names(TARGET_NAME)
                .filter(t -> Players.getAll(p -> t.equals(p.getTarget())).size() == 0)
                .alive()
                .results()
                .nearest(Distance.CHEBYSHEV);

        if (newTarget == null) {
            log.debug("Failed to find a guard");
            Time.sleep(200);
            return;
        }

        log.trace("New target health: " + newTarget.getHealthPercent());

        Action.logPerform("ATTACK_NEW_TARGET");
        if (!attack(newTarget)) {
            Action.logFail("ATTACK_NEW_TARGET");
        }

        log.trace("Saving new target");
        getBlackboard().put(Keys.TARGET_INDEX, newTarget.getIndex());
    }

    private static Npc getCurrentTarget(Blackboard blackboard ){
        return Npcs.getNearest(npc -> npc.getIndex() == blackboard.get(Keys.TARGET_INDEX));
    }

    private static boolean attack(Actor npc){

        log.info("Attacking target at distance: " + npc.distance());

        if(!npc.interact("Attack"))
        {
            log.info("Failed to attack");
            return false;
        }

        return Time.sleepUntil(() -> {
            Player self = Players.getLocal();
            return !self.isMoving() && npc.equals(self.getTarget());
        }, () -> Players.getLocal().isMoving(), 3000);
    }
}
