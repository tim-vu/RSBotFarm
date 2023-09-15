package net.rlbot.script.api.quest.nodes.common;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.reaction.Reaction;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public class CombatNode extends UnaryNode {

    private final Position safespot;

    private final int maxDistance;

    private final Spell spell;

    private final BooleanSupplier condition;

    private final Supplier<Npc> enemySupplier;

    private final String enemyName;

    private CombatNode(Builder builder) {

        super("Fighting " + builder.enemyName);

        if (builder.condition == null) {
            throw new NullPointerException("condition must not be null");
        }

        if (builder.enemySupplier == null) {
            throw new NullPointerException("enemySupplier must not be null");
        }

        this.enemyName = builder.enemyName;
        this.enemySupplier = builder.enemySupplier;
        this.safespot = builder.safeSpot;
        this.maxDistance = builder.maxDistance;
        this.spell = builder.spell;
        this.condition = builder.condition;
    }

    private static boolean isFighting(Npc enemy) {

        return enemy.equals(Players.getLocal().getTarget());
    }

    public static Builder builder(String enemyName) {

        return new Builder(enemyName);
    }

    public static Builder builder(Supplier<Npc> npcSupplier, String npcName) {

        return new Builder(npcSupplier, npcName);
    }

    @Override
    protected ActionResult doExecute() {

        if (condition.getAsBoolean()) {
            return ActionResult.SUCCESS;
        }

        Npc enemy = enemySupplier.get();

        if (enemy == null) {
            log.info("Failed to find enemy");
            return ActionResult.FAILURE;
        }

        if (enemy.getHealthPercent() == 0) {
            log.info("Enemy is dead");
            return ActionResult.FAILURE;
        }

        boolean fighting = isFighting(enemy);

        if (fighting && Dialog.isOpen()) {
            Dialog.continueSpace();
            return ActionResult.IN_PROGRESS;
        }

        if (safespot != null) {

            if (isEnemyCloseEnough()) {

                if (isInSafespot()) {

                    if (isFighting(enemy)) {
                        Time.sleepTick();
                        return ActionResult.IN_PROGRESS;
                    }

                    if (!attack(enemy))
                    {
                        log.warn("Failed to attack enemy");
                        return ActionResult.FAILURE;
                    }

                    Time.sleepTick();
                    return ActionResult.IN_PROGRESS;
                }

                if (!moveToSafespot())
                {
                    log.warn("Failed to move to the safe spot");
                    return ActionResult.FAILURE;
                }

                Time.sleepTick();
                return ActionResult.IN_PROGRESS;
            }

            if (!isFollowing(enemy)) {

                if (!attack(enemy))
                {
                    log.warn("Failed to attack enemy");
                    return ActionResult.FAILURE;
                }

                Reaction.REGULAR.sleep();
                return ActionResult.IN_PROGRESS;
            }

            if (isInSafespot()) {
                return ActionResult.IN_PROGRESS;
            }

            if (!moveToSafespot())
            {
                log.warn("Failed to move to the safe spot");
                return ActionResult.FAILURE;
            }

            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        if (fighting) {
            log.info("Already fighting");
            Time.sleepTick();
            return ActionResult.IN_PROGRESS;
        }

        log.info("Attacking");
        attack(enemy);
        Time.sleepTick();
        return ActionResult.IN_PROGRESS;
    }

    private boolean isEnemyCloseEnough() {

        Npc enemy = enemySupplier.get();

        return enemy != null && enemy.distanceTo(safespot, Distance.CHEBYSHEV) <= maxDistance;
    }

    private boolean isInSafespot() {

        return safespot.equals(Players.getLocal().getPosition());
    }

    private boolean isFollowing(Npc npc) {

        return npc.getTarget() == Players.getLocal();
    }

    private boolean attack(Npc enemy) {

        if (spell != null) {

            var xp = Skills.getExperience(Skill.MAGIC);

            if (!Magic.cast(spell, enemy)) {
                return false;
            }

            return Time.sleepUntil(
                    () -> Skills.getExperience(Skill.MAGIC) > xp,
                    () -> { var me = Players.getLocal(); return me.isMoving() || me.isAnimating(); },
                    1200
            );
        }

        return enemy.interact("Attack") && Time.sleepUntil(
                () -> { var me = Players.getLocal(); return me.getTarget() != null && me.isAnimating(); },
                () -> Players.getLocal().isMoving(), 5000
        );
    }

    private boolean moveToSafespot() {

        return Movement.walk(safespot) && Time.sleepUntil(this::isInSafespot, () -> Players.getLocal().isMoving(), 3000);
    }

    public static class Builder {

        private BooleanSupplier condition = () -> false;

        private Position safeSpot;

        private int maxDistance;

        private final Supplier<Npc> enemySupplier;

        private final String enemyName;

        private Spell spell;

        public Builder(Supplier<Npc> enemySupplier, String enemyName) {

            this.enemySupplier = enemySupplier;
            this.enemyName = enemyName;
        }

        public Builder(String enemyName) {

            this(() -> Npcs.getNearest(enemyName), enemyName);
        }

        public Builder safespot(Position position, int maxDistance) {

            this.safeSpot = position;
            this.maxDistance = maxDistance;
            return this;
        }

        public Builder condition(BooleanSupplier precondition) {

            this.condition = precondition;
            return this;
        }

        public Builder castSpell(Spell spell) {

            this.spell = spell;
            return this;
        }

        public CombatNode build() {

            return new CombatNode(this);
        }
    }
}
