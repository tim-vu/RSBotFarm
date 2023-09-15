package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.scene.Players;
import net.rlbot.api.scene.SceneObjects;
import net.rlbot.api.widgets.Production;
import net.rlbot.oc.airorbs.task.airorbs.DangerousPlayers;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class ChargeAirOrbsAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Charging air orbs";
    }

    @Override
    public void execute() {

        if(Inventory.getCount(ItemId.UNPOWERED_ORB) == 0)
        {
            log.info("Out of unpowered orbs, stopping charging");
            getBlackboard().put(Keys.IS_CHARGING, false);
            return;
        }

        if(!Players.getLocal().getPosition().equals(Constants.OBELISK_CHARGE_POSITION)){

            Action.logPerform("MOVE_TO_CHARGING_SPOT");
            if(!Movement.walk(Constants.OBELISK_CHARGE_POSITION) || !Time.sleepUntil(
                    () -> Players.getLocal().getPosition().equals(Constants.OBELISK_CHARGE_POSITION),
                    () -> Players.getLocal().isMoving(), 2000))
            {
                Action.logPerform("MOVE_TO_CHARGING_SPOT");
                return;
            }

            Reaction.PREDICTABLE.sleep();
            return;
        }

        var obelisk = SceneObjects.getNearest("Obelisk of Air");

        if(obelisk == null) {
            log.warn("Failed to find the obelisk");
            Time.sleepTick();
            return;
        }

        Player self = Players.getLocal();
        if(self.isAnimating()) {

            log.trace("Unpowered orbs remaining: " + Inventory.getCount(ItemId.UNPOWERED_ORB));
            if(!Time.sleepUntil(() -> self.getAnimationFrame() > 10 || DangerousPlayers.shouldEscape(getBlackboard()), 3000)) {
                log.warn("Failed to wait for animation to finish");
                Time.sleepTick();
                return;
            }

            if(DangerousPlayers.shouldEscape(getBlackboard()))
            {
                log.info("Escaping dangerous players");
                return;
            }
        }

        Action.logPerform("CAST_CHARGE_AIR_ORB");
        if(!Magic.cast(SpellBook.Standard.CHARGE_AIR_ORB, obelisk))
        {
            Action.logFail("CAST_CHARGE_AIR_ORB");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(Production::isOpen, 1200)) {
            Action.logTimeout("CAST_CHARGE_AIR_ORB");
            return;
        }

        Action.logPerform("CHOOSE_AIR_ORB");
        if(!Production.chooseOption(1))
        {
            Action.logFail("CHOOSE_AIR_ORB");
            return;
        }

        if(Inventory.getCount(ItemId.UNPOWERED_ORB) == 2) {

            log.trace("Waiting for the last air orb to be charged");
            if(!Time.sleepUntil(() -> Inventory.getCount(ItemId.UNPOWERED_ORB) == 0, 5000)) {
                Action.logTimeout("CHOOSE_AIR_ORB");
                Time.sleepTick();
                return;
            }

            log.info("Last air orb charged");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> {
            Player me = Players.getLocal();
            return (me.isAnimating() && me.getAnimationFrame() < 10)
                    || DangerousPlayers.shouldEscape(getBlackboard());
        }, 3000)) {
            Action.logTimeout("CHOOSE_AIR_ORB");
            Time.sleepTick();
        }
    }
}
