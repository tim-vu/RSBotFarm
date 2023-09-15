package tasks.agility;

import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.agility.behaviour.Behaviour;
import tasks.agility.behaviour.Keys;
import tasks.agility.behaviour.action.GoToStartAction;
import tasks.agility.behaviour.course.Course;
import tasks.common.AccountBuilderTask;

public class AgilityTask implements AccountBuilderTask {

    private final DecisionTree behaviour;

    public AgilityTask(Course course) {

        var blackboard = new Blackboard();
        blackboard.put(Keys.COURSE, course);
        blackboard.put(Keys.FOOD_ITEM_ID, ItemId.SALMON);

        this.behaviour = Behaviour.buildTree(blackboard);
    }

    @Override
    public Node getNode() {

        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == GoToStartAction.class) {
            this.stopped = true;
            return null;
        }

        return behaviour.getValidNode();
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
