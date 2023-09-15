package tasks.crafting.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.definitions.ItemDefinition;

@FieldDefaults(makeFinal = true)
@Getter
@EqualsAndHashCode
public class Resource {

    int itemId;

    int count;

    int uses;

    public Resource(int itemId, int count) {
        this(itemId, count, 1);
    }

    public Resource(int itemId, int count, int uses) {
        this.itemId = itemId;
        this.count = count;
        this.uses = uses;
    }
    public boolean isStackable() {
        return ItemDefinition.isStackable(itemId);
    }
}