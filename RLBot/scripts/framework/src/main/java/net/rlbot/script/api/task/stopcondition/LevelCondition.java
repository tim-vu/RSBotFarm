package net.rlbot.script.api.task.stopcondition;

import lombok.Value;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;

@Value
@FieldDefaults(makeFinal = true)
public class LevelCondition implements StopCondition {

    Skill skill;

    int level;

    @Override
    public boolean getAsBoolean() {
        return Skills.getLevel(this.skill) >= this.level;
    }
}
