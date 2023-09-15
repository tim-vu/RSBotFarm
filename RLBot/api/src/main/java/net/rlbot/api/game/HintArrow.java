package net.rlbot.api.game;

import net.rlbot.internal.ApiContext;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.Player;
import net.rlbot.api.movement.Position;

public class HintArrow {

    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    public static boolean isPresent() {
        var type = HintArrowType.fromValue(API_CONTEXT.getClient().getHintArrowType());
        return type != null && type != HintArrowType.NONE;
    }

    public static HintArrowType getType() {
        return HintArrowType.fromValue(API_CONTEXT.getClient().getHintArrowType());
    }

    public static Actor getTargetActor() {
        var npc = API_CONTEXT.getClient().getHintArrowNpc();

        if(npc != null) {
            return new Npc(npc);
        }

        var player = API_CONTEXT.getClient().getHintArrowPlayer();

        if(player != null) {
            return new Player(player);
        }

        return null;
    }

    public static Position getTargetPosition() {
        var target = API_CONTEXT.getClient().getHintArrowPoint();

        if(target == null) {
            return null;
        }

        return new Position(target.getX(), target.getY(), target.getPlane());
    }
}
