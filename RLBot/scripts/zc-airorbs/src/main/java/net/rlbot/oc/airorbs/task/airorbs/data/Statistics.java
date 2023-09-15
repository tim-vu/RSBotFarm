package net.rlbot.oc.airorbs.task.airorbs.data;

import lombok.Getter;
import net.rlbot.api.script.Stopwatch;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.ItemPriceAPI;

public class Statistics {

    private static final int SECONDS_IN_HOUR = 3600;

    public void initialize(){
        this.stopwatch.resume();
    }

    private final Stopwatch stopwatch = Stopwatch.createPaused();

    public void incrementDeaths(){
        deaths++;
    }

    @Getter
    private int deaths;

    public void incrementOrbsCharged(){
        this.orbsCharged++;
    }

    @Getter
    private int orbsCharged;

    public void incrementGloryDosesUsed(){
        this.gloryDosesUsed++;
    }

    @Getter
    private int gloryDosesUsed;

    public void incrementDuelingDosesUsed(){
        this.duelingDosesUsed++;
    }

    @Getter
    private int duelingDosesUsed;

    public int getTotalProfit(){

        int totalProfit = 0;

        int orbMargin = ItemPriceAPI.getSellPrice(ItemId.AIR_ORB) - ItemPriceAPI.getBuyPrice(ItemId.UNPOWERED_ORB);

        totalProfit += orbMargin*getOrbsCharged();

        totalProfit -= ItemPriceAPI.getBuyPrice(ItemId.COSMIC_RUNE) * 3 * getOrbsCharged();

        totalProfit -= (ItemPriceAPI.getBuyPrice(ItemIds.RING_OF_DUELING.get(0)) / 8)*getDuelingDosesUsed();

        totalProfit -= (ItemPriceAPI.getBuyPrice(ItemIds.AMULET_OF_GLORY.get(0)) - ItemPriceAPI.getSellPrice(ItemId.AMULET_OF_GLORY)) / 6 * getGloryDosesUsed();

        return totalProfit;
    }

    public int getHourlyProfit(){
        return (int)(getTotalProfit() / (float)this.stopwatch.getDuration().getSeconds())*SECONDS_IN_HOUR;
    }
}
