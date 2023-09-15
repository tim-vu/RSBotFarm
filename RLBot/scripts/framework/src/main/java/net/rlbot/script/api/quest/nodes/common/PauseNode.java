package net.rlbot.script.api.quest.nodes.common;


import net.rlbot.api.common.ActionResult;
import net.rlbot.script.api.quest.nodes.UnaryNode;

public class PauseNode extends UnaryNode {

    public PauseNode() {

        super("Pausing");
    }

    @Override
    protected ActionResult doExecute() {
        return ActionResult.SUCCESS;
    }

}
