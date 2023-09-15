package tasks.prayer.gildedaltar.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.script.api.data.ItemId;

@AllArgsConstructor
public enum Bone {

    BONES(ItemId.BONES),
    DRAGON_BONES(ItemId.DRAGON_BONES);

    @Getter
    private final int itemId;

    public int getNotedId(){
        return ItemDefinition.getNotedId(this.itemId);
    }

}

