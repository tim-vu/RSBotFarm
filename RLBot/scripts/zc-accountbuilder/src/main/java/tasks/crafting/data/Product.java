package tasks.crafting.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import tasks.crafting.enums.Facility;

import java.util.Set;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true)
public abstract class Product {

    @Getter
    int toolItemId;

    @Getter
    Set<Resource> resources;

    @Getter
    Facility facility;

    @Getter
    int ticks;

    public abstract void make();
}
