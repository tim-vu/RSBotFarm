package tasks.combat.common.combattask;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.event.listeners.ChatMessageListener;
import net.rlbot.api.event.listeners.DeathListener;
import net.rlbot.api.event.types.ChatMessageEvent;
import net.rlbot.api.event.types.DeathEvent;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivity;
import net.rlbot.script.api.task.common.basicactivitytask.BasicActivityConfiguration;
import net.rlbot.script.api.task.common.basicactivitytask.behaviour.action.SetupLoadoutAction;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.combat.common.combattask.behaviour.CombatBehaviour;
import tasks.combat.common.combattask.data.CombatKeys;
import tasks.common.AccountBuilderTask;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class CombatTask implements AccountBuilderTask, ChatMessageListener, DeathListener {

    private final DecisionTree behaviour;

    private final Blackboard blackboard;

    public CombatTask(@NonNull CombatConfiguration configuration) {
        this.blackboard = createBlackboard(configuration);
        this.behaviour = BasicActivity.createBehaviour(
                BasicActivityConfiguration.builder()
                        .area(configuration.getMonsterArea().getArea())
                        .loadout(getLoadout(configuration))
                        .tradeables(getTradeables(configuration))
                        .trainingNode(CombatBehaviour.buildTree())
                        .extraItemIds(getItemIdsToLoot(configuration))
                        .build(),
                this.blackboard
        );
    }

    @Override
    public void initialize() {
        Game.getEventDispatcher().register(this);
    }

    @Override
    public void terminate() {
        Game.getEventDispatcher().deregister(this);
    }

    private static Blackboard createBlackboard(CombatConfiguration configuration) {
        var blackboard = new Blackboard();
        blackboard.put(CombatKeys.FOOD_ITEM_ID, configuration.getFoodItemId());
        blackboard.put(CombatKeys.BURY_BONES, configuration.isBuryBones());
        blackboard.put(CombatKeys.TARGET_INDEX, -1);
        blackboard.put(CombatKeys.TARGET_DEATH_POSITION, null);
        blackboard.put(CombatKeys.MONSTER_AREA, configuration.getMonsterArea());
        blackboard.put(CombatKeys.ITEM_IDS_TO_LOOT, getItemIdsToLoot(configuration));
        blackboard.put(CombatKeys.COMBAT_STYLE, configuration.getCombatStyle());
        return blackboard;
    }

    private static Loadout getLoadout(CombatConfiguration configuration) {

        var builder = Loadout.builder()
                .fromLoadout(configuration.getLoadout());

        for (var supply : configuration.getMonsterArea().getInventorySupplies()) {
            builder.with(supply);
        }

        var occupiedSlots = builder.getInventorySupplies().stream().mapToInt(s -> s.isStackable() ? 1 : s.getMaximumAmount()).sum();

        builder.withItem(configuration.getFoodItemId()).amount(1, Inventory.SLOTS - occupiedSlots).build();

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(CombatConfiguration configuration) {
        var tradeables = new HashSet<>(configuration.getTradeables());
        tradeables.addAll(configuration.getMonsterArea().getTradeables());
        tradeables.add(new Tradeable(configuration.getFoodItemId(), 250));
        return tradeables;
    }

    private static Set<Integer> getItemIdsToLoot(CombatConfiguration configuration) {
        var result = new HashSet<>(configuration.getItemIdsToLoot());

        if (configuration.isBuryBones()) {
            result.add(ItemId.BONES);
            result.add(ItemId.BIG_BONES);
        }

        return result;
    }

    public Node getNode() {

        var node = this.behaviour.getValidNode();

        if(this.stop && node.getClass() == SetupLoadoutAction.class) {
            this.stopped = true;
            return null;
        }

        return node;
    }

    @Override
    public void notify(ChatMessageEvent event) {

        if (event.getMessage().contains("I'm already under attack.")) {

            var me = Players.getLocal();

            var targetingMe = Npcs.getAll(n -> me.equals(n.getTarget()));

            if (targetingMe.size() > 1) {
                log.debug("Multiple NPCs targeting me");

                var animating = targetingMe.stream().filter(Actor::isAnimating).findFirst();

                animating.ifPresentOrElse(npc -> this.blackboard.put(CombatKeys.TARGET_INDEX, npc.getIndex()), () -> {
                    log.debug("No npc targeting me is animating");
                });
                return;
            }

            var npc = Npcs.getNearest(n -> me.equals(n.getTarget()));

            if (npc == null) {
                return;
            }

            log.debug("Setting target to npc attacking me");
            this.blackboard.put(CombatKeys.TARGET_INDEX, npc.getIndex());
            return;
        }

        if (event.getMessage().contains("Someone else is fighting that.")) {
            this.blackboard.put(CombatKeys.TARGET_INDEX, -1);
            log.info("Already under attack, switching targets");
        }
    }

    @Override
    public void notify(DeathEvent event) {

        if (!(event.getActor() instanceof Npc npc)) {
            return;
        }

        if (npc.getIndex() != this.blackboard.get(CombatKeys.TARGET_INDEX)) {
            return;
        }

        log.debug("Target died");
        this.blackboard.put(CombatKeys.TARGET_DEATH_POSITION, npc.getPosition());
    }

    public boolean isStopped() {
        return this.stopped;
    }

    private boolean stopped;

    public void signalStop() {
        this.stop = true;
    }

    private boolean stop;
}


