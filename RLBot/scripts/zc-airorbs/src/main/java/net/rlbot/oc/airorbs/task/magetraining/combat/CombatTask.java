package net.rlbot.oc.airorbs.task.magetraining.combat;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.event.listeners.ChatMessageListener;
import net.rlbot.api.event.types.ChatMessageEvent;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.position.Area;
import net.rlbot.oc.airorbs.task.magetraining.combat.behaviour.FightGuardAction;
import net.rlbot.oc.airorbs.task.magetraining.combat.data.Keys;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityTask;
import net.rlbot.script.api.tree.Blackboard;

import java.util.Set;

@Slf4j
public class CombatTask extends BasicActivityTask implements ChatMessageListener {

    private static final int FOOD_ITEM_ID = ItemId.SALMON;

    private static final String TASK_NAME = "Combat";

    private static final Area TRAINING_AREA = Area.polygonal(
            new Position(3207, 3471, 0),
            new Position(3219, 3471, 0),
            new Position(3221, 3469, 0),
            new Position(3224, 3469, 0),
            new Position(3224, 3461, 0),
            new Position(3222, 3459, 0),
            new Position(3204, 3459, 0),
            new Position(3202, 3461, 0),
            new Position(3202, 3469, 0),
            new Position(3205, 3469, 0)
    );

    private static final Loadout LOADOUT = Loadout.builder()
            .withItem(ItemId.AIR_RUNE)
                .amount(2, Integer.MAX_VALUE).build()
            .withItem(ItemId.CHAOS_RUNE)
                .amount(1, Integer.MAX_VALUE).build()
            .withItem(FOOD_ITEM_ID)
                .amount(1, 26).build()
            .withEquipmentSet()
                .with(ItemId.STAFF_OF_FIRE).build()
                .with(ItemId.BLUE_WIZARD_HAT).build()
                .with(ItemId.BLUE_WIZARD_ROBE).build()
                .with(ItemId.BLUE_SKIRT).build()
                .build()
            .build();

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.STAFF_OF_FIRE, 1),
            new Tradeable(ItemId.AIR_RUNE, 7950),
            new Tradeable(ItemId.CHAOS_RUNE, 2650),
            new Tradeable(FOOD_ITEM_ID, 140),
            new Tradeable(ItemId.BLUE_WIZARD_HAT, 1),
            new Tradeable(ItemId.BLUE_WIZARD_ROBE, 1),
            new Tradeable(ItemId.BLUE_SKIRT, 1)
    );

    @Override
    public boolean isDone() {
        return Skills.getLevel(Skill.MAGIC) >= stopLevel;
    }

    private final int stopLevel;

    public CombatTask(int stopLevel) {
        super(
                TASK_NAME,
                createBlackboard(),
                BasicActivityConfiguration.builder()
                            .area(TRAINING_AREA)
                            .loadout(LOADOUT)
                            .tradeables(TRADEABLES)
                            .trainingNode(new FightGuardAction())
                        .build()
        );
        this.stopLevel = stopLevel;
    }

    private static Blackboard createBlackboard() {
        var blackboard = new Blackboard();
        blackboard.put(Keys.TARGET_INDEX, -1);
        blackboard.put(Keys.FOOD_ITEM_ID, FOOD_ITEM_ID);
        return blackboard;
    }

    @Override
    public void initialize() {
        Game.getEventDispatcher().register(this);
        super.initialize();
    }

    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {

        if(chatMessageEvent.getMessage().contains("I'm already under attack.") || chatMessageEvent.getMessage().contains("Someone else is fighting that.")){
            getBlackboard().put(Keys.TARGET_INDEX, -1);
            log.info("Already under attack, switching targets");
        }
    }
}
