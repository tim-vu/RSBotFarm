package net.rlbot.oc.airorbs.task.magetraining.superheat.behaviour;

import net.rlbot.api.common.Time;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.api.widgets.Tab;
import net.rlbot.api.widgets.Tabs;
import net.rlbot.oc.airorbs.task.magetraining.superheat.data.Keys;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class CastSuperHeatItemAction extends LeafNodeBase {

    @Override
    public String getStatus() {
        return "Casting super heat item";
    }

    @Override
    public void execute() {

        var item = Inventory.getFirst(getBlackboard().get(Keys.ORE_ITEM_ID));

        int currentXpCount = Skills.getExperience(Skill.MAGIC);

        Action.logPerform("CAST_SPELL");
        if(!Magic.cast(SpellBook.Standard.SUPERHEAT_ITEM, item)){
            Action.logFail("CAST_SPELL");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> (Skills.getExperience(Skill.MAGIC) > currentXpCount && Tabs.isOpen(Tab.MAGIC)), 5000)) {
            Action.logTimeout("CAST_SPELL");
            Time.sleepTick();
            return;
        }
    }
}
