package net.rlbot.oc.airorbs.task.magetraining.highalch.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.oc.airorbs.task.magetraining.highalch.data.Keys;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.task.common.basicactivitytask.data.BasicActivityKeys;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class CastHighAlchAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Casting high alchemy";
    }

    @Override
    public void execute() {

        var item = Inventory.getFirst(ItemDefinition.getNotedId(getBlackboard().get(Keys.ITEM_ID)));

        int currentXp = Skills.getExperience(Skill.MAGIC);

        if(!Magic.cast(SpellBook.Standard.HIGH_LEVEL_ALCHEMY, item)) {
            log.info("Failed to cast high level alchemy");
            return;
        }

        if(!Time.sleepUntil(() -> Skills.getExperience(Skill.MAGIC) > currentXp, 5000)) {
            log.warn("Casting high level alchemy timed out");
        }

        Reaction.PREDICTABLE.sleep();
    }
}
