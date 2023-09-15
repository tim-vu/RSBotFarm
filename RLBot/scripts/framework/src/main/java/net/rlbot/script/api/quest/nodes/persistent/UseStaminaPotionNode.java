package net.rlbot.script.api.quest.nodes.persistent;

import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.common.Time;
import net.rlbot.api.items.Bank;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Arrays;
import java.util.List;

public class UseStaminaPotionNode implements PersistentNode {

    private static final List<Integer> STAMINA_POTION = Arrays.asList(12625, 12627, 12629, 12631);

    @Override
    public boolean validate() {

        return Inventory.contains(i -> STAMINA_POTION.contains(i.getId())) && !Movement.isStaminaBoosted() && !Bank.isOpen();
    }

    @Override
    public String getStatus() {
        return "Using stamina potion";
    }

    @Override
    public void execute() {

        int itemId = 0;

        //TODO: Remove duplication
        for (var i = STAMINA_POTION.size() - 1; i >= 0; i--) {

            var dosedItemId = STAMINA_POTION.get(i);

            if (Inventory.getCount(dosedItemId) == 0) {
                continue;
            }

            itemId = dosedItemId;
            break;
        }

        Item staminaPotion = Inventory.getFirst(itemId);

        if(!staminaPotion.interact("Drink") || Time.sleepUntil(Movement::isStaminaBoosted, 2000)) {
            return;
        }

        Reaction.REGULAR.sleep();
    }

}
