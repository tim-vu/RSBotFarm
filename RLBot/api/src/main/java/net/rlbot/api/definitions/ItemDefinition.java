package net.rlbot.api.definitions;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import net.rlbot.internal.ApiContext;
import net.runelite.api.ItemComposition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Value
public class ItemDefinition {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final int BASE_ACTION_PARAM = 451;

    private static final int MAX_CUSTOM_ACTIONS = 8;

    ItemDefinition(ItemComposition def) {
        this.name = def.getName();
        this.memberName = def.getName();
        this.noted = def.getNote() > -1;
        this.notedId = def.getLinkedNoteId();
        this.stackable = def.isStackable();
        this.placeholderTemplateId = def.getPlaceholderTemplateId();
        this.tradeable = def.isTradeable();
        this.members = def.isMembers();
        this.price = def.getPrice();

        this.customActions = new String[8];

        var index = 0;
        for (var param = BASE_ACTION_PARAM; param < (BASE_ACTION_PARAM + MAX_CUSTOM_ACTIONS) - 1; param++)
        {
            this.customActions[index++] = def.getStringValue(param);
        }
    }

    String name;

    String memberName;

    boolean noted;

    int notedId;

    boolean stackable;

    int placeholderTemplateId;

    boolean tradeable;

    boolean members;

    int price;

    String[] customActions;

    public static int getNotedId(int itemId) {
        return Definitions.getItemDefinition(itemId).getNotedId();
    }

    public static boolean isStackable(int itemId) {
        return Definitions.getItemDefinition(itemId).isStackable();
    }

    public static String getName(int itemId) {
        return Definitions.getItemDefinition(itemId).getName();
    }
}
