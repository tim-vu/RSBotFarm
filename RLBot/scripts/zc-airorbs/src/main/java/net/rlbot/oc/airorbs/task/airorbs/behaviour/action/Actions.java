package net.rlbot.oc.airorbs.task.airorbs.behaviour.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.script.api.component.WorldHopperEx;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.AnonLeafNode;
import net.rlbot.script.api.tree.decisiontree.LeafNode;

@Slf4j
public class Actions {

    public static LeafNode hopToMemberWorld() {
        return new AnonLeafNode(c -> {
            if(!WorldHopperEx.randomHopInF2p()) {
                log.warn("Failed to hop to member world");
                return;
            }

            Reaction.REGULAR.sleep();
        }, "Hopping to members world");
    }

}
