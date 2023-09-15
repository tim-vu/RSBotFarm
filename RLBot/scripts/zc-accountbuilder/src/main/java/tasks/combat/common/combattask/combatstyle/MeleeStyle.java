package tasks.combat.common.combattask.combatstyle;

import lombok.AllArgsConstructor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Combat;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.reaction.Reaction;

@AllArgsConstructor
public enum MeleeStyle implements CombatStyle {

    ACCURATE(Combat.AttackStyle.FIRST),
    AGGRESIVE(Combat.AttackStyle.SECOND),
    DEFENSIVE(Combat.AttackStyle.FOURTH);

    private final Combat.AttackStyle attackStyle;

    @Override
    public boolean isSetup() {
        return Combat.getAttackStyle() == this.attackStyle;
    }

    @Override
    public boolean setup() {
        return Combat.setAttackStyle(this.attackStyle);
    }

    @Override
    public ActionResult attack(Npc npc) {

        if(npc.distance() > 7) {
            Movement.walkTo(npc);
            return ActionResult.IN_PROGRESS;
        }

        if(!npc.interact("Attack") || !Time.sleepUntil(
                () -> { var me = Players.getLocal(); return !me.isMoving() && me.getTarget().equals(npc); },
                () -> Players.getLocal().isMoving(),
                1800)) {
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.SUCCESS;
    }
}
