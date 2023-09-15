package net.rlbot.api.event.types;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.rlbot.api.event.Event;
import net.rlbot.api.game.Skill;

@AllArgsConstructor
@Value
public class SkillEvent implements Event {

    Skill skill;

    int previous;

    int current;

}
