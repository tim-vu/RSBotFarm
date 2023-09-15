package tunnels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.movement.position.Area;

@Value
public class Room {

    Area area;

    boolean chestRoom;

    public Room(Area area) {
        this(area, false);
    }

    public Room(Area area, boolean chestRoom) {
        this.area = area;
        this.chestRoom = chestRoom;
    }
}
