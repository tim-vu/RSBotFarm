package tasks.combat.common.combattask.combatstyle;

import lombok.AllArgsConstructor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.reaction.Reaction;

@AllArgsConstructor
public class MagicStyle implements CombatStyle {

    private final Spell spell;

    private final boolean autocast;

    @Override
    public boolean isSetup() {
        return !this.autocast || net.rlbot.api.magic.Magic.Autocast.getSelectedSpell() == spell;
    }

    @Override
    public boolean setup() {

        if(!this.autocast) {
            return true;
        }

        return net.rlbot.api.magic.Magic.Autocast.selectSpell(net.rlbot.api.magic.Magic.Autocast.Mode.OFFENSIVE, this.spell);
    }

    @Override
    public ActionResult attack(Npc npc) {

        if(npc.distance() > 12) {
            Movement.walkTo(npc);
            return ActionResult.IN_PROGRESS;
        }

        if(!this.autocast) {

            var xp = Skills.getExperience(Skill.MAGIC);

            if(!net.rlbot.api.magic.Magic.cast(this.spell, npc) || !Time.sleepUntil(() -> Skills.getExperience(Skill.MAGIC) > xp, () -> Players.getLocal().isMoving(), 2400)) {
                Time.sleepTick();
                 return ActionResult.FAILURE;
            }

            Reaction.PREDICTABLE.sleep();
            return ActionResult.SUCCESS;
        }

        if(!npc.interact("Attack") || !Time.sleepUntil(
                () -> { var me = Players.getLocal(); return !me.isMoving() && npc.equals(me.getTarget()); },
                () -> Players.getLocal().isMoving(),
                1800)) {
            return ActionResult.FAILURE;
        }

        Reaction.REGULAR.sleep();
        return ActionResult.SUCCESS;
    }
}
