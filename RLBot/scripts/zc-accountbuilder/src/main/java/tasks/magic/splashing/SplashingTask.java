package tasks.magic.splashing;

import lombok.NonNull;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.magic.splashing.behaviour.SplashAction;
import tasks.magic.splashing.data.Keys;
import tasks.magic.splashing.enums.SplashingSpell;

import java.util.HashSet;
import java.util.Set;

public class SplashingTask implements AccountBuilderTask {

    private static final Area TRAINING_AREA = Area.polygonal(
            new Position(3214, 3477, 0),
            new Position(3216, 3477, 0),
            new Position(3218, 3479, 0),
            new Position(3220, 3479, 0),
            new Position(3220, 3475, 0),
            new Position(3214, 3475, 0)
    );

    private final DecisionTree behaviour;

    public SplashingTask(SplashingTaskConfiguration configuration) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                    .area(TRAINING_AREA)
                    .loadout(getLoadout(configuration))
                    .tradeables(getTradeables(configuration))
                    .trainingNode(new SplashAction())
                .build(),
            createBlackboard(configuration)
        );
    }

    public static Blackboard createBlackboard(SplashingTaskConfiguration configuration) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.SPLASHING_SPELL, configuration.getSplashingSpell());
        return blackboard;
    }

    public static Loadout getLoadout(SplashingTaskConfiguration configuration) {
        var builder = Loadout.builder();

        builder.withEquipmentSet()
                .with(ItemId.RUNE_FULL_HELM).build()
                .with(ItemId.RUNE_PLATEBODY).build()
                .with(ItemId.RUNE_KITESHIELD).build()
                .with(ItemId.RUNE_PLATELEGS).build()
                .build();

        for(var runeRequirement : configuration.getSplashingSpell().getSpell().getRuneRequirements()) {
            var rune = runeRequirement.getRune();

            builder.withItem(rune.getItemId()).amount(runeRequirement.getQuantity(), Integer.MAX_VALUE).build();
        }

        return builder.build();
    }

    public static Set<Tradeable> getTradeables(SplashingTaskConfiguration configuration) {
       var result = new HashSet<Tradeable>();

       result.add(new Tradeable(ItemId.RUNE_FULL_HELM, 1));
       result.add(new Tradeable(ItemId.RUNE_PLATEBODY, 1));
       result.add(new Tradeable(ItemId.RUNE_KITESHIELD, 1));
       result.add(new Tradeable(ItemId.RUNE_PLATELEGS, 1));

       for(var runeRequirement : configuration.getSplashingSpell().getSpell().getRuneRequirements()) {
           var rune = runeRequirement.getRune();

           result.add(new Tradeable(rune.getItemId(), runeRequirement.getQuantity() * configuration.getRestockAmount()));
       }

       return result;
    }

    @Override
    public Node getNode() {

        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == SplashAction.class) {
            this.stopped = true;
            return null;
        }

        return this.behaviour.getValidNode();
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    private boolean stopped;

    @Override
    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}
