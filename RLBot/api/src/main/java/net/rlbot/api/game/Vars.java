package net.rlbot.api.game;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;

public class Vars
{
    private static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static int getBit(int id)
    {
        var client = API_CONTEXT.getClient();
        return client.getVarbitValue(client.getVarps(), id);
    }

    public static int getVarp(int id)
    {
        return API_CONTEXT.getClient().getVarpValue(id);
    }

    public static int getVarcInt(int varClientInt)
    {
        return API_CONTEXT.getClient().getVarcIntValue(varClientInt);
    }

    public static String getVarcStr(int varClientStr)
    {
        return API_CONTEXT.getClient().getVarcStrValue(varClientStr);
    }

    public static void setVarcInt(int varClientInt, int value) {
        API_CONTEXT.getClient().setVarcIntValue(varClientInt, value);
    }

    public static void setVarcStr(int varClientStr, String value) {
        API_CONTEXT.getClient().setVarcStrValue(varClientStr, value);
    }
}
