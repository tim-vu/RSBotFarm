package net.rlbot.internal.wrapper;

import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;
import net.runelite.api.ObjectComposition;

public abstract class ObjectCompositionWrapper implements ObjectComposition {

    private final ObjectComposition objectComposition;

    public ObjectCompositionWrapper(ObjectComposition objectComposition) {

        this.objectComposition = objectComposition;
    }

    @Override
    public int getId() {

        return objectComposition.getId();
    }

    @Override
    public String getName() {

        return objectComposition.getName();
    }

    @Override
    public String[] getActions() {

        return objectComposition.getActions();
    }

    @Override
    public int getMapSceneId() {

        return objectComposition.getMapSceneId();
    }

    @Override
    public void setMapSceneId(int mapSceneId) {
        objectComposition.setMapSceneId(mapSceneId);
    }

    @Override
    public int getMapIconId() {

        return objectComposition.getMapIconId();
    }

    @Override
    public void setMapIconId(int mapIconId) {
        objectComposition.setMapIconId(mapIconId);
    }

    @Override
    public int[] getImpostorIds() {
        return objectComposition.getImpostorIds();
    }

    @Override
    public ObjectComposition getImpostor() {
        return objectComposition.getImpostor();
    }

    @Override
    public int getVarbitId() {
        return objectComposition.getVarbitId();
    }

    @Override
    public int getVarPlayerId() {
        return objectComposition.getVarPlayerId();
    }

    @Override
    public IterableHashTable<Node> getParams() {
        return objectComposition.getParams();
    }

    @Override
    public void setParams(IterableHashTable<Node> params) {
        objectComposition.setParams(params);
    }

    @Override
    public int getIntValue(int paramID) {
        return objectComposition.getIntValue(paramID);
    }

    @Override
    public void setValue(int paramID, int value) {
        objectComposition.setValue(paramID, value);
    }

    @Override
    public String getStringValue(int paramID) {
        return objectComposition.getStringValue(paramID);
    }

    @Override
    public void setValue(int paramID, String value) {
        objectComposition.setValue(paramID, value);
    }
}
