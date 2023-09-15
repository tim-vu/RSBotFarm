package net.rlbot.oc.airorbs.task.airorbs;

import net.rlbot.api.Game;
import net.rlbot.api.event.listeners.DeathListener;
import net.rlbot.api.event.listeners.HitsplatListener;
import net.rlbot.api.event.listeners.SkillListener;
import net.rlbot.api.event.types.DeathEvent;
import net.rlbot.api.event.types.HitsplatEvent;
import net.rlbot.api.event.types.SkillEvent;
import net.rlbot.api.game.Skill;
import net.rlbot.api.scene.Players;
import net.rlbot.oc.airorbs.task.airorbs.behaviour.Behaviour;
import net.rlbot.oc.airorbs.task.airorbs.data.Keys;
import net.rlbot.oc.airorbs.task.airorbs.data.Constants;
import net.rlbot.oc.airorbs.task.airorbs.data.Statistics;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import net.rlbot.script.farm.api.MulingApi;

import java.time.LocalDateTime;
import java.util.List;

public class AirOrbsTask implements Task, DeathListener, HitsplatListener, SkillListener {


    private final DecisionTree behaviour;

    private final Blackboard blackboard;

    @Override
    public Node getNode() {

        if(this.blackboard.get(Keys.IS_CHARGING))
        {
            DangerousPlayers.updateDangerousPlayers(this.blackboard);
        }

        return this.behaviour.getValidNode();
    }

    @Override
    public boolean isDone() {
        return this.blackboard.get(RestockingKeys.IS_OUT_OF_COINS);
    }

    @Override
    public void initialize(){
        Game.getEventDispatcher().register(this);

        this.blackboard.get(Keys.STATISTICS).initialize();
    }

    @Override
    public void terminate(){
        Game.getEventDispatcher().deregister(this);
    }

    @Override
    public List<String> getPaintInfo(){

        var statistics = this.blackboard.get(Keys.STATISTICS);

        return List.of(
                String.format("Orbs charged: %d", statistics.getOrbsCharged()),
                String.format("Total profit: %.2fk", statistics.getTotalProfit() / 1000f),
                String.format("Profit per hour: %.2fk", statistics.getHourlyProfit() / 1000f),
                String.format("Deaths: %d", statistics.getDeaths())
        );
    }

    public AirOrbsTask(MulingApi mulingApi){
        this.blackboard = new Blackboard();
        this.blackboard.put(Keys.STATISTICS, new Statistics());
        this.blackboard.put(Keys.HAS_LOADOUT, new TempMemory<>(() -> LoadoutManager.hasLoadout(Constants.LOADOUT), 5000));

        var restockingSettings = RestockingSettings.builder()
                .sellItems(true)
                .sellBeforeBuy(false)
                .tradeables(Constants.TRADEABLES)
                .build();

        this.behaviour = Behaviour.buildTree(blackboard, restockingSettings);
    }

    @Override
    public void notify(DeathEvent deathEvent) {

        if(deathEvent.getActor().equals(Players.getLocal()))
        {
            blackboard.put(Keys.IS_CHARGING, false);
            blackboard.put(Keys.TELEBLOCK_TIMESTAMP, LocalDateTime.MIN);
            blackboard.put(Keys.HITSPLAT_TIMESTAMP, LocalDateTime.MIN);
            blackboard.get(Keys.STATISTICS).incrementDeaths();
        }
    }

    @Override
    public void notify(HitsplatEvent hitsplatEvent) {

        if(hitsplatEvent.getActor().equals(Players.getLocal())){
            this.blackboard.put(Keys.HITSPLAT_TIMESTAMP, LocalDateTime.now());
        }
    }

    @Override
    public void notify(SkillEvent skillEvent) {

        if(skillEvent.getSkill() == Skill.MAGIC){
            this.blackboard.get(Keys.STATISTICS).incrementOrbsCharged();
        }

    }
}
