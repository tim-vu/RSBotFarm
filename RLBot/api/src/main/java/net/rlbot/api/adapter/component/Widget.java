package net.rlbot.api.adapter.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.common.Interactable;
import net.rlbot.internal.Interaction;
import net.rlbot.internal.menu.Menu;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.internal.util.Text;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.WidgetType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class Widget implements Interactable {

    private static ApiContext API_CONTEXT;

    static void init(@NonNull ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public int getIndex() {
        return this.widget.getIndex();
    }

    @Getter
    private final int id;

    @Getter
    private final int parentId;

    private final net.runelite.api.widgets.Widget widget;

    private final net.runelite.api.widgets.Widget parentWidget;

    public Widget(@NonNull net.runelite.api.widgets.Widget widget) {
        this.id = widget.getId();
        this.widget = widget;
        this.parentWidget = widget.getParent();
        this.parentId = (parentWidget != null) ? parentWidget.getId() : -1;
    }

    public boolean isVisible() {
        return (isSelfVisible() && !this.widget.isHidden());
    }

    public boolean isSelfVisible() {return !this.widget.isSelfHidden();}

    public boolean isHidden() {
        return this.widget.isHidden();
    }

    public int getType() {
        return this.widget.getType();
    }

    public int getBorderType() {
        return this.widget.getBorderType();
    }

    public int getAbsoluteX() {
        return this.widget.getCanvasLocation().getX();
    }

    public int getAbsoluteY() {
        return this.widget.getCanvasLocation().getY();
    }

    public int getRelativeX() {
        return this.widget.getRelativeX();
    }

    public int getRelativeY() {
        return this.widget.getRelativeY();
    }

    public Rectangle getArea() {
        return this.widget.getBounds();
    }

    @Nullable
    public Widget[] getChildren() {
        var children = this.widget.getChildren();

        if(children == null) {
            return null;
        }

        var result = new Widget[children.length];

        for(var i = 0; i < result.length; i++){

            var child = children[i];

            if(child == null) {
                continue;
            }

            result[i] = new Widget(child);
        }

        return result;
    }

    @Nullable
    public Widget getChild(int idx) {

        var widget = this.widget.getChild(idx);

        if(widget == null) {
            return null;
        }

        return new Widget(widget);
    }

    public Widget[] getDynamicChildren() {
        var children = this.widget.getDynamicChildren();

        var result = new Widget[children.length];

        for(var i = 0; i < result.length; i++) {
            result[i] = new Widget(children[i]);
        }

        return result;
    }

    public Widget[] getStaticChildren() {

        var children = this.widget.getStaticChildren();

        var result = new Widget[children.length];

        for(var i = 0; i < result.length; i++) {
            result[i] = new Widget(children[i]);
        }

        return result;
    }

    public int getItemQuantity() {
        return this.widget.getItemQuantity();
    }

    public String getName() {
        return this.widget.getName();
    }

    public int getHeight() {

        if (!isInScrollableArea()) {
            return getRealHeight();
        }

        return this.widget.getHeight() - 4;
    }

    public int getSpriteId() {
        return this.widget.getSpriteId();
    }

    public int getItemId() {
        return this.widget.getItemId();
    }

    public int getModelId() {
        return this.widget.getModelId();
    }

    public Widget getParent() {
        return new Widget(parentWidget);
    }

    public int getGroupIndex() {
        return WidgetInfo.TO_GROUP(this.widget.getId());
    }

    public int getChildIndex() {
        return WidgetInfo.TO_CHILD(this.widget.getId());
    }

    public int getVerticalScrollPosition() {
        return this.widget.getScrollY();

    }

    public int getHorizontalScrollPosition() {
        return this.widget.getScrollX();

    }

    public int getScrollableContentHeight() {
        return this.widget.getScrollHeight();

    }

    public int getScrollableContentWidth() {
        return this.widget.getScrollWidth();
    }

    public int getRealHeight() {
        return this.widget.getScrollHeight();
    }

    public int getRealWidth() {
        return this.widget.getScrollWidth();
    }

    public boolean isInScrollableArea() {

        //Check if we have a parent
        if (this.getParentId() == -1) {
            return false;
        }

        //Find scrollable area
        Widget scrollableArea = this.getParent();
        while ((scrollableArea.getScrollableContentHeight() == 0) && (scrollableArea.getParentId() != -1)) {
            scrollableArea = scrollableArea.getParent();
        }

        //Return if we are in a scrollable area
        return (scrollableArea.getScrollableContentHeight() != 0);
    }

    public String getText() {
        return this.widget.getText();
    }

    public boolean containsAction(final String phrase) {

        for (final String action : getActions()) {
            if (action.toLowerCase().contains(phrase.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAction(String action) {

        for(var a : getActions()) {

            if (!a.equals(action)) {
                continue;
            }

            return true;
        }

        return false;
    }

    public static final int WIDGET_TYPE_INVENTORY = 2;

    @Override
    public boolean interact(int index) {

        if(index < 0 || index > 4) {
            return false;
        }

        var identifier = index + 1;

        var param0 = getIndex();
        var param1 = getId();

        Interaction.log("WIDGET", kv("menuIndex", index), kv("widgetId", getId()));
        MousePackets.queueClickPacket();
        return Menu.invokeWidgetDefaultMenuAction(identifier, param0, param1, this.widget.getItemId());
    }

    public String[] getActions() {
        String[] rawActions = this.widget.getActions();
        if (rawActions == null)
        {
            return null;
        }

        String[] sanitized = new String[rawActions.length];
        for (int i = 0; i < rawActions.length; i++)
        {
            sanitized[i] = Text.sanitize(rawActions[i]);
        }

        return sanitized;
    }

    public int getWidth() {

        if (!isInScrollableArea()) {
            return getRealWidth();
        }

        return this.widget.getWidth() - 4;
    }

    public Rectangle getBounds() {
        return this.widget.getBounds();
    }

    public Object[] getOnOpListener() {
        return this.widget.getOnOpListener();
    }
    @Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof final Widget child) {
            return (id == child.getId()) && child.parentWidget.equals(parentWidget);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.widget.hashCode();
    }

}
