import behaviour.Behaviour;
import behaviour.decisions.TogglePrayers;
import data.Keys;
import data.Settings;
import enums.Brother;
import enums.SpecialAttackWeapon;
import equipment.RechargeableEquipment;
import equipment.weapons.RechargeableWeapon;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.event.listeners.AnimationChangedListener;
import net.rlbot.api.event.listeners.ChatMessageListener;
import net.rlbot.api.event.listeners.DeathListener;
import net.rlbot.api.event.types.AnimationChangedEvent;
import net.rlbot.api.event.types.ChatMessageEvent;
import net.rlbot.api.event.types.DeathEvent;
import net.rlbot.api.game.HintArrow;
import net.rlbot.api.scene.Players;
import net.rlbot.api.script.Script;
import net.rlbot.api.script.ScriptManifest;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ScriptManifest(name = "Barrows", author = "Tim", version = 0.01)
public class Barrows extends Script implements DeathListener, ChatMessageListener, AnimationChangedListener {

    private final DecisionTree behaviour;

    private final Blackboard blackboard;

    private String previousStatus;

    @Override
    public int loop() {
        var node = this.behaviour.getValidNode();

        if(node != null) {

            var status = node.getStatus();

            if(status != null && !status.equals(previousStatus)) {
                log.debug("Status: {}", status);
                this.previousStatus = status;
            }

            node.execute();
        }

        return Random.between(50, 200);
    }

    public Barrows() {
        this.blackboard = createBlackboard(Configurations.TRIDENT);
        this.behaviour = Behaviour.createBehaviour(this.blackboard, RestockingSettings.builder()
                .sellBeforeBuy(true)
                .sellItems(true)
                .build()
        );
    }

    private static Blackboard createBlackboard(BarrowsConfiguration configuration) {

        var loadout = getLoadout(configuration);

        var specialAttackWeapons = getSpecialAttackWeapons(loadout);
        var rechargeableEquipment = getRechargeableEquipment(loadout);

        var settings = Settings.builder()
                .brotherToPray(Set.of(Brother.values()))
                .minimumHealthPercent(configuration.getMinimumHealthPercent())
                .foodItemIds(configuration.getFoodItemIds())
                .minimumPrayerPoints(5)
                .loadout(loadout)
                .magicEquipmentSet(configuration.getMagicEquipmentSet())
                .rangedEquipmentSet(configuration.getRangedEquipmentSet())
                .monsterEquipmentSet(configuration.getMonsterEquipmentSet())
                .specialAttackWeapons(specialAttackWeapons)
                .rechargeableEquipment(rechargeableEquipment)
                .build();

        var blackboard = new Blackboard();

        blackboard.put(Keys.SETTINGS, settings);
        blackboard.put(Keys.TOGGLE_PRAYERS, new TempMemory<>(new TogglePrayers(blackboard), 2000));
        blackboard.put(Keys.HAS_LOADOUT, new TempMemory<>(() -> LoadoutManager.isLoadoutSetup(loadout), 2000));
        blackboard.put(Keys.HAS_RECHARGE_LOADOUT, false);
        blackboard.put(Keys.REMAINING_BROTHERS, new ArrayDeque<>(Arrays.asList(Brother.values())));
        blackboard.put(Keys.SEARCHED_CHEST, false);
        blackboard.put(Keys.REWARD_POTENTIAL, 0);
        blackboard.put(Keys.TARGET_INDEX, -1);

        return blackboard;
    }

    private static Loadout getLoadout(BarrowsConfiguration configuration) {

        var builder = Loadout.builder();
        builder.withItem(ItemId.SPADE).build();
        builder.withItem(ItemId.BARROWS_TELEPORT).amount(1, 5).build();
        builder.withItem(configuration.getFoodItemIds()).amount(configuration.getMinimumFood(), configuration.getMaximumFood()).build();
        builder.withItem(ItemIds.PRAYER_POTION).amount(configuration.getMinimumPrayerPotions(), configuration.getMaximumPrayerPotions()).build();

        builder.with(configuration.getMagicEquipmentSet());
        builder.with(configuration.getRangedEquipmentSet());
        builder.with(configuration.getMonsterEquipmentSet());

        if(builder.getEquipmentSets().stream().noneMatch(s -> s.containsItem(ItemIds.RING_OF_DUELING))) {
            builder.withItem(ItemIds.RING_OF_DUELING).build();
        }

        return builder.build();
    }

    private static Set<SpecialAttackWeapon> getSpecialAttackWeapons(Loadout loadout) {

        var result = new HashSet<SpecialAttackWeapon>();

        for(var equipmentSet : loadout.getEquipmentSets()) {
            for(var supply : equipmentSet.getSupplies()) {

                var weapon = SpecialAttackWeapon.getByItemIds(supply.getItemIds());

                if(weapon == null) {
                    continue;
                }

                result.add(weapon);
            }
        }

        return result;
    }

    private static Set<RechargeableEquipment> getRechargeableEquipment(Loadout loadout) {
        var result = new HashSet<RechargeableEquipment>();

        for(var equipmentSet : loadout.getEquipmentSets()) {
            for(var supply : equipmentSet.getSupplies()) {

            }
        }

        return result;
    }
    @Override
    public void notify(DeathEvent event) {

        var source = event.getActor();

        if(!(source instanceof Npc npc)) {
            return;
        }

        if(npc.equals(HintArrow.getTargetActor())) {
            var brother = Brother.getBrotherByName(npc.getName());
            this.blackboard.get(Keys.REMAINING_BROTHERS).remove(brother);
            var rewardPotential = this.blackboard.get(Keys.REWARD_POTENTIAL);
            //noinspection DataFlowIssue
            this.blackboard.put(Keys.REWARD_POTENTIAL, rewardPotential + brother.getCombatLevel());
            return;
        }


        if(npc.getIndex() == this.blackboard.get(Keys.TARGET_INDEX)) {
            var rewardPotential = this.blackboard.get(Keys.REWARD_POTENTIAL);
            this.blackboard.put(Keys.REWARD_POTENTIAL, rewardPotential + npc.getCombatLevel());
            this.blackboard.put(Keys.TARGET_INDEX, -1);
            return;
        }

    }

    @Override
    public void notify(AnimationChangedEvent event) {

        var local = Players.getLocal();
        if(!local.equals(event.getActor())) {
            return;
        }

        var animation = local.getAnimation();

        var rechargeableEquipment = this.blackboard.get(Keys.SETTINGS).getRechargeableEquipment();

        var equipment = rechargeableEquipment.stream()
                .filter(e -> e instanceof RechargeableWeapon)
                .map(e -> (RechargeableWeapon)e)
                .filter(w -> w.getAnimationId() == animation)
                .findFirst();

        equipment.ifPresent(RechargeableWeapon::decrementShotsRemaining);
    }

    @Override
    public void notify(ChatMessageEvent event) {

        if (event.getMessage().contains("The chest is empty.")) {
            this.blackboard.put(Keys.SEARCHED_CHEST, true);
            return;
        }

        if(event.getMessage().contains("I'm already under attack.") || event.getMessage().contains("Someone else is fighting that.")) {
            this.blackboard.put(Keys.TARGET_INDEX, -1);
            return;
        }
    }

}
