package net.rlbot.script.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.script.api.restocking.Tradeable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class ItemPriceAPI {

    private static final String RUNELITE_PRICE_URL = "https://prices.runescape.wiki/api/v1/osrs/latest";

    private static JsonObject RUNELITE_PRICE_SUMMARY;

    public static long getTotalSellPrice(Set<Tradeable> tradeables) {

        return tradeables.stream().filter(Tradeable::needsSelling).mapToInt(t -> getSellPrice(t.getItemId()) * (t.getCurrentAmount() - t.getRestockAmount())).sum();
    }

    public static long getTotalBuyPrice(Set<Tradeable> tradeables) {

        return tradeables.stream().filter(Tradeable::needsBuying).mapToInt(t -> getBuyPrice(t.getItemId()) * (t.getRestockAmount() - t.getCurrentAmount())).sum();
    }

    public static long getTotalTradePrice(Set<Tradeable> tradeables) {

        return tradeables.stream().filter(t -> t.needsSelling() || t.needsBuying()).mapToInt(t -> {

            if (t.needsBuying())
                return +getBuyPrice(t.getItemId()) * (t.getRestockAmount() - t.getCurrentAmount());
            else
                return -getSellPrice(t.getItemId()) * (t.getCurrentAmount() - t.getRestockAmount());

        }).sum();
    }

    public static int getSellPrice(int itemId) {

        if (RUNELITE_PRICE_SUMMARY == null)
            return 0;

        final JsonObject itemSummary = RUNELITE_PRICE_SUMMARY.getAsJsonObject(Integer.toString(itemId));

        return itemSummary == null ? 0 : itemSummary.get("low").getAsInt();
    }

    public static int getBuyPrice(int itemId) {

        if (RUNELITE_PRICE_SUMMARY == null)
            return 0;

        final JsonObject itemSummary = RUNELITE_PRICE_SUMMARY.getAsJsonObject(Integer.toString(itemId));

        return itemSummary == null ? 0 : itemSummary.get("high").getAsInt();
    }

    public static void initializePriceSummary() {

        if (RUNELITE_PRICE_SUMMARY != null)
            return;

        var request = new Request.Builder().url(RUNELITE_PRICE_URL).get().build();

        var okHttpClient = new OkHttpClient();

        try(var response = okHttpClient.newCall(request).execute()){


            if (!response.isSuccessful())
                return;

            if (response.body() == null)
                return;

            Gson gson = new Gson();
            var root = gson.fromJson(response.body().string(), JsonObject.class);

            if(root == null) {
                return;
            }

            RUNELITE_PRICE_SUMMARY = root.getAsJsonObject("data");

        } catch (IOException ex) {
            log.info("An exception occurred while fetching the price summary: " + ex.getMessage());
        }
    }

}
