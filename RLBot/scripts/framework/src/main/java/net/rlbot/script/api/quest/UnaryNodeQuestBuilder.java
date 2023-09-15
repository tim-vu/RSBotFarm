package net.rlbot.script.api.quest;

import net.rlbot.script.api.quest.nodes.QuestNode;
import net.rlbot.script.api.quest.nodes.UnaryNode;
import net.rlbot.script.api.quest.nodes.common.EndNode;

import java.util.Collections;
import java.util.List;

/**
 * The base class of a quest builder for a quest which only consists of {@link UnaryNode}s
 */
public class UnaryNodeQuestBuilder implements QuestBuilder {

    private final List<UnaryNode> questNodes;

    public UnaryNodeQuestBuilder(List<UnaryNode> questNodes) {

        this.questNodes = questNodes;
    }

    @Override
    public QuestNode buildQuest() {

        for (int i = 0; i < questNodes.size(); i++) {

            UnaryNode node = questNodes.get(i);

            if (i == questNodes.size() - 1) {
                node.next(new EndNode());
                break;
            }

            node.next(questNodes.get(i + 1));
        }

        return questNodes.get(0);
    }

}
