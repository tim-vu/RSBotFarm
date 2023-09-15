package tasks.magic.enchanting;

import net.rlbot.api.items.Inventory;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.magic.enchanting.behaviour.EnchantAction;
import tasks.magic.enchanting.data.Keys;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnchantingTask implements AccountBuilderTask {

    private final DecisionTree behaviour;

    public EnchantingTask(EnchantingTaskConfiguration configuration) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                        .loadout(getLoadout(configuration))
                        .tradeables(getTradeables(configuration))
                        .extraItemIds(Set.of(configuration.getEnchantmentProduct().getProductItemId()))
                        .trainingNode(new EnchantAction())
                        .build(),
                createBlackboard(configuration)
        );
    }

    private static Blackboard createBlackboard(EnchantingTaskConfiguration configuration) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.ENCHANTMENT_PRODUCT, configuration.getEnchantmentProduct());
        return blackboard;
    }

    private static Loadout getLoadout(EnchantingTaskConfiguration configuration) {

        var enchantmentSpell = configuration.getEnchantmentProduct().getEnchantmentSpell();
        var staff = enchantmentSpell.getElementalStaff();

        var runeRequirements = enchantmentSpell.getSpell().getRuneRequirements().stream()
                .filter(r -> r.getRune() != staff.getRune())
                .collect(Collectors.toSet());

        var builder = Loadout.builder()
                .withEquipmentSet()
                    .with(staff.getItemId()).build()
                .build();

        for(var runeRequirement : runeRequirements) {
            builder.withItem(runeRequirement.getRune().getItemId()).amount(runeRequirement.getQuantity(), Integer.MAX_VALUE).build();
        }

        builder.withItem(configuration.getEnchantmentProduct().getSourceItemId()).amount(1, Inventory.SLOTS - 1).build();

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(EnchantingTaskConfiguration configuration) {
        var result = new HashSet<Tradeable>();

        var enchantmentSpell = configuration.getEnchantmentProduct().getEnchantmentSpell();
        var staff = enchantmentSpell.getElementalStaff();

        var runeRequirements = enchantmentSpell.getSpell().getRuneRequirements().stream()
                .filter(r -> r.getRune() != staff.getRune())
                .collect(Collectors.toSet());

        result.add(new Tradeable(staff.getItemId(), 1));
        result.add(new Tradeable(configuration.getEnchantmentProduct().getSourceItemId(), configuration.getRestockAmount()));
        result.add(new Tradeable(configuration.getEnchantmentProduct().getProductItemId(), 0));

        for(var runeRequirement : runeRequirements) {
            result.add(new Tradeable(runeRequirement.getRune().getItemId(), runeRequirement.getQuantity() * configuration.getRestockAmount()));
        }

        return result;
    }

    @Override
    public Node getNode() {

        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == EnchantAction.class) {
            this.stopped = true;
            return null;
        }

        return node;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    private boolean stopped;

    @Override
    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}
