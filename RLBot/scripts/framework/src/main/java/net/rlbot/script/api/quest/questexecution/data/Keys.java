package net.rlbot.script.api.quest.questexecution.data;

import net.rlbot.script.api.quest.nodes.PersistentNode;
import net.rlbot.script.api.quest.nodes.QuestNode;
import net.rlbot.script.api.tree.Key;

import java.util.List;

public class Keys {

    public static final Key<Boolean> IS_READY = new Key<>();

    public static final Key<Boolean> IS_DONE = new Key<>();

    public static final Key<List<PersistentNode>> PERSISTENT_NODES = new Key<>();

    public static final Key<QuestNode> START_NODE = new Key<>();

}
