package tasks.combat.common.combattask.combatstyle;

import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.common.ActionResult;

public interface CombatStyle {

    boolean isSetup();

    boolean setup();

    ActionResult attack(Npc npc);
}
