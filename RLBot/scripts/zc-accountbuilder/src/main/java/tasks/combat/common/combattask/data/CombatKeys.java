package tasks.combat.common.combattask.data;

import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.game.Combat;
import net.rlbot.api.movement.Position;
import net.rlbot.script.api.tree.Key;
import tasks.combat.common.combattask.combatstyle.CombatStyle;
import tasks.combat.common.combattask.enums.MonsterArea;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class CombatKeys {

    public static final Key<Integer> FOOD_ITEM_ID = new Key<>();

    public static final Key<Set<Integer>> ITEM_IDS_TO_LOOT = new Key<>();

    public static final Key<Position> TARGET_DEATH_POSITION = new Key<>();

    public static final Key<Integer> TARGET_INDEX = new Key<>();

    public static final Key<Boolean> BURY_BONES = new Key<>();

    public static final Key<MonsterArea> MONSTER_AREA = new Key<>();

    public static final Key<CombatStyle> COMBAT_STYLE = new Key<>();
}
