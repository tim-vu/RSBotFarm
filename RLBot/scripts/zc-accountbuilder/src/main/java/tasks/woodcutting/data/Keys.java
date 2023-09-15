package tasks.woodcutting.data;

import net.rlbot.api.movement.Position;
import net.rlbot.script.api.tree.Key;
import tasks.woodcutting.enums.Axe;
import tasks.woodcutting.enums.Tree;

public class Keys {

    public static final Key<Axe> AXE = new Key<>();

    public static final Key<Tree> TREE = new Key<>();

    public static final Key<Boolean> IS_DROPPING = new Key<>();

    public static final Key<Position> CURRENT_TREE_POSITION = new Key<>();
}
