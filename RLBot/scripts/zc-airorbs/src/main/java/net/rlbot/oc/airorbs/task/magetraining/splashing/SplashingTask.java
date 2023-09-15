package net.rlbot.oc.airorbs.task.magetraining.splashing;

import lombok.NonNull;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.oc.airorbs.task.magetraining.splashing.behaviour.SplashAction;
import net.rlbot.oc.airorbs.task.magetraining.splashing.data.Keys;
import net.rlbot.oc.airorbs.task.magetraining.splashing.enums.SplashingSpell;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityTask;
import net.rlbot.script.api.tree.Blackboard;

public class SplashingTask extends BasicActivityTask {

    private static final String TASK_NAME = "Splashing";

    private static final Area TRAINING_AREA = Area.polygonal(
            new Position(3214, 3477, 0),
            new Position(3216, 3477, 0),
            new Position(3218, 3479, 0),
            new Position(3220, 3479, 0),
            new Position(3220, 3475, 0),
            new Position(3214, 3475, 0)
    );

    @Override
    public boolean isDone() {
        return Skills.getLevel(Skill.MAGIC) >= stopLevel;
    }

    private final int stopLevel;

    public SplashingTask(@NonNull SplashingSpell spell, int stopLevel) {
        super(
                TASK_NAME,
                createBlackboard(spell),
                BasicActivityConfiguration.builder()
                            .area(TRAINING_AREA)
                            .loadout(SplashingSpell.getLoadouts(spell))
                            .tradeables(spell.getTradeables())
                            .trainingNode(new SplashAction())
                        .build()
        );
        this.stopLevel = stopLevel;
    }

    private static Blackboard createBlackboard(SplashingSpell spell) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.SPELL, spell.getSpell());
        return blackboard;
    }
}
