package tasks.prayer.gildedaltar.behaviour.decisions;

import net.rlbot.api.items.Inventory;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.Areas;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tasks.prayer.gildedaltar.behaviour.Wilderness;
import tasks.prayer.gildedaltar.data.Constants;
import tasks.prayer.gildedaltar.data.Keys;

public class Decisions {

    public static Decision isRestocking() {
        return c -> c.get(RestockingKeys.IS_RESTOCKING);
    }

    public static Decision hasLoadout() {
        return c -> c.get(Keys.HAS_LOADOUT).get();
    }

    public static Decision arePlayersNearby() {
        return c -> Wilderness.arePlayersNearby();
    }

    public static Decision hasBones() {
        return c -> {
            var bones = c.get(Keys.BONE);
            return Inventory.contains(bones.getItemId()) || Inventory.contains(bones.getNotedId());
        };
    }

    public static Decision hasUnnotedBones() {
        return c -> Inventory.contains(c.get(Keys.BONE).getItemId());
    }

    public static Decision isAtGildedAltar() {
        return c -> Constants.GILDED_ALTAR.contains(Players.getLocal());
    }

    public static Decision isAtGrandExchange() {
        return c -> Areas.GRAND_EXCHANGE.contains(Players.getLocal());
    }

    public static Decision isInWilderness() {
        return c -> Players.getLocal().getY() > 3250;
    }

}
