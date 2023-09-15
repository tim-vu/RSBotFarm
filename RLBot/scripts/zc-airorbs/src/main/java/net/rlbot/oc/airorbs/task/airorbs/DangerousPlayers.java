package net.rlbot.oc.airorbs.task.airorbs;

import lombok.NonNull;
import net.rlbot.api.Game;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.script.api.tree.Blackboard;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class DangerousPlayers {

    public static void updateDangerousPlayers(Blackboard blackboard){

        var wildernessLevel = Game.getWildyLevel();

        if(wildernessLevel < 5)
            return;

        final var combatLevel = Players.getLocal().getCombatLevel();

        var dangerousPlayers =  Players.query()
                .filter(p -> Math.abs(p.getCombatLevel() - combatLevel) <= 7)
                .filter(p -> p.getSkullIcon() != null || p.getTarget() == Players.getLocal())
                .reachable()
                .results();

        for(var player : dangerousPlayers){
            blackboard.get(Keys.DANGEROUS_PLAYERS).add(player.getName());
        }
    }

    public static boolean isTeleblocked(Blackboard blackboard) {
        return Math.abs(ChronoUnit.MINUTES.between(LocalDateTime.now(), blackboard.get(Keys.TELEBLOCK_TIMESTAMP))) < Constants.TELEBLOCK_DURATION_MINUTES;
    }

    public static boolean isInCombat(Blackboard blackboard) {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), blackboard.get(Keys.HITSPLAT_TIMESTAMP)) < 12;
    }

    public static boolean shouldEscape(@NonNull Blackboard blackboard){

        var isTeleblocked = isTeleblocked(blackboard);
        var isInCombat = isInCombat(blackboard);

        if(!Constants.OBELISK.contains(Players.getLocal()) || (isTeleblocked && isInCombat))
        {
            return false;
        }

        var dangerousPlayers = blackboard.get(Keys.DANGEROUS_PLAYERS);

        return Players.query().filter(p -> dangerousPlayers.contains(p.getName())).reachable().results().size() > 0;
    }
}
