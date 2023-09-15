package net.rlbot.internal.wrapper;

import lombok.SneakyThrows;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.worldmap.MapElementConfig;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;

@Singleton
public class ClientImpl extends ClientWrapper {

    private final Queue<FutureTask<Object>> taskQueue = new ConcurrentLinkedQueue<>();

    @Inject
    public ClientImpl(Client client, ClientThread clientThread) {
        super(client);
        clientThread.invoke(() -> {
            final var expirationTime = System.nanoTime() + 100_000 * 20;

            if (taskQueue.isEmpty())
                return false;

            while (System.nanoTime() < expirationTime) {
                final var task = taskQueue.poll();
                if (task != null) {
                    task.run();
                }
            }
            return false;
        });
    }

    private void runTask(FutureTask<Object> task) {
        if (super.isClientThread()) {
            task.run();
        } else {
            taskQueue.add(task);
        }
    }

    @SneakyThrows
    public void runOnClientThread(Runnable method) {
        final var task = new FutureTask<>(() -> {
            method.run();
            return null;
        });
        runTask(task);
        task.get();
    }

    @SuppressWarnings("unchecked")
    private <T> T convertResult(T result) {
        if (result instanceof Widget concreteResult) {
            return (T) new WidgetWrapperImpl(concreteResult);
        } else if (result instanceof Widget[] concreteResult) {
            WidgetWrapperImpl[] convertedResult = new WidgetWrapperImpl[concreteResult.length];
            for (int i = 0 ; i < concreteResult.length ; i++) {
                convertedResult[i] = new WidgetWrapperImpl(concreteResult[i]);
            }
            return (T) convertedResult;
        }
        return result;
    }

    private Widget[] convertArg(Widget[] arg) {
        Widget[] convertedArg = new Widget[arg.length];
        for (int i = 0 ; i < arg.length ; i++) {
            if (arg[i] instanceof WidgetWrapperImpl widgetWrapperImpl) {
                convertedArg[i] = widgetWrapperImpl.widget;
            } else {
                convertedArg[i] = arg[i];
            }
        }
        return convertedArg;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T> T runOnClientThread(Callable<T> method) {
        final var task = new FutureTask<Object>(() -> convertResult(method.call()));
        runTask(task);
        return (T) task.get();
    }

    @Override
    @Nullable
    public ObjectComposition getObjectDefinition(int id) {

        var def = super.getObjectDefinition(id);

        if(def == null) {
            return null;
        }

        return new ObjectCompositionWrapperImpl(def);
    }

    @Override
    public List<Integer> getDBRowsByValue(int table, int column, int tupleIndex, Object value) {
        return null;
    }

    @Override
    public void runScript(Object... args) {
        runOnClientThread(() -> super.runScript(args));
    }

    @Override
    @Nullable
    public Widget getDraggedWidget() {
        return convertResult(super.getDraggedWidget());
    }

    @Override
    @Nullable
    public Widget getDraggedOnWidget() {
        return convertResult(super.getDraggedOnWidget());
    }

    @Override
    public void setDraggedOnWidget(Widget widget) {
        super.setDraggedOnWidget(((WidgetWrapperImpl) widget).widget);
    }

    @Override
    public Widget[] getWidgetRoots() {
        return convertResult(super.getWidgetRoots());
    }

    @SneakyThrows
    @Override
    @Nullable
    public Widget getWidget(WidgetInfo widget) {
        return convertResult(super.getWidget(widget));
    }

    @Override
    @Nullable
    public Widget getWidget(int groupId, int childId) {
        return convertResult(super.getWidget(groupId, childId));
    }

    @Override
    @Nullable
    public Widget getWidget(int packedID) {
        return convertResult(super.getWidget(packedID));
    }

    @Override
    public MapElementConfig getMapElementConfig(int id) {
        return convertResult(super.getMapElementConfig(id));
    }

    @Override
    public Widget getScriptActiveWidget() {
        return convertResult(super.getScriptActiveWidget());
    }

    @Override
    public Widget getScriptDotWidget() {
        return convertResult(super.getScriptDotWidget());
    }

    @Override
    @Deprecated
    public RenderOverview getRenderOverview() {
        return null;
    }

    @Override
    public void setHintArrow(LocalPoint point) {
        super.setHintArrow(point);
    }

    @Override
    public boolean isWidgetSelected() {
        return false;
    }

    @Override
    @Nullable
    public Widget getSelectedWidget() {
        return convertResult(super.getSelectedWidget());
    }

    @Override
    public void setWidgetSelected(boolean selected) {}

    @Override
    public void setIdleTimeout(int ticks) {
        super.setIdleTimeout(ticks);
    }

    @Override
    public int getIdleTimeout() {
        return convertResult(super.getIdleTimeout());
    }

    @Override
    public void setMinimapTileDrawer(TileFunction drawTile) {

    }

    @Override
    public Rasterizer getRasterizer() {
        return null;
    }

    @Override
    public int getSceneMaxPlane() {
        return super.getSceneMaxPlane();
    }

    @Override
    public Player getLocalPlayer() {
        return super.getLocalPlayer();
    }

    @Override
    @NotNull
    public ItemComposition getItemDefinition(int id) {
        return runOnClientThread(() -> super.getItemDefinition(id));
    }

    private class WidgetWrapperImpl extends WidgetWrapper {

        WidgetWrapperImpl(Widget widget) {
            super(widget);
        }

        @Override
        public Widget getParent() {
            return runOnClientThread(super::getParent);
        }

        @Override
        public int getParentId() {
            return runOnClientThread(super::getParentId);
        }

        @Override
        public Widget setContentType(int contentType) {
            return convertResult(super.setContentType(contentType));
        }

        @Override
        public Widget setClickMask(int mask) {
            return convertResult(super.setClickMask(mask));
        }

        @Override
        public Widget getChild(int index) {
            return convertResult(super.getChild(index));
        }

        @Override
        @Nullable
        public Widget[] getChildren() {
            return convertResult(super.getChildren());
        }

        @Override
        public void setChildren(Widget[] children) {
            super.setChildren(convertArg(children));
        }

        @Override
        public Widget[] getDynamicChildren() {
            return convertResult(super.getDynamicChildren());
        }

        @Override
        public Widget[] getStaticChildren() {
            return convertResult(super.getStaticChildren());
        }

        @Override
        public Widget[] getNestedChildren() {
            return convertResult(super.getNestedChildren());
        }

        @Override
        public Widget setText(String text) {
            return convertResult(super.setText(text));
        }

        @Override
        public Widget setTextColor(int textColor) {
            return convertResult(super.setTextColor(textColor));
        }

        @Override
        public Widget setOpacity(int transparency) {
            return convertResult(super.setOpacity(transparency));
        }

        @Override
        public Widget setName(String name) {
            return convertResult(super.setName(name));
        }

        @Override
        public Widget setModelId(int id) {
            return convertResult(super.setModelId(id));
        }

        @Override
        public Widget setModelType(int type) {
            return convertResult(super.setModelType(type));
        }

        @Override
        public Widget setAnimationId(int animationId) {
            return convertResult(super.setAnimationId(animationId));
        }

        @Override
        public Widget setRotationX(int modelX) {
            return convertResult(super.setRotationX(modelX));
        }

        @Override
        public Widget setRotationY(int modelY) {
            return convertResult(super.setRotationY(modelY));
        }

        @Override
        public Widget setRotationZ(int modelZ) {
            return convertResult(super.setRotationZ(modelZ));
        }

        @Override
        public Widget setModelZoom(int modelZoom) {
            return convertResult(super.setModelZoom(modelZoom));
        }

        @Override
        public Widget setSpriteTiling(boolean tiling) {
            return convertResult(super.setSpriteTiling(tiling));
        }

        @Override
        public Widget setSpriteId(int spriteId) {
            return convertResult(super.setSpriteId(spriteId));
        }

        @Override
        public Widget setHidden(boolean hidden) {
            return convertResult(super.setHidden(hidden));
        }

        @Override
        public Widget setItemId(int itemId) {
            return convertResult(super.setItemId(itemId));
        }

        @Override
        public Widget setItemQuantity(int quantity) {
            return convertResult(super.setItemQuantity(quantity));
        }

        @Override
        public Widget setScrollX(int scrollX) {
            return convertResult(super.setScrollX(scrollX));
        }

        @Override
        public Widget setScrollY(int scrollY) {
            return convertResult(super.setScrollY(scrollY));
        }

        @Override
        public Widget setScrollWidth(int width) {
            return convertResult(super.setScrollWidth(width));
        }

        @Override
        public Widget setScrollHeight(int height) {
            return convertResult(super.setScrollHeight(height));
        }

        @Override
        public Widget setOriginalX(int originalX) {
            return convertResult(super.setOriginalX(originalX));
        }

        @Override
        public Widget setOriginalY(int originalY) {
            return convertResult(super.setOriginalY(originalY));
        }

        @Override
        public Widget setPos(int x, int y) {
            return convertResult(super.setPos(x, y));
        }

        @Override
        public Widget setPos(int x, int y, int xMode, int yMode) {
            return convertResult(super.setPos(x, y, xMode, yMode));
        }

        @Override
        public Widget setOriginalHeight(int originalHeight) {
            return convertResult(super.setOriginalHeight(originalHeight));
        }

        @Override
        public Widget setOriginalWidth(int originalWidth) {
            return convertResult(super.setOriginalWidth(originalWidth));
        }

        @Override
        public Widget setSize(int width, int height) {
            return convertResult(super.setSize(width, height));
        }

        @Override
        public Widget setSize(int width, int height, int widthMode, int heightMode) {
            return convertResult(super.setSize(width, height, widthMode, heightMode));
        }

        @Override
        public Widget createChild(int index, int type) {
            return convertResult(super.createChild(index, type));
        }

        @Override
        public Widget createChild(int type) {
            return convertResult(super.createChild(type));
        }

        @Override
        public Widget setHasListener(boolean hasListener) {
            return convertResult(super.setHasListener(hasListener));
        }

        @Override
        public Widget setFontId(int id) {
            return convertResult(super.setFontId(id));
        }

        @Override
        public Widget setTextShadowed(boolean shadowed) {
            return convertResult(super.setTextShadowed(shadowed));
        }

        @Override
        public Widget setItemQuantityMode(int itemQuantityMode) {
            return convertResult(super.setItemQuantityMode(itemQuantityMode));
        }

        @Override
        public Widget setXPositionMode(int xpm) {
            return convertResult(super.setXPositionMode(xpm));
        }

        @Override
        public Widget setYPositionMode(int ypm) {
            return convertResult(super.setYPositionMode(ypm));
        }

        @Override
        public Widget setLineHeight(int lineHeight) {
            return convertResult(super.setLineHeight(lineHeight));
        }

        @Override
        public Widget setXTextAlignment(int xta) {
            return convertResult(super.setXTextAlignment(xta));
        }

        @Override
        public Widget setYTextAlignment(int yta) {
            return convertResult(super.setYTextAlignment(yta));
        }

        @Override
        public Widget setWidthMode(int widthMode) {
            return convertResult(super.setWidthMode(widthMode));
        }

        @Override
        public Widget setHeightMode(int heightMode) {
            return convertResult(super.setHeightMode(heightMode));
        }

        @Override
        public Widget setFilled(boolean filled) {
            return convertResult(super.setFilled(filled));
        }

        @Override
        public Widget getDragParent() {
            return convertResult(super.getDragParent());
        }

        @Override
        public Widget setDragParent(Widget dragParent) {
            return convertResult(super.setDragParent(dragParent));
        }
    }

    private class ObjectCompositionWrapperImpl extends ObjectCompositionWrapper {

        public ObjectCompositionWrapperImpl(ObjectComposition objectComposition) {

            super(objectComposition);
        }

        @Override
        public ObjectComposition getImpostor() {

            var def = runOnClientThread(super::getImpostor);

            if(def == null) {
                return null;
            }

            return new ObjectCompositionWrapperImpl(def);
        }
    }
}
