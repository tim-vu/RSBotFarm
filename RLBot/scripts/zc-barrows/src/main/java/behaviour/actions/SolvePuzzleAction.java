package behaviour.actions;

import tunnels.Puzzle;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class SolvePuzzleAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Solving puzzle";
    }

    @Override
    public void execute() {

        var solutionWidget = Puzzle.getSolutionWidget();

        if (solutionWidget == null) {
            log.warn("Solution widget null when trying to solve the puzzle");
            Time.sleepTick();
            return;
        }

        Action.logPerform("SELECT_ANSWER");
        if(!solutionWidget.interact("Select")) {
            Action.logFail("SELECT_ANSWER");
            Time.sleepTick();
            return;
        }

        if(!Time.sleepUntil(() -> !Puzzle.isOpen(), 1800)) {
            Action.logTimeout("SELECT_ANSWER");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
