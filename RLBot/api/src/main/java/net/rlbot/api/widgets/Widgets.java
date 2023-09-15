package net.rlbot.api.widgets;

import lombok.NonNull;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.component.Widget;
import org.jetbrains.annotations.Nullable;

public class Widgets {

    private static ApiContext API_CONTEXT;

    private static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static boolean isVisible(int groupId, int id) {
        var widget = API_CONTEXT.getClient().getWidget(groupId, id);

        if(widget == null) {
            return false;
        }

        return API_CONTEXT.getClient().runOnClientThread(() -> !widget.isSelfHidden() && !widget.isHidden());
    }

    public static boolean isVisible(int groupId, int id, int childId) {
        var widget = API_CONTEXT.getClient().getWidget(groupId, id);

        if(widget == null)
            return false;

        var child = widget.getChild(childId);

        if(child == null) {
            return false;
        }

        return API_CONTEXT.getClient().runOnClientThread(() -> !child.isSelfHidden() && !child.isHidden());
    }

    public static boolean isVisible(WidgetInfo info) {
        var widget = API_CONTEXT.getClient().getWidget(info.getPackedId());

        if(widget == null)
        {
            return false;
        }

        return API_CONTEXT.getClient().runOnClientThread(() -> !widget.isSelfHidden() && !widget.isHidden());
    }

    public static boolean isVisible(Widget widget) {
        return widget != null && widget.isVisible();
    }

    @Nullable
    public static Widget get(int packedId) {
        var widget = API_CONTEXT.getClient().getWidget(packedId);

        if(widget == null) {
            return null;
        }

        return new Widget(widget);
    }

    @Nullable
    public static Widget get(int groupId, int id) {

        var widget = API_CONTEXT.getClient().getWidget(groupId, id);

        if(widget == null) {
            return null;
        }

        return new Widget(widget);
    }

    @Nullable
    public static Widget get(int groupId, int id, int childId) {
        var widget = get(groupId, id);

        if(widget == null) {
            return null;
        }

        return widget.getChild(childId);
    }

    @Nullable
    public static Widget get(WidgetInfo info) {

        var widget = API_CONTEXT.getClient().getWidget(info.getPackedId());

        if(widget == null) {
            return null;
        }

        return new Widget(widget);
    }
}
