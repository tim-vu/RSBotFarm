package tasks.prayer.gildedaltar;

import net.rlbot.api.game.Skill;
import net.rlbot.script.api.TempMemory;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.node.Node;
import net.rlbot.script.api.restocking.RestockingSettings;
import net.rlbot.script.api.restocking.Tradeable;
import net.rlbot.script.api.task.TaskBase;
import net.rlbot.script.api.tree.Blackboard;
import net.rlbot.script.api.tree.decisiontree.DecisionTree;
import tasks.prayer.gildedaltar.behaviour.Behaviour;
import tasks.prayer.gildedaltar.data.Keys;
import tasks.prayer.gildedaltar.enums.Bone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class GildedAltarTask extends TaskBase {

    private static final int MAX_BONES = 50;
    private static final int UNNOTED_BONES_IN_INVENTORY = 26;
    private static final int COINS_PER_NOTED_BONE = 50;
    private static final int BONES_PER_RESTOCK = 50;

    private final DecisionTree behaviour;

    private final BooleanSupplier stopCondition;

    @Override
    public Node getNode() {
        return this.behaviour.getValidNode();
    }

    public GildedAltarTask(Bone bone, BooleanSupplier stopCondition) {
        super("Gilded altar");
        var restockingSettings = RestockingSettings.builder()
                    .sellBeforeBuy(false)
                    .sellItems(false)
                    .tradeables(getTradeables(bone))
                .build();

        this.behaviour = Behaviour.buildTree(createBlackboard(bone), restockingSettings);
        this.stopCondition = stopCondition;
    }

    private static Blackboard createBlackboard(Bone bone) {
        var blackboard = new Blackboard();
        blackboard.put(Keys.BONE, bone);
        var loadout = getLoadout(bone);
        blackboard.put(Keys.LOADOUT, loadout);
        blackboard.put(Keys.HAS_LOADOUT, new TempMemory<>(() -> LoadoutManager.hasLoadout(loadout), 5000));
        return blackboard;
    }

    private static Loadout getLoadout(Bone bone) {

        var notedBones = MAX_BONES - UNNOTED_BONES_IN_INVENTORY;

        var builder = Loadout.builder();

        builder.withItem(bone.getItemId()).amount(UNNOTED_BONES_IN_INVENTORY).build();

        if(notedBones > 0) {
            builder.withItem(bone.getItemId()).amount(1, notedBones).noted().build();
            builder.withItem(ItemId.COINS_995).amount(notedBones * COINS_PER_NOTED_BONE).build();
        }

        builder.withEquipmentSet()
                    .with(ItemIds.BURNING_AMULET).build()
                    .with(ItemIds.RING_OF_WEALTH).build()
                .build();

        return builder.build();
    }

    private static Set<Tradeable> getTradeables(Bone bone) {
        var tradeables = new HashSet<Tradeable>();

        int tripCount = (BONES_PER_RESTOCK / MAX_BONES / 5) + 1;

        tradeables.add(new Tradeable(ItemIds.BURNING_AMULET.get(0), tripCount));
        tradeables.add(new Tradeable(ItemIds.RING_OF_WEALTH.get(0), tripCount));
        tradeables.add(new Tradeable(bone.getItemId(), BONES_PER_RESTOCK));

        return tradeables;
    }

    @Override
    public boolean isDone() {
        return this.stopCondition.getAsBoolean();
    }

    private static final int SECONDS_IN_HOUR = 60 * 60;

    @Override
    public List<String> getPaintInfo() {

        var xpGained = getExperiencedGained().getOrDefault(Skill.PRAYER, 0);
        var hourlyXp = (xpGained / getRuntime().getSeconds()) * SECONDS_IN_HOUR;

        var lines = new ArrayList<>(super.getPaintInfo());
        lines.add(String.format("Experience gained (/h): %.02fk (%.02fk)", xpGained / (double)1000, hourlyXp / (double)1000));
        return lines;
    }
}
