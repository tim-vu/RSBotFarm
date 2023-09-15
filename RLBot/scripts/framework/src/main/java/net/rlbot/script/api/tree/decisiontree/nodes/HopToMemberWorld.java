package net.rlbot.script.api.tree.decisiontree.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.game.Worlds;
import net.rlbot.script.api.component.WorldHopperEx;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class HopToMemberWorld extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Hopping to member world";
    }

    @Override
    public void execute() {

    }

}
