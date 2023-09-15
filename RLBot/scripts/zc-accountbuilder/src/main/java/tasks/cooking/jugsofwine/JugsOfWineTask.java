package tasks.cooking.jugsofwine;

import net.rlbot.api.event.listeners.GraphicChangedListener;
import net.rlbot.api.event.types.GraphicChangedEvent;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.action.SetupLoadoutAction;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.common.AccountBuilderTask;
import tasks.cooking.jugsofwine.behaviour.MakeJugsOfWineAction;
import tasks.cooking.jugsofwine.data.GraphicId;
import tasks.cooking.jugsofwine.data.Keys;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public class JugsOfWineTask implements AccountBuilderTask, GraphicChangedListener {

    private static final String TASK_NAME = "Jugs of wine";

    private final DecisionTree behaviour;

    private final Blackboard blackboard;

    public JugsOfWineTask() {

        this.blackboard = createBlackboard();
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                    .area(Areas.GRAND_EXCHANGE)
                    .loadout(getLoadout())
                    .tradeables(getTradeables())
                    .trainingNode(new MakeJugsOfWineAction())
                    .extraItemIds(Set.of(ItemId.JUG_OF_WINE, ItemId.JUG_OF_BAD_WINE))
                .build(),
                this.blackboard
        );
    }

    private static Blackboard createBlackboard() {
        var blackboard = new Blackboard();
        blackboard.put(Keys.FERMENT_TIME, Instant.MIN);
        return blackboard;
    }

    private static Loadout getLoadout() {
        return Loadout.builder()
                .withItem(ItemId.GRAPES).amount(1, 14).build()
                .withItem(ItemId.JUG_OF_WATER).amount(1, 14).build()
                .build();
    }

    private static Set<Tradeable> getTradeables() {
        return Set.of(
                new Tradeable(ItemId.GRAPES, 500),
                new Tradeable(ItemId.JUG_OF_WATER, 500)
        );
    }

    private static final Duration FERMENT_TIME = Duration.ofMillis(13_800);

    @Override
    public void notify(GraphicChangedEvent graphicChangedEvent) {

        var player = Players.getLocal();

        if(!graphicChangedEvent.getActor().equals(player)) {
            return;
        }

        if(player.getGraphic() == GraphicId.WINE_MAKE) {
            this.blackboard.put(Keys.FERMENT_TIME, Instant.now().plus(FERMENT_TIME));
        }
    }

    @Override
    public Node getNode() {
        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == SetupLoadoutAction.class) {
            this.stopped = true;
            return null;
        }

        return node;
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
