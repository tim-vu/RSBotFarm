package net.rlbot.oc.airorbs.task.magetraining.highalch;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.oc.airorbs.task.magetraining.highalch.behaviour.CastHighAlchAction;
import net.rlbot.oc.airorbs.task.magetraining.highalch.data.Keys;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityTask;
import net.rlbot.script.api.tree.Blackboard;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class HighAlchTask extends BasicActivityTask {

    private static final int RESTOCK_AMOUNT = 1020;
    private static final String TASK_NAME = "High level alchemy";

    private static final Set<Tradeable> TRADEABLES = Set.of(
            new Tradeable(ItemId.STAFF_OF_FIRE, 1),
            new Tradeable(ItemId.NATURE_RUNE, RESTOCK_AMOUNT)
    );

    private int getStopXp(){
        return stopXp;
    }

    private final int stopXp;

    @Override
    public boolean isDone(){
        return Skills.getExperience(Skill.MAGIC) > getStopXp();
    }

    public HighAlchTask(int itemId, int stopXp) {
        super(
                TASK_NAME,
                createBlackboard(itemId),
                BasicActivityConfiguration.builder()
                            .area(Areas.GRAND_EXCHANGE)
                            .loadout(getLoadout(itemId))
                            .tradeables(getTradeables(itemId))
                            .trainingNode(new CastHighAlchAction())
                        .build()
        );
        this.stopXp = stopXp;
    }

    private static Blackboard createBlackboard(int itemId) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.ITEM_ID, itemId);
        return blackboard;
    }

    private static Loadout getLoadout(int itemId){

        return Loadout.builder()
                .withItem(ItemId.NATURE_RUNE)
                    .amount(1, Integer.MAX_VALUE).build()
                .withItem(itemId)
                    .amount(1, Integer.MAX_VALUE).build()
                .withEquipmentSet()
                    .with(ItemId.STAFF_OF_AIR).build()
                    .build()
                .build();
    }

    private static Set<Tradeable> getTradeables(int itemId){
        Set<Tradeable> tradeables = new HashSet<>(TRADEABLES);
        tradeables.add(new Tradeable(itemId, RESTOCK_AMOUNT));
        return tradeables;
    }
}
