package tasks.cooking.rangecooking;

import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.action.GoToBankAction;
import net.rlbot.script.api.task.stopcondition.StopCondition;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.cooking.rangecooking.behaviour.CookFoodAction;
import tasks.cooking.rangecooking.data.Keys;
import tasks.cooking.rangecooking.enums.Food;
import tasks.cooking.rangecooking.enums.Range;

import java.util.HashSet;
import java.util.Set;

public class RangeCookingTask implements AccountBuilderTask {

    private static final String TASK_NAME = "Range cooking";

    private static final int RESTOCK_AMOUNT = 500;

    private final DecisionTree behaviour;

    private final StopCondition stopCondition;

    public RangeCookingTask(RangeCookingTaskConfiguration configuration, StopCondition stopCondition) {
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                            .area(configuration.getRange().getArea())
                            .loadout(getLoadout(configuration.getFood()))
                            .tradeables(getTradeables(configuration.getFood()))
                            .trainingNode(new CookFoodAction())
                            .extraItemIds(Set.of(configuration.getFood().getCookedItemId(), configuration.getFood().getBurntItemId()))
                        .build(),
                createBlackboard(configuration.getRange(), configuration.getFood())
        );
        this.stopCondition = stopCondition;
    }

    private static Blackboard createBlackboard(Range range, Food food) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.FOOD, food);
        blackboard.put(Keys.RANGE, range);
        return blackboard;
    }

    private static Loadout getLoadout(Food food) {
        return Loadout.builder()
                .withItem(food.getRawItemId()).amount(1, 28).build()
                .build();
    }

    private static Set<Tradeable> getTradeables(Food food) {
        return new HashSet<>() {{
            add(new Tradeable(food.getRawItemId(), RESTOCK_AMOUNT));
            add(new Tradeable(food.getCookedItemId(), 0));
        }};
    }

    @Override
    public Node getNode() {
        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == GoToBankAction.class) {
            this.stopped = true;
            return null;
        }

        return node;
    }

    @Override
    public boolean isStopped() {
        return this.stopped || this.stopCondition.getAsBoolean();
    }

    private boolean stopped;

    @Override
    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}
