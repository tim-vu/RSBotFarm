package net.rlbot.oc.airorbs;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.event.listeners.RenderListener;
import net.rlbot.api.event.types.RenderEvent;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.Walker;
import net.rlbot.api.script.ScriptManifest;
import net.rlbot.oc.airorbs.data.Constants;
import net.rlbot.oc.airorbs.task.airorbs.AirOrbsTask;
import net.rlbot.oc.airorbs.task.magetraining.combat.CombatTask;
import net.rlbot.oc.airorbs.task.magetraining.splashing.enums.SplashingSpell;
import net.rlbot.oc.airorbs.task.magetraining.splashing.SplashingTask;
import net.rlbot.oc.airorbs.task.magetraining.superheat.enums.Bar;
import net.rlbot.oc.airorbs.task.magetraining.superheat.SuperHeatTask;
import net.rlbot.oc.airorbs.task.restock.RestockTask;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.Paint;
import net.rlbot.script.api.quests.tutorialisland.TutorialIsland;
import net.rlbot.script.api.ItemPriceAPI;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.Task;
import net.rlbot.script.api.task.TaskQueueScript;
import net.rlbot.script.farm.api.MulingApi;
import net.rlbot.script.farm.api.invoker.ApiClient;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@ScriptManifest(author = "Dijkstra", name = "Air Orbs", version = Constants.VERSION)
public class AirOrbs extends TaskQueueScript implements RenderListener {

    @Override
    public boolean onStart(String[] args) {

        ItemPriceAPI.initializePriceSummary();

        var tasks = new ArrayDeque<Task>();

        var magicLevel = Skills.getLevel(Skill.MAGIC);

        tasks.add(new TutorialIsland());

        if(magicLevel < 43){

            SplashingSpell startSpell = SplashingSpell.getStartSpell();

            tasks.add(new RestockTask(SplashingSpell.getAllTradeables(startSpell)));

            tasks.add(new SplashingTask(startSpell, startSpell.getStopLevel()));

            for(SplashingSpell spell : SplashingSpell.values()){

                if(spell.getStartLevel() >= startSpell.getStopLevel())
                    tasks.add(new SplashingTask(spell, spell.getStopLevel()));

            }
        }

        if(Skills.getExperience(Skill.MAGIC) < 472254){

            if(Skills.getLevel(Skill.SMITHING) < 15) {
                tasks.add(new SuperHeatTask(Bar.BRONZE, () -> Skills.getLevel(Skill.SMITHING) >= 15));
                tasks.add(new RestockTask(Set.of(new Tradeable(ItemId.BRONZE_BAR))));
            }

            tasks.add(new SuperHeatTask(Bar.IRON, () -> Skills.getExperience(Skill.MAGIC) >= 472254));
            tasks.add(new RestockTask(Set.of(new Tradeable(ItemId.IRON_BAR))));
        }

        tasks.add(new CombatTask(66));

        tasks.add(new AirOrbsTask(null));

        setTasks(tasks);

        super.onStart(args);

        return true;
    }

    private static MulingApi initializeMulingApi(String[] args) {

        if(args.length == 0) {
            return null;
        }

        log.debug("Initializing muling api with key: {}", args[0]);
        var apiClient = new ApiClient();
        apiClient.setApiKey(args[0]);
        apiClient.setDebugging(true);
        return new MulingApi(apiClient);
    }


    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;

    @Override
    public void notify(RenderEvent renderEvent) {

        Graphics2D g = (Graphics2D) renderEvent.getGraphics();

        Duration runningTime = getRuntime();

        long hours = runningTime.getSeconds() / SECONDS_IN_HOUR;
        long minutes = (runningTime.getSeconds() % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        long seconds = (runningTime.getSeconds() % SECONDS_IN_MINUTE);

        var lines = new ArrayList<String>();
        lines.add("dAirOrbs v" + Constants.VERSION);
        lines.add(String.format("Time ran: %02d:%02d:%02d", hours, minutes, seconds));

        var task = getCurrentTask();

        if(task != null) {
            lines.addAll(task.getPaintInfo());
        }

        Paint.drawStrings(g, lines);

        try {
            var pathFuture = Walker.pathFuture;

            List<Position> path;

            if(pathFuture == null || (path = pathFuture.get()) == null) {
                return;
            }

            for(var pos : path) {
                pos.outline(g, Color.RED);
            }
        }catch(Exception ignored) {

        }
    }
}
