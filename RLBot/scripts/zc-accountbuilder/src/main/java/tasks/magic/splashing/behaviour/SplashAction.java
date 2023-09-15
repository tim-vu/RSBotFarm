package tasks.magic.splashing.behaviour;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.magic.splashing.data.Keys;

@Slf4j
@AllArgsConstructor
public class SplashAction extends LeafNodeBase {

    private static final String TARGET_NAME = "Monk of Zamorak";

    @Override
    public String getStatus() {
        return "Splashing";
    }

    @Override
    public void execute() {

        var monk = Npcs.getNearest(TARGET_NAME);

        if(monk == null){
            log.warn("Failed to find the monk");
            return;
        }

        int currentXp = Skills.getExperience(Skill.MAGIC);

        Action.logPerform("CAST_SPELL");
        if(!Magic.cast(getBlackboard().get(Keys.SPLASHING_SPELL).getSpell(), monk)){
            Action.logFail("CAST_SPELL");
            return;
        }

        if(!Time.sleepUntil(() -> (Skills.getExperience(Skill.MAGIC) > currentXp && !Players.getLocal().isAnimating()), 5000)) {
            Action.logTimeout("CAST_SPELL");
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
