package tasks.magic.highalch.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.magic.highalch.data.Keys;

@Slf4j
public class HighAlchAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Casting high alch";
    }

    @Override
    public void execute() {
        var item = Inventory.getFirst(ItemDefinition.getNotedId(getBlackboard().get(Keys.ITEM_ID)));

        if(item == null) {
            log.warn("Unable to find item when trying to high alch");
            Time.sleepTick();
            return;
        }

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
