package net.rlbot.script.api.quests.tutorialisland;

import net.rlbot.api.Game;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.script.api.quest.nodes.UnaryNode;

public class SetViewportFixed extends UnaryNode {

    public SetViewportFixed() {

        super("Setting the viewport to fixed");
    }

    @Override
    protected ActionResult doExecute() {
        Game.runScript(3998, 0);
        Time.sleep(200, 800);
        return ActionResult.SUCCESS;
    }

}
