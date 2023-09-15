package net.rlbot.api.event.listeners;


import net.rlbot.api.event.types.SkillEvent;

public interface SkillListener extends EventListener {

    void notify(SkillEvent event);

}
