package tasks.magic.enchanting.behaviour;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;
import tasks.magic.enchanting.data.Keys;

@Slf4j
public class EnchantAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Enchanting";
    }

    @Override
    public void execute() {

        var product = getBlackboard().get(Keys.ENCHANTMENT_PRODUCT);
        var item = Inventory.getFirst(product.getSourceItemId());

        if(item == null) {
            log.warn("Unable to find source item when trying to enchant");
            Time.sleepTick();
            return;
        }

        var xp = Skills.getExperience(Skill.MAGIC);

        Action.logPerform("ENCHANT");
        if(!Magic.cast(product.getEnchantmentSpell().getSpell(), item)) {
            Action.logFail("ENCHANT");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> Skills.getExperience(Skill.MAGIC) > xp, 2400)) {
            Action.logTimeout("ENCHANT");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
