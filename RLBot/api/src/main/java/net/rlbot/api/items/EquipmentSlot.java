package net.rlbot.api.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.common.Interactable;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.internal.ApiContext;
import net.runelite.api.InventoryID;

import java.util.function.Predicate;

@AllArgsConstructor
public enum EquipmentSlot implements Interactable {

    HEAD(0, WidgetInfo.EQUIPMENT_HELMET),
    CAPE(1, WidgetInfo.EQUIPMENT_CAPE),
    AMULET(2, WidgetInfo.EQUIPMENT_AMULET),
    WEAPON(3, WidgetInfo.EQUIPMENT_WEAPON),
    BODY(4, WidgetInfo.EQUIPMENT_BODY),
    SHIELD(5, WidgetInfo.EQUIPMENT_SHIELD),
    LEGS(7, WidgetInfo.EQUIPMENT_LEGS),
    GLOVES(9, WidgetInfo.EQUIPMENT_GLOVES),
    BOOTS(10, WidgetInfo.EQUIPMENT_BOOTS),
    RING(12, WidgetInfo.EQUIPMENT_RING),
    AMMO(13, WidgetInfo.EQUIPMENT_AMMO);

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    @Getter
    private final int slot;

    @Getter
    private final WidgetInfo widgetInfo;

    public int getItemId() {
        var item = getItem(this.slot);

        if(item == null) {
            return -1;
        }

        return item.getId();
    }

    @Override
    public String[] getActions() {

        var item = getItem(this.slot);

        if(item == null) {
            return null;
        }

        return item.getActions();
    }

    @Override
    public boolean interact(int index) {

        var item = getItem(this.slot);

        if(item == null) {
            return false;
        }

        return item.interact(index);
    }

    private static Item getItem(int slot) {
        var container = API_CONTEXT.getInventoryManager().getCachedItemContainers().get(InventoryID.EQUIPMENT.getId());

        for(var item : container) {

            if(item.getSlot() != slot) {
                continue;
            }

            return item;
        }

        return null;
    }
}
