package net.rlbot.api.widgets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.adapter.component.Widget;

@AllArgsConstructor
public class WidgetAddress {

    @Getter
    private final int groupId;

    @Getter
    private final int id;

    @Getter
    private final Integer childId;

    public WidgetAddress(int groupId, int id) {
        this(groupId, id, null);
    }

    public boolean isWidgetVisible() {

        if(childId == null) {
            return Widgets.isVisible(groupId, id);
        }

        return Widgets.isVisible(groupId, id, childId);
    }

    public Widget resolve() {

        if(childId == null) {
            return Widgets.get(groupId, id);
        }

        return Widgets.get(groupId, id, childId);
    }
}
