package net.rlbot.script.api.quest.nodes.action;

import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.Time;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.reaction.Reaction;

import java.util.function.Supplier;

public class FightAction implements Supplier<Boolean> {

    private final Supplier<Npc> targetSupplier;

    private final Position safeSpot;

    public FightAction(String name) {
        //TODO: Update to use query with reachable
        this(() -> Npcs.getNearest(name));
    }

    public FightAction(Supplier<Npc> targetSupplier) {

        this.targetSupplier = targetSupplier;
        this.safeSpot = null;
    }

    public FightAction(Supplier<Npc> targetSupplier, Position safeSpot) {

        this.targetSupplier = targetSupplier;
        this.safeSpot = safeSpot;
    }

    private Supplier<Npc> getTargetSupplier() {

        return targetSupplier;
    }

    private Position getSafeSpot() {

        return safeSpot;
    }

    @Override
    public Boolean get() {

        Npc target = getTargetSupplier().get();

        if (target == null) {
            return true;
        }

        if (target.distance() > 8) {
            Movement.walkTo(target.getPosition());
            return false;
        }

        if (Players.getLocal().getTarget() != target) {

            if (!target.interact("Attack"))
                return false;

            Time.sleepUntil(() -> !getPlayer().isMoving() && getPlayer().getTarget() == target, () -> getPlayer().isMoving(), 4000);
        }

        if (Dialog.isOpen()) {

            //TODO: Verify if this works
            Dialog.close();

            Reaction.REGULAR.sleep();
        }

        if (getSafeSpot() != null && !Players.getLocal().getPosition().equals(getSafeSpot())) {

            if (!Movement.walk(getSafeSpot()) || !Time.sleepUntil(() -> Players.getLocal().getPosition().equals(getSafeSpot()), () -> Players.getLocal().isMoving(), 4000))
                return false;

        }

        boolean dead = Time.sleepUntil(() -> {
            Npc t = getTargetSupplier().get();
            return t == null || t.getHealthPercent() == 0;
        }, 2000);


        Reaction.REGULAR.sleep();
        return dead;
    }

    private Player getPlayer() {

        return Players.getLocal();
    }

}
